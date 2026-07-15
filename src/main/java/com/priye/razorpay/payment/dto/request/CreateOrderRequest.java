package com.priye.razorpay.payment.dto.request;

import com.priye.razorpay.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(

        @NotNull(message = "Amount is required")
        Money amount,

        @Size(max = 100)
        String receipt, // order-id (known to merchant) // used as idempotency to not create a new order if exits

        Map<String, Object> notes, // various type of information phone, address, order details

        LocalDateTime expiresAt
) {
}
