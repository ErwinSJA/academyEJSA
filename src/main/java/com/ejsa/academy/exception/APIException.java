package com.ejsa.academy.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException{

	private static final long serialVersionUID = -235359793116264421L;
	
	private final String message;
	private final HttpStatus httpStatus;
	
	public APIException(String message, HttpStatus httpStatus) {
		super(message);
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
