package com.priye.razorpay.payment.dto.request;

import com.priye.razorpay.common.enums.PaymentMethod;
import com.priye.razorpay.payment.entity.Payment;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record PaymentInitRequest(

        @NotNull(message = "Order Id is required")
        UUID orderId,

        @NotNull(message = "Payment method is required")
        PaymentMethod method,

        Map<String, Object> methodDetails

) {
}
