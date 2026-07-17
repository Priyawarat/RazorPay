package com.priye.razorpay.merchant.service;

import com.priye.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.priye.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.priye.razorpay.merchant.dto.response.ApiKeyResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {

    ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request);

    List<ApiKeyResponse> list(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotateKey(UUID merchantId, UUID keyId);
}
