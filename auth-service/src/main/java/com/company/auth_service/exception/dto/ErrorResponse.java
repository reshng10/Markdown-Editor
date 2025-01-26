package com.company.auth_service.exception.dto;

import lombok.Getter;

import java.util.Map;

public class ErrorResponse {

    @Getter
    private Map<String,String> errors;

    public ErrorResponse(Map<String, String> errors) {
        this.errors = errors;
    }
}
