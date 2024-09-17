package com.ejsa.academy.presentation.advise;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ejsa.academy.exception.APIException;
import com.ejsa.academy.presentation.util.ResponseMessage;

@RestControllerAdvice
public class APIAdvise {
	@ExceptionHandler(value = APIException.class)
    public ResponseEntity<ResponseMessage<Void>> getException(APIException exception) {

        ResponseMessage<Void> msg = ResponseMessage.<Void>builder()
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(msg, exception.getHttpStatus());
    }
}
