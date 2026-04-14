package com.devrenno.tcf5.health.record.producer.domain.exception;

import com.devrenno.tcf5.health.record.producer.domain.model.ErrorResponse;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .build(
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Request")
                .message(ex.getMessage())
                .build(
                );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        ErrorResponse error;
        String message;

        String headerName = ex.getHeaderName();

        if (headerName.equalsIgnoreCase("authorization")) {
            message = "Requisição não autorizada: Authorization header invalido ou ausente";
            error = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .error("Unauthorized")
                    .message(message)
                    .build();
        } else {
            message = String.format("Header requerido ausente: %s", headerName);
            error = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error("Invalid Request")
                    .message(message)
                    .build();
        }
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message(ex.getMessage())
                .build(
                );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Request")
                .message(ex.getMessage())
                .build(
                );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .build(
                );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler (Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .build(
                );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
