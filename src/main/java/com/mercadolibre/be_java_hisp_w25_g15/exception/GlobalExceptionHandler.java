package com.mercadolibre.be_java_hisp_w25_g15.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponseDto> notFoundException(NotFoundException e){
        return new ResponseEntity<>(new MessageResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    //Excepciones de validaciones
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = "Error de validación: " + ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(new MessageResponseDto(errorMessage), HttpStatus.BAD_REQUEST);
    }

    //Excepciones de formato de datos, actualmente se lanza cuando se envia una fecha invalida
    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<Object> handleMismatchedInputException(MismatchedInputException ex) {
        String errorMessage = "Error en el formato de los datos enviados: " + ex.getMessage();
        return new ResponseEntity<>(new MessageResponseDto(errorMessage), HttpStatus.BAD_REQUEST);
    }
}
