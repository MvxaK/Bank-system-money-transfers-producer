package org.cook.bank_system.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(annotations = RestController.class)
public class RestApiExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException e){
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), "Can not perform this action", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exceptionDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException e){
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), "Entity not found", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalError(Exception e){
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), "Internal Server error", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDto);
    }


}
