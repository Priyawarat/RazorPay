package com.priye.razorpay.merchant.mapper;

import com.priye.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.priye.razorpay.merchant.dto.response.ApiKeyResponse;
import com.priye.razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    @Mapping(target = "keySecret", source = "keySecretHash")
    ApiKeyCreateResponse toApiKeyResponse(ApiKey apiKey);

    List<ApiKeyResponse> toApiKeyResponseList(List<ApiKey> apiKeys);
}
