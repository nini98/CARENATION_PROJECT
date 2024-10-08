package com.carenation.carenation_project.common.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.carenation.carenation_project.common.response.Response;
import com.carenation.carenation_project.common.response.ResultCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarenationException.class)
    public final ResponseEntity<Response<?>> handleCarenationException(CarenationException csOpenApiException) {
        log.error("[handleCarenationException] : {}", csOpenApiException.getMessage());
        Response<?> response = Response.fail(csOpenApiException.getResultCode());
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(csOpenApiException.getResultCode().getCode()));
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Response<?>> handleValidationExceptions(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        log.error("[handleValidationExceptions] : {}", errorMessage);
        Response<?> response = Response.fail(ResultCode.VALIDATION_ERROR, errorMessage.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Response<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("[handleDataIntegrityViolationException] : ", e);
        Throwable rootCause = ExceptionUtils.getRootCause(e);

        if (rootCause instanceof java.sql.SQLIntegrityConstraintViolationException) {
            String message = rootCause.getMessage();

            if (message != null && message.contains("Duplicate entry")) {
                Response<?> response = Response.fail(ResultCode.DUPLICATE_DATA);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            Response<?> response = Response.fail(ResultCode.DATA_INTEGRITY_VIOLATION);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Response<?> response = Response.fail(ResultCode.DATA_INTEGRITY_VIOLATION);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response<?>> handleAllException(Exception e) {
        log.error("[handleAllException] : ", e);
        Response<?> response = Response.fail(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
