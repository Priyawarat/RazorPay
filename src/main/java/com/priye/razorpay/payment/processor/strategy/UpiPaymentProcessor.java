package com.priye.razorpay.payment.processor.strategy;

import com.priye.razorpay.common.util.RandomizerUtil;
import com.priye.razorpay.payment.processor.PaymentProcessor;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.priye.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        final String VPA_CODE_FAIL = "fail@okaxis";

        String bankCode = request.methodDetails()!=null ? request.methodDetails().get("vpa").toString() : null;

        // simulation
        if(VPA_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("UPI_REJECTED",
                    "Bank rejected the transaction registration");
        }

        // we create this ourselves
        String processorRef = "UPI_PROCESSOR" + RandomizerUtil.randomBase64(16);

        String redirectRef = "BANK_REF" + RandomizerUtil.randomBase64(16);

        // call third party card payment gateway API to process the payment
        return new PaymentProcessorResponse.Success(processorRef, redirectRef);
    }
}
