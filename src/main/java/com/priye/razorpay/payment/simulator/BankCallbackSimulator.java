package com.priye.razorpay.payment.simulator;

import com.priye.razorpay.common.enums.PaymentStatus;
import com.priye.razorpay.payment.entity.Payment;
import com.priye.razorpay.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallbackSimulator {

    private final PaymentRepository paymentRepository;
    private final PaymentRepository paymentRepository2;

    @Scheduled(fixedDelayString = "${payment.simulator.poll-interval-ms:5000}")
    public void processCallbacks() {

        LocalDateTime globalWindow = LocalDateTime.now().minusSeconds(1);

        List<Payment> candidates = paymentRepository.findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING, globalWindow);

        log.info("Simulating payments for {} payments", candidates.size());

        if (candidates.isEmpty()) return;

        for (Payment payment: candidates) {
            simulateCallback(payment);
        }
    }

    private void simulateCallback(Payment payment) {
    }
}
