package com.mercadolibre.be_java_hisp_w25_g15.exception;

import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponseDto> notFoundException(NotFoundException e){
        return new ResponseEntity<>(new MessageResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
