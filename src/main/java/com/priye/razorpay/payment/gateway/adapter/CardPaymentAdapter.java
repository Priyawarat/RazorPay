package com.priye.razorpay.payment.gateway.adapter;

import com.priye.razorpay.payment.gateway.PaymentAdapter;
import com.priye.razorpay.payment.gateway.dto.PaymentRequest;
import com.priye.razorpay.payment.gateway.dto.PaymentResult;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.priye.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;

    @Override
    public PaymentResult initiate(PaymentRequest request) {

        String token = request.methodDetails().get("token").toString();

        PaymentProcessorResponse paymentProcessorResponse = vaultService.charge(request.paymentId(), token, request.amount(), request.methodDetails());

        return switch (paymentProcessorResponse) {
            case PaymentProcessorResponse.Success success ->
                    new PaymentResult.Success(success.bankReference());
            case PaymentProcessorResponse.Failure failure ->
                    new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponse.Pending pending ->
                    new PaymentResult.Pending(pending.processorReference());
        };
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
