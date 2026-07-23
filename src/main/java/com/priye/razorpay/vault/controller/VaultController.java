package com.priye.razorpay.vault.controller;

import com.priye.razorpay.vault.dto.request.TokenizeRequest;
import com.priye.razorpay.vault.dto.response.TokenizeResponse;
import com.priye.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {

    private final VaultService vaultService;

    UUID merchantId = UUID.fromString("f99e0705-a3d9-4f17-acab-b54d28e2a49a"); // TODO: replace with merchant context

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@RequestBody TokenizeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vaultService.tokenize(request,merchantId));
    }

}
