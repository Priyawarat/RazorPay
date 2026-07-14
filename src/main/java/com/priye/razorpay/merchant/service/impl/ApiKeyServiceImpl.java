package com.priye.razorpay.merchant.service.impl;

import com.priye.razorpay.common.exception.ResourceNotFoundException;
import com.priye.razorpay.common.util.RandomizerUtil;
import com.priye.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.priye.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.priye.razorpay.merchant.entity.ApiKey;
import com.priye.razorpay.merchant.entity.Merchant;
import com.priye.razorpay.merchant.repository.ApiKeyRepository;
import com.priye.razorpay.merchant.repository.MerchantRepository;
import com.priye.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_"+request.environment().name().toLowerCase()+"_"+ "big_random_string";
        String rawSecret = "big random secret"; // TODO: replcae with RandomizerUtil.randomBase64(40) or similar

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) // TODO : encode with BCryptPasswordEncoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());
    }

}
