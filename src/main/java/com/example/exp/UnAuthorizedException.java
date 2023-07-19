package com.example.exp;

public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException(String not_authorized) {
        getMessage();
    }
}
