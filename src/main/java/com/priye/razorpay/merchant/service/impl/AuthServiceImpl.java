package com.priye.razorpay.merchant.service.impl;

import com.priye.razorpay.common.enums.MerchantStatus;
import com.priye.razorpay.common.enums.UserRole;
import com.priye.razorpay.common.exception.DuplicateResourceException;
import com.priye.razorpay.common.exception.ResourceNotFoundException;
import com.priye.razorpay.merchant.dto.request.LoginRequest;
import com.priye.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.priye.razorpay.merchant.dto.response.LoginResponse;
import com.priye.razorpay.merchant.dto.response.MerchantResponse;
import com.priye.razorpay.merchant.entity.AppUser;
import com.priye.razorpay.merchant.entity.Merchant;
import com.priye.razorpay.merchant.mapper.MerchantMapper;
import com.priye.razorpay.merchant.repository.AppUserRepository;
import com.priye.razorpay.merchant.repository.MerchantRepository;
import com.priye.razorpay.merchant.security.JwtUtil;
import com.priye.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","Merchant with email already exists: " + request.email());
        }

        // Merchant merchant = new Merchant(); -> Old way to create, we will use Builder.
        Merchant merchant = merchantMapper.toMerchantEntity(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);
        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(UserRole.OWNER)
                .build();
        appUser = appUserRepository.save(appUser);

        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND","User not found with email: " + request.email()));

        String token = jwtUtil.generateAccessToken(request.email(), appUser.getMerchant().getId(), appUser.getRole().toString());

        return new LoginResponse(token);
    }

}
