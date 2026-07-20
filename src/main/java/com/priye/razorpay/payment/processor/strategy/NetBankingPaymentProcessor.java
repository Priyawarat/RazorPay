package com.priye.razorpay.payment.processor.strategy;

import com.priye.razorpay.common.util.RandomizerUtil;
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

        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails()!=null ? request.methodDetails().get("BANK").toString() : null;

        // simulation
        if(BANK_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("BANK_REJECTED",
                    "Bank rejected the transaction registration");
        }

        // we create this ourselves
        String processorRef = "NBK_PROCESSOR" + RandomizerUtil.randomBase64(16);

        // Provides by the bank to give this url to client so that they can redirect using link and pay
        String redirectRef = "https://REDIRECT_BANK.com/"+processorRef;

        return new PaymentProcessorResponse.Success(processorRef, redirectRef);
    }
}
