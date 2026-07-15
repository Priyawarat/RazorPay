package com.priye.razorpay.merchant.controller;

import com.priye.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.priye.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.priye.razorpay.merchant.dto.response.ApiKeyResponse;
import com.priye.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService  apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@PathVariable UUID merchantId, @Valid @RequestBody CreateApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.create(merchantId,request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> getAll(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(apiKeyService.list(merchantId));
    }

    @DeleteMapping("/keyId")
    public ResponseEntity<Void> delete(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        apiKeyService.revoke(merchantId, keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotate(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        return ResponseEntity.ok(apiKeyService.rotateKey(merchantId, keyId));
    }
    }
