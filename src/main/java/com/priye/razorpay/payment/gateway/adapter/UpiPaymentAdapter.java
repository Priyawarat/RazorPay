package com.priye.razorpay.payment.gateway.adapter;

import com.priye.razorpay.common.enums.PaymentMethod;
import com.priye.razorpay.payment.gateway.PaymentAdapter;
import com.priye.razorpay.payment.gateway.dto.PaymentRequest;
import com.priye.razorpay.payment.gateway.dto.PaymentResult;
import com.priye.razorpay.payment.processor.PaymentProcessorRouter;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpiPaymentAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    public PaymentResult initiate(PaymentRequest request) {

        log.info("Initiate payment with UPI, paymentId :{}",request.paymentId());

        try {
            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.nonCard(
                    request.paymentId(),
                    PaymentMethod.UPI,
                    request.amount(),
                    request.methodDetails());

            PaymentProcessorResponse paymentProcessorResponse = paymentProcessorRouter.charge(paymentProcessorRequest);

            return switch (paymentProcessorResponse) {
                case PaymentProcessorResponse.Success success ->
                        new PaymentResult.Success(success.bankReference());
                case PaymentProcessorResponse.Failure failure ->
                        new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
                case PaymentProcessorResponse.Pending pending ->
                        new PaymentResult.Pending(pending.processorReference());
            };
        } catch (Exception e) {
            log.error("Error while initiating payment with UpiPaymentAdapter, paymentId :{}, error: {}", request.paymentId(), e.getMessage());
            return new PaymentResult.Failure("UPI_INITIATION_ERROR", "Failed to initiate payment via UPI");
        }
    }

    @Override
    public PaymentResult capture(UUID paymentId) {

        return new PaymentResult.Success("UPI_REF" + paymentId);
    }
}
