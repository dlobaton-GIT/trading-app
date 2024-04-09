package com.example.tradingplatform.shared.config.error;


import com.example.tradingplatform.shared.miscellaneous.dtos.ServiceErrorDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Class to handle the exceptions thrown in the application.
 * Each exception handled will be mapped to an HTTP response
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Method to handle exceptions to type ServletRequestBindingException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return HTTP status and the response body containing info about the error
     */
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        log.error("Error trying to bind resources to {}. Error message: {}", request.getContextPath(), ex.getMessage());
        return buildResponseEntity(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Collections.singletonList(ex.getMessage()));
    }

    /**
     * Method to handle exceptions to type HttpMessageNotReadableException.
     * This exception will be thrown when Spring cannot convert the data sent in a request
     * into the object needed. In that case, the service will return 400 - Bad Request
     *
     * @param ex      exception produced containing info about the problem
     * @param headers headers involved in the request
     * @param status  response status
     * @param request request sent
     * @return HTTP status and the response body containing info about the error
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        log.error("Error trying to transform the data sent. Path: {}. Error message: {}",
                request.getContextPath(), ex.getMessage());
        return buildResponseEntity(
                HttpStatus.BAD_REQUEST,
                "The request structure is invalid",
                Collections.singletonList(ex.getMessage())
        );
    }

    /**
     * Method to handle exceptions to type MissingServletRequestParameterException.
     * This exception will be thrown when a requested parameter is not present in a request.
     * In that case, the service will return a 400 - Bad Request
     *
     * @param ex      exception produced containing info about the problem
     * @param headers headers involved in the request
     * @param status  response status
     * @param request request sent
     * @return HTTP status and the response body containing info about the error
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        log.error("Error on {}. Some required parameters are missing. Error message: {}",
                request.getContextPath(), ex.getMessage());
        return buildResponseEntity(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Collections.singletonList(ex.getMessage())
        );
    }

    /**
     * Method to handle exceptions to type MethodArgumentNotValidException.
     * This exception will be thrown when @Valid is present and the validation fails.
     * In that case, the service will return a 400 - Bad Request
     *
     * @param ex      exception produced containing info about the problem
     * @param headers headers involved in the request
     * @param status  response status
     * @param request request sent
     * @return HTTP status and the response body containing info about the error
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        final List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.error("Error validating input data in {}. Errors: {}", request.getContextPath(), new Gson().toJson(errors));
        return buildResponseEntity(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors
        );
    }

    /**
     * Method to handle exceptions to type ServiceException.
     * This type of exception is generated inside the application
     *
     * @param ex exception produced containing info about the problem
     * @return entity with status defined by the exception and the error body
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(final ServiceException ex) {
        final HttpStatus httpStatus = Arrays.stream(HttpStatus.values())
                .filter(status -> String.valueOf(status.value()).equals(ex.getCode()))
                .findAny()
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        log.error("Something went wrong. Error: {}", new Gson().toJson(ex.getErrors()));
        return buildResponseEntity(httpStatus, ex.getMessage(), ex.getErrors());
    }

    /**
     * Method to handle exceptions to type ConstraintViolationException.
     * This exception will be thrown when the client send an improper value for a param,
     * annotated with @Valid, which does not satisfy the validation
     *
     * @param ex exception produced containing info about the problem
     * @return HTTP status and the response body containing info about the error
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex) {
        final List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toUnmodifiableList());

        log.error("Error performing validations to input data. Errors: {}", new Gson().toJson(errors));
        return buildResponseEntity(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors
        );
    }

    /**
     * Method to handle exceptions to type Exception.
     * This type of exception is thrown when something failed and was not predicted
     *
     * @param ex exception produced containing info about the problem
     * @return entity with status defined by the exception and the error body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception ex) {
        if (ex instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) ex);
        } else {
            log.error("Something went wrong. Error: {}", ex.getMessage());
            return buildResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    Collections.singletonList(ex.getMessage()));
        }
    }

    /**
     * Method to handle exceptions to type Exception.
     * This type of exception is thrown when something failed and was not predicted
     *
     * @param ex exception produced containing info about the problem
     * @return entity with status defined by the exception and the error body
     */
    @ExceptionHandler(java.util.concurrent.TimeoutException.class)
    public ResponseEntity<Object> handleTimeoutException(final java.util.concurrent.TimeoutException ex) {
        log.error("External service call timeout: {}", ex.getMessage());

        return buildResponseEntity(
                HttpStatus.CONFLICT,
                "External service call timeout",
                Map.of("Error message", HttpStatus.REQUEST_TIMEOUT.getReasonPhrase()));

    }

    /**
     * Method to handle exceptions to type WebClientResponseException.
     * This type of exception is thrown after trying to perform an HTTP request using WebClient class
     * and the service return 4XX or 5XX.
     *
     * @param ex exception produced containing info about the problem
     * @return HTTP status and the response body containing info about the error
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> handleException(final WebClientResponseException ex) {
        log.error("Error calling an external resource. Message: {}", ex.getMessage());
        return buildResponseEntity(
                ex.getStatusCode(),
                ex.getStatusText(),
                Collections.singletonList(ex.getMessage()));
    }

    /**
     * Builds the application response
     *
     * @param status  status for the response
     * @param message error message
     * @param errors  error list
     * @return application response
     */
    private ResponseEntity<Object> buildResponseEntity(
            final HttpStatus status,
            final String message,
            final List<String> errors
    ) {
        final ServiceErrorDto errorResponse = new ServiceErrorDto(status.value(), message, errors);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Builds the application response
     *
     * @param status
     * @param message
     * @param errors
     * @param errorDetails
     * @return application response
     */
    private ResponseEntity<Object> buildResponseEntity(
            final HttpStatus status,
            final String message,
            final List<String> errors,
            final Map<String, Object> errorDetails
    ) {
        final ServiceErrorDto errorResponse = new ServiceErrorDto(status.value(), message, errors, errorDetails);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Builds the application response
     *
     * @param status       status for the response
     * @param message      error message
     * @param errorDetails errors happened
     * @return application response
     */
    private ResponseEntity<Object> buildResponseEntity(
            final HttpStatus status,
            final String message,
            final Map<String, Object> errorDetails
    ) {
        final ServiceErrorDto errorResponse = new ServiceErrorDto(status.value(), message, errorDetails);
        return new ResponseEntity<>(errorResponse, status);
    }
}
