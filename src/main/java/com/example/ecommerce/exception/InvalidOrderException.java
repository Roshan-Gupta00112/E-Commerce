package com.example.ecommerce.exception;

public class InvalidOrderException extends Exception{

    public InvalidOrderException(String message){
        super(message);
    }
}
