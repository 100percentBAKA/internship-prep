package com.adarshgs.demo.exceptions;

public class PublisherNotFound extends RuntimeException{
    public PublisherNotFound(String message) {
        super(message);
    }
}
