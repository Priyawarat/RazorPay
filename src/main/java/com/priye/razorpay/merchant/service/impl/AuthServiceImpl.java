package com.priye.razorpay.merchant.service.impl;

import com.priye.razorpay.common.enums.MerchantStatus;
import com.priye.razorpay.common.enums.UserRole;
import com.priye.razorpay.common.exception.DuplicateResourceException;
import com.priye.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.priye.razorpay.merchant.dto.response.MerchantResponse;
import com.priye.razorpay.merchant.entity.AppUser;
import com.priye.razorpay.merchant.entity.Merchant;
import com.priye.razorpay.merchant.repository.AppUserRepository;
import com.priye.razorpay.merchant.repository.MerchantRepository;
import com.priye.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","Merchant with email already exists: " + request.email());
        }

        // Merchant merchant = new Merchant(); -> Old way to create, we will use Builder.
        Merchant merchant = Merchant.builder()
                .businessType(request.businessType())
                .businessName(request.businessName())
                .name(request.name())
                .email(request.email())
                .status(MerchantStatus.PENDING_KYC)
                .build();
        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password()) // TODO: encrypt using Bcrypt
                .userRole(UserRole.OWNER)
                .build();
        appUser = appUserRepository.save(appUser);

        return new MerchantResponse(merchant.getId(), merchant.getName(),
                merchant.getEmail(), merchant.getBusinessName(),
                merchant.getBusinessType(), merchant.getStatus());
    }

}
