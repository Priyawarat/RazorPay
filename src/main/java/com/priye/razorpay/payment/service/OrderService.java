package com.priye.razorpay.payment.service;

import com.priye.razorpay.payment.dto.request.CreateOrderRequest;
import com.priye.razorpay.payment.dto.response.OrderResponse;
import com.priye.razorpay.payment.dto.response.PaymentResponse;
import com.priye.razorpay.payment.entity.Payment;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse create(UUID merchantId, CreateOrderRequest request);

    OrderResponse getById(UUID merchantId, UUID orderId);

    OrderResponse cancel(UUID merchantId, UUID orderId);

    List<PaymentResponse> listPayments(UUID merchantId, UUID orderId);
}
