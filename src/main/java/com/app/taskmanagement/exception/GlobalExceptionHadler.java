package com.app.taskmanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHadler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErorResponse> accessDeniedExceptionHandler  (AccessDeniedException e, WebRequest request) {
        ErorResponse erorResponse =  new ErorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(erorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErorResponse>  duplicateRequestExceptionHandler  (DuplicateResourceException e, WebRequest request) {
        ErorResponse erorResponse =  new ErorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(erorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErorResponse>  invalidOperationExceptionHandler  (InvalidOperationException e, WebRequest request) {
        ErorResponse erorResponse =  new ErorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(erorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErorResponse>  ResourceNotFoundExceptionHandler  (ResourceNotFoundException e, WebRequest request) {
        ErorResponse erorResponse = new ErorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(erorResponse, HttpStatus.NOT_FOUND);
    }
}
