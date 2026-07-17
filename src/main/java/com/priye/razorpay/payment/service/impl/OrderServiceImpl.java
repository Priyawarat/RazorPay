package com.priye.razorpay.payment.service.impl;

import com.priye.razorpay.common.enums.OrderStatus;
import com.priye.razorpay.common.exception.BusinessRuleViolationException;
import com.priye.razorpay.common.exception.DuplicateResourceException;
import com.priye.razorpay.common.exception.ResourceNotFoundException;
import com.priye.razorpay.payment.dto.request.CreateOrderRequest;
import com.priye.razorpay.payment.dto.response.OrderResponse;
import com.priye.razorpay.payment.dto.response.PaymentResponse;
import com.priye.razorpay.payment.entity.OrderRecord;
import com.priye.razorpay.payment.entity.Payment;
import com.priye.razorpay.payment.mapper.OrderMapper;
import com.priye.razorpay.payment.mapper.PaymentMapper;
import com.priye.razorpay.payment.repository.OrderRepository;
import com.priye.razorpay.payment.repository.PaymentRepository;
import com.priye.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final OrderMapper  orderMapper;

    @Value("${payment.order.default-expiry-minutes:30}")
    private int defaultExpiryMinutes;

    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {

        if (request.receipt()!=null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())) {
            throw new DuplicateResourceException("ORDER_RECIPT_DUPLICATE", "Order with receipt already exists" + request.receipt());
        }

        OrderRecord order = OrderRecord.builder()
                        .receipt(request.receipt())
                        .amount(request.amount())
                        .notes(request.notes())
                        .merchantId(merchantId)
                        .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt()!=null?request.expiresAt(): LocalDateTime.now().plusMinutes(defaultExpiryMinutes))
                .build();

        order = orderRepository.save(order);

        // TODO: send Kafka event for order created

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(merchantId,orderId).
                orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", "orderId : "+ orderId));

        return orderMapper.toOrderResponse(order);

    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(merchantId,orderId).
                orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", "orderId : "+ orderId));

        if(order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.PAID) {
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL", "Can not CANCEL order with STATUS : " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<PaymentResponse> getByMerchantId(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(merchantId,orderId).
                orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", "orderId : "+ orderId));

        List<Payment> payments = paymentRepository.findByOrder_Id(orderId);

        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("NO_PAYMENT_FOUND", "MerchantId : " + merchantId + " and orderId : " + orderId);
        }

//        return payments.stream()
//                .map(paymentMapper::toPaymentResponse)
//                .collect(Collectors.toList());

        return paymentMapper.toPaymentResponseList(payments);
    }
}
