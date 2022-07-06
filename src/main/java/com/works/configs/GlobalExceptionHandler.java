package com.works.configs;

import com.works.utils.REnum;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class, TransactionSystemException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class} )
    protected ResponseEntity myFnc( Exception ex ) {
        if ( ex instanceof IllegalStateException ) {
            IllegalStateException il = (IllegalStateException) ex;
            System.out.println( il.getMessage() );
        }if(ex instanceof ConstraintViolationException){

            Map<REnum, Object> hm = new LinkedHashMap<>();
            List<String> details = ((ConstraintViolationException) ex).getConstraintViolations()
                    .parallelStream()
                    .map(e -> e.getMessage())
                    .collect(Collectors.toList());
             hm.put(REnum.status,false);
             hm.put(REnum.message,details);

            return new ResponseEntity<>(hm, BAD_REQUEST);
        }
        if(ex instanceof DataIntegrityViolationException){
            Map<REnum, Object> hm = new LinkedHashMap<>();
           String exMessage=ex.getMessage();
            hm.put(REnum.status,false);
            hm.put(REnum.message,exMessage);
            return new ResponseEntity<>(hm, BAD_REQUEST);
        }
        return null;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("hata çalıştı handleMethodArgumentNotValid " + ex.getMessage());
        Map<REnum, Object> hm = new LinkedHashMap<>();
        List<FieldError> errors = ex.getFieldErrors();
        List<Map<String ,String >> lss = new ArrayList<>();
        for ( FieldError item : errors ) {
            Map<String , String > hmx = new HashMap<>();
            String fieldName = item.getField();
            String message = item.getDefaultMessage();
            hmx.put(fieldName, message);
            System.out.println( fieldName + " " + message );
            lss.add(hmx);
        }
        hm.put(REnum.status, false);
        hm.put(REnum.error, lss);
        return new ResponseEntity<>(hm, BAD_REQUEST);
    }
}

