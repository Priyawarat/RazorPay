package com.priye.razorpay.payment.service.impl;

import com.priye.razorpay.common.enums.OrderStatus;
import com.priye.razorpay.common.exception.DuplicateResourceException;
import com.priye.razorpay.payment.dto.request.CreateOrderRequest;
import com.priye.razorpay.payment.dto.response.OrderResponse;
import com.priye.razorpay.payment.entity.OrderRecord;
import com.priye.razorpay.payment.repository.OrderRepository;
import com.priye.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

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

        // TODO: send kafka event for order created

        return new OrderResponse(
                order.getId(), order.getMerchantId(),
                order.getReceipt(), order.getAmount(),
                order.getOrderStatus(), order.getAttempts(),
                order.getNotes(), order.getExpiresAt(),
                null
        );
    }
}
