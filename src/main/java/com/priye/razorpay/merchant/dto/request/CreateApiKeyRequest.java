package com.priye.razorpay.merchant.dto.request;

import com.priye.razorpay.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}
