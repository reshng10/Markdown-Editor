package com.company.auth_service.exception.handler;

import com.company.auth_service.exception.ExceptionResponse;
import com.company.auth_service.exception.dto.ErrorResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException(MessagingException messagingException){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messagingException.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleMessagingException(UsernameNotFoundException usernameNotFoundException){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(usernameNotFoundException.getMessage());
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException methodArgumentNotValidException){
//        Map<String, String> errors = new HashMap<>();
//        methodArgumentNotValidException.getBindingResult().getAllErrors()
//                .forEach(objectError -> {
//                    var fieldName = ((FieldError) objectError).getField();
//                    var errorMessage = objectError.getDefaultMessage();
//                    errors.put(fieldName,errorMessage);
//
//                });
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse(errors));
//    }

}
