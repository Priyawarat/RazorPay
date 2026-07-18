package com.priye.razorpay.payment.gateway.adapter;

import com.priye.razorpay.payment.gateway.PaymentAdapter;
import com.priye.razorpay.payment.gateway.dto.PaymentRequest;
import com.priye.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("NETBANKING")
@Slf4j
@RequiredArgsConstructor
public class NetBankingAdapter implements PaymentAdapter {

    @Override
    public PaymentResult initiate(PaymentRequest request) {
        return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
