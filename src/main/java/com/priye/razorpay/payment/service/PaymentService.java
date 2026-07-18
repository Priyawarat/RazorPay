package com.priye.razorpay.payment.service;

import com.priye.razorpay.payment.dto.request.PaymentInitRequest;
import com.priye.razorpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);
}
