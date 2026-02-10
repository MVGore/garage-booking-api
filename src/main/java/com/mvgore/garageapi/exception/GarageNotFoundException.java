package com.mvgore.garageapi.exception;

public class GarageNotFoundException extends RuntimeException {
    public GarageNotFoundException(String message) {
        super(message);
    }
}
