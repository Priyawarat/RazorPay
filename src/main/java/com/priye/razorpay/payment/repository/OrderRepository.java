package com.priye.razorpay.payment.repository;

import com.priye.razorpay.payment.entity.OrderRecord;
import com.priye.razorpay.payment.entity.Payment;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository  extends JpaRepository<OrderRecord, UUID> {
    boolean existsByMerchantIdAndReceipt(UUID merchantId, String receipt);
}
