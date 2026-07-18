package com.priye.razorpay.payment.processor;

import com.priye.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);

}
