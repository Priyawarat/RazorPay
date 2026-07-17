package com.priye.razorpay.payment.controller;

import com.priye.razorpay.payment.dto.request.CreateOrderRequest;
import com.priye.razorpay.payment.dto.response.OrderResponse;
import com.priye.razorpay.payment.entity.Payment;
import com.priye.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    UUID merchantId = UUID.fromString("f99e0705-a3d9-4f17-acab-b54d28e2a49a"); // TODO: replace with merchant context

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(merchantId, request));
    }
}
