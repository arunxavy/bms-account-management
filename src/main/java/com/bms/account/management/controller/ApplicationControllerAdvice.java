package com.bms.account.management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.bms.account.management.domain.GenericResponse;
import com.bms.account.management.exception.InvalidInputException;
import com.bms.account.management.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ApplicationControllerAdvice {

	private static final String MESSAGE = "message";

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public GenericResponse resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		GenericResponse response = new GenericResponse();
		Map<String, Object> responseMessages = new HashMap<>();
		responseMessages.put(MESSAGE, ex.getMessage());
		response.setResponse(responseMessages);

		return response;
	}

	@ExceptionHandler(value = { InvalidInputException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public GenericResponse resourceNotFoundException(InvalidInputException ex, WebRequest request) {
		GenericResponse response = new GenericResponse();
		Map<String, Object> responseMessages = new HashMap<>();
		responseMessages.put(MESSAGE, ex.getMessage());
		response.setResponse(responseMessages);

		return response;
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public GenericResponse resourceNotFoundException(MethodArgumentNotValidException ex, WebRequest request) {
		GenericResponse response = new GenericResponse();
		Map<String, Object> responseMessages = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			responseMessages.put(fieldName, errorMessage);
		});
		response.setResponse(responseMessages);

		return response;
	}

	@ExceptionHandler(value = { RuntimeException.class })
	@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
	public GenericResponse allRuntimeExceptions(RuntimeException ex, WebRequest request) {
		log.error("Error : ", ex);
		GenericResponse response = new GenericResponse();
		Map<String, Object> responseMessages = new HashMap<>();
		responseMessages.put(MESSAGE, "An unexpected error occured!, please try again");
		responseMessages.put("debug", ex.getMessage());
		response.setResponse(responseMessages);

		return response;
	}
}
