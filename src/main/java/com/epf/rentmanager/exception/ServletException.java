package com.epf.rentmanager.exception;

public class ServletException extends Exception {
    public ServletException(String message) {
        super(message);
        System.out.println(message);
    }
}