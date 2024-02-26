package com.epf.rentmanager.exception;

public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
        System.out.println(message);
    }
}