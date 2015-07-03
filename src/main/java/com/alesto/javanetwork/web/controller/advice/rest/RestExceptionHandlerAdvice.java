package com.alesto.javanetwork.web.controller.advice.rest;

import com.alesto.javanetwork.exception.rest.ResourceNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.alesto.javanetwork.web.controller.rest")
public class RestExceptionHandlerAdvice {

	/*
	 * ======================================================================
	 * ----- Constants
	 * ======================================================================
	 */

    private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error+json");
	
	/*
	 * ======================================================================
	 * ----- Exception handling methods
	 * ======================================================================
	 */

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<VndErrors> handleResourceNotFoundException(ResourceNotFoundException e) {
        return error(e, HttpStatus.NOT_FOUND, e.getLogref());
    }
	
	/*
	 * ======================================================================
	 * ----- Helper methods
	 * ======================================================================
	 */

    private <E extends Exception> ResponseEntity<VndErrors> error(E e, HttpStatus httpStatus, String logref) {
        if (logref == null) {
            logref = "";
        }

        String message = e.getMessage();
        if (message == null) {
            message = e.getClass().getSimpleName();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(this.vndErrorMediaType);

        return new ResponseEntity<>(new VndErrors(logref, message), httpHeaders, httpStatus);
    }

}
