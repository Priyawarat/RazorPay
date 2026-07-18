package com.priye.razorpay.payment.processor.strategy;

import com.priye.razorpay.payment.processor.PaymentProcessor;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NetBankingPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        // call third party net banking payment gateway API to process the payment
        return null;
    }
}
