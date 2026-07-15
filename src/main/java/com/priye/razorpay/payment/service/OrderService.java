package com.priye.razorpay.payment.service;

import com.priye.razorpay.payment.dto.request.CreateOrderRequest;
import com.priye.razorpay.payment.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {

    OrderResponse create(UUID merchantId, CreateOrderRequest request);
}
