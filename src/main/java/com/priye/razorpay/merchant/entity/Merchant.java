package com.priye.razorpay.merchant.entity;

import com.priye.razorpay.common.enums.BusinessType;
import com.priye.razorpay.common.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="merchant")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 20)
    private String contactNumber;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @Column(length = 100)
    private String businessName;

    @Column(length = 200)
    private String websiteUrl;

    @Column(nullable = false, length = 200)
    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    @Column(length = 20)
    private String gstId;

    @Column(length = 20)
    private String panId;

    @Column(length = 200)
    private String settlementBankAccount;

    @Column(length = 20)
    private String settlementBankIfsc;

    @Column(length = 200)
    private String settlementBankAccountHolderName;
}
