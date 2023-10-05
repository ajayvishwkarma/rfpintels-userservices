package com.rfpintels.userservices.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rfpintels.userservices.exception.InvalidTokenRequestException;
import com.rfpintels.userservices.exception.ResourceAlreadyInUseException;
import com.rfpintels.userservices.exception.ResourceNotFoundException;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidTokenRequestException.class)
	public ResponseEntity<Object> handleConflict1(InvalidTokenRequestException invalidTokenRequestException) {
		return new ResponseEntity(invalidTokenRequestException.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleConflict2(ResourceNotFoundException resourceNotFoundException ) {
		return new ResponseEntity(resourceNotFoundException.getFieldName(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(ResourceAlreadyInUseException.class)
	public ResponseEntity<Object> handleConflict3(ResourceAlreadyInUseException resourceNotFoundException ) {
		return new ResponseEntity(resourceNotFoundException.getFieldName(), HttpStatus.CONFLICT);
	}
	
}