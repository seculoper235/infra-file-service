package com.example.infrafileservice.web.exception.handler;

import com.example.infrafileservice.web.exception.model.EntityNotFoundException;
import com.example.infrafileservice.web.exception.model.ExceptionStatus;
import com.example.infrafileservice.web.exception.model.PutObjectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class RouteExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {

        ExceptionResponse response = new ExceptionResponse(
                new Date().toString(),
                ExceptionStatus.FS001,
                e.getMessage());

        log.error(response.getDetail());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(PutObjectException.class)
    public ResponseEntity<ExceptionResponse> handlePutObjectException(PutObjectException e) {

        ExceptionResponse response = new ExceptionResponse(
                new Date().toString(),
                ExceptionStatus.FS002,
                e.getMessage());

        log.error(response.getDetail());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ExceptionResponse> handleS3Exception(S3Exception e) {

        ExceptionResponse response = new ExceptionResponse(
                new Date().toString(),
                ExceptionStatus.FS003,
                e.getMessage());

        log.error(response.getDetail());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
