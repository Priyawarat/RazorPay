package com.priye.razorpay.vault.service;

import com.priye.razorpay.common.entity.Money;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.priye.razorpay.vault.dto.request.TokenizeRequest;
import com.priye.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
