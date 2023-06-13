package com.technoidentity.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DataFoundException.class})
    public ResponseEntity<CommonApiResponse> handleDataFoundException(DataFoundException ex) {
        CommonApiResponse errorResponse = new CommonApiResponse();
        errorResponse.setHttpStatus(HttpStatus.CONFLICT.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<CommonApiResponse>(errorResponse, HttpStatus.FOUND);
    }

    @ExceptionHandler(value = {NoDataFoundException.class})
    public ResponseEntity<CommonApiResponse> handleNoDataFoundException(NoDataFoundException ex) {
        CommonApiResponse errorResponse = new CommonApiResponse();
        errorResponse.setHttpStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<CommonApiResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<CommonApiResponse> handleApiException(ApiException ex) {
        CommonApiResponse errorResponse = new CommonApiResponse();
        errorResponse.setHttpStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<CommonApiResponse>(errorResponse, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity<CommonApiResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        CommonApiResponse errorResponse = new CommonApiResponse();
        errorResponse.setHttpStatus(HttpStatus.FORBIDDEN.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<CommonApiResponse>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<CommonApiResponse> handleBadRequestException(BadRequestException ex){
        CommonApiResponse errorResponse = new CommonApiResponse();
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<CommonApiResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonApiResponse> handleMaxUploadSizeException(MaxUploadSizeExceededException ex){
        CommonApiResponse errorResponse = new CommonApiResponse();
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<CommonApiResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}

