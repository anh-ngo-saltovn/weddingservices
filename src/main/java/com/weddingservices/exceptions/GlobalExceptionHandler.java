package com.weddingservices.exceptions;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(WebRequest request, Exception ex){
		logger.warn(ex.getMessage());
		IllegalArgumentException existException = (IllegalArgumentException) ex;
		JsonMessage errorResource = new JsonMessage(HttpStatus.BAD_REQUEST.value(),existException.getMessage());
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(ex, errorResource, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	
	@ExceptionHandler(CustomizeException.class)
	public ResponseEntity<Object> handleObjectNotExistException(WebRequest request, RuntimeException ex){
		logger.warn(ex.getMessage());
		CustomizeException existException = (CustomizeException) ex;
		JsonMessage errorResource = (JsonMessage)existException.getObj();
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(ex, errorResource, headers,HttpStatus.valueOf(errorResource.getState()), request);
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
		logger.warn(e.getMessage());
        InvalidRequestException ire = (InvalidRequestException) e;
        List<FieldErrorResource> fieldErrorResources = new ArrayList<FieldErrorResource>();

        List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            FieldErrorResource fieldErrorResource = new FieldErrorResource();
            fieldErrorResource.setResource(fieldError.getObjectName());
            fieldErrorResource.setField(fieldError.getField());
            fieldErrorResource.setCode(fieldError.getCode());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());
            fieldErrorResources.add(fieldErrorResource);
        }

        ErrorResource error = new ErrorResource(HttpStatus.UNPROCESSABLE_ENTITY.toString(), ire.getMessage());
        error.setFieldErrors(fieldErrorResources);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
	
	@ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<Object> defaultErrorHandler(HttpServletRequest request, Exception e) {
		logger.warn(e.getMessage());
		CustomizeException existException = (CustomizeException) e;
		JsonMessage errorResource = (JsonMessage)existException.getObj();
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(e, errorResource, headers,HttpStatus.valueOf(errorResource.getState()), (WebRequest) request);
    }
	
}
