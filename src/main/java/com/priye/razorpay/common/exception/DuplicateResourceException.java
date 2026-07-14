package com.priye.razorpay.common.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException{

    private final String errrorCode;

    public DuplicateResourceException(String errrorCode, String message) {
        super(message);
        this.errrorCode = errrorCode;
    }
}
