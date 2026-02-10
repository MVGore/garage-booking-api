package com.mvgore.garageapi.exception;

import com.mvgore.garageapi.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ===============================
    // 403 - FORBIDDEN
    // ===============================
    @ExceptionHandler({
            AccessDeniedException.class,
            ForbiddenOperationException.class
    })
    public ResponseEntity<ErrorResponse> handleForbidden(RuntimeException ex) {

        logger.warn("Forbidden operation: {}", ex.getMessage());

        ErrorResponse error =
                new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // ===============================
    // 409 - CONFLICT
    // ===============================
    @ExceptionHandler(BookingConflictException.class)
    public ResponseEntity<ErrorResponse> handleBookingConflict(
            BookingConflictException ex) {

        logger.error("Booking conflict occurred: {}", ex.getMessage());

        ErrorResponse error =
                new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // ===============================
    // 400 - BAD REQUEST
    // ===============================
    @ExceptionHandler(GarageException.class)
    public ResponseEntity<ErrorResponse> handleGarageException(GarageException ex) {

        logger.error("Garage exception occurred: {}", ex.getMessage());

        ErrorResponse error =
                new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ===============================
    // 404 - NOT FOUND
    // ===============================
    @ExceptionHandler({
            BookingNotFoundException.class,
            GarageNotFoundException.class,
            ServiceNotFoundException.class,
            CustomerNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {

        logger.warn("Resource not found: {}", ex.getMessage());

        ErrorResponse error =
                new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ===============================
    // 500 - INTERNAL SERVER ERROR
    // ===============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        logger.error("Unhandled exception occurred", ex);

        ErrorResponse error =
                new ErrorResponse(
                        "Internal server error",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
