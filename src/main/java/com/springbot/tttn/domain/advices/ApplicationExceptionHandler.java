package com.springbot.tttn.domain.advices;

import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleInvalidArgument(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        ResponseObject result = new ResponseObject(HttpStatus.BAD_REQUEST, new Result(message, null));
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {

        String message = ex.getParameterName() + " parameter is missing";

        ResponseObject result = new ResponseObject(HttpStatus.BAD_REQUEST, new Result(message, null));
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }
}
