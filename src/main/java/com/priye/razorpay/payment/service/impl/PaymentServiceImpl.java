package com.priye.razorpay.payment.service.impl;

import com.priye.razorpay.common.enums.OrderStatus;
import com.priye.razorpay.common.enums.PaymentEvent;
import com.priye.razorpay.common.enums.PaymentStatus;
import com.priye.razorpay.common.exception.BusinessRuleViolationException;
import com.priye.razorpay.common.exception.ResourceNotFoundException;
import com.priye.razorpay.payment.dto.request.PaymentInitRequest;
import com.priye.razorpay.payment.dto.response.PaymentResponse;
import com.priye.razorpay.payment.entity.OrderRecord;
import com.priye.razorpay.payment.entity.Payment;
import com.priye.razorpay.payment.gateway.PaymentGatewayRouter;
import com.priye.razorpay.payment.gateway.dto.PaymentRequest;
import com.priye.razorpay.payment.gateway.dto.PaymentResult;
import com.priye.razorpay.payment.mapper.PaymentMapper;
import com.priye.razorpay.payment.repository.OrderRepository;
import com.priye.razorpay.payment.repository.PaymentRepository;
import com.priye.razorpay.payment.service.PaymentService;
import com.priye.razorpay.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionService  paymentTransitionService;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT FOUND", "Order Id :"+request.orderId()));

        if(order.getOrderStatus()!= OrderStatus.CREATED && order.getOrderStatus()!=OrderStatus.ATTEMPTED){
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE", "Order Status :"+order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts()+1);

        order = orderRepository.save(order);

        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .idempotencyKey(UUID.randomUUID().toString()) //TODO: idempotency
                .methodDetails(request.methodDetails())
                .build();

        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                request.orderId(), merchantId,
                order.getAmount(), request.method(),
                request.methodDetails());

        paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_ATTEMPT);
        PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);

        switch (result) {
            case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationRef());
            case PaymentResult.Failure failure -> {
//                payment.setStatus(PaymentStatus.FAILED);
                paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }
            case PaymentResult.Success success -> {
                log.warn("Invalid state");
                return null;
            }
        }

        payment = paymentRepository.save(payment);
        orderRepository.save(order);

        // TODO: send outbox (kafka event)
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {

        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("PAYMENT_NOT_FOUND", "Payment Id :"+paymentId));

        //payment.setStatus(PaymentStatus.CAPTURING); //TODO: State machine
        paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);

        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getMethod(), payment.getId());

        if(paymentResult instanceof PaymentResult.Success success) {
//            payment.setStatus(PaymentStatus.CAPTURED);
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
            log.info("Payment captured successfully, paymentId: {}", payment.getId());
        } else if(paymentResult instanceof PaymentResult.Failure(String errorCode, String errorDescription)) {
//            payment.setStatus(PaymentStatus.AUTHORIZED);
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(errorCode);
            payment.setErrorDescription(errorDescription);
            log.info("Payment capture failed, paymentId: {}", payment.getId());
        }

        payment = paymentRepository.save(payment);

        // TODO: send outbox (kafka event)
        return paymentMapper.toResponse(payment);
    }
}
