package com.mvgore.garageapi.exception;

public class GarageException extends RuntimeException {
    public GarageException() {
        super();
    }

    public GarageException(String message) {
        super(message);
    }
}
