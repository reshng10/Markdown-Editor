package com.example.document_service.exception;

public class UserNotAllowedException extends Exception {
    private final String exceptionMsg;

    public UserNotAllowedException(String s) {
        this.exceptionMsg =s;
    }
}
