package com.priye.razorpay.merchant.service;

import com.priye.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.priye.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);
}
