package com.nguyenminh.microservices.zwallet.exception;

public class UserValidateException extends RuntimeException{
    public UserValidateException(String message) {
        super(message);
    }
}
