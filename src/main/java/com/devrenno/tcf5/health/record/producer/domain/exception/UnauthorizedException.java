package com.devrenno.tcf5.health.record.producer.domain.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

}