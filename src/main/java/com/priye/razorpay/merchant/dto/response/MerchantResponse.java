package com.priye.razorpay.merchant.dto.response;

import com.priye.razorpay.common.enums.BusinessType;
import com.priye.razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {
}
