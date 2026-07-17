package com.priye.razorpay.merchant.mapper;

import com.priye.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.priye.razorpay.merchant.dto.response.MerchantResponse;
import com.priye.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    @Mapping(target = "merchantStatus", source = "status")
    MerchantResponse toMerchantResponse(Merchant merchant);

    Merchant toMerchantEntity(MerchantSignupRequest merchantSignupRequest);
}
