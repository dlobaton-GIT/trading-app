package com.example.tradingplatform.shared.config.error;


import lombok.Getter;

import java.util.Map;


/**
 * Custom exception class used in the application
 */
@Getter
public class ServiceException extends RuntimeException {

    private final String code;
    private final transient Map<String, Object> errors;

    /**
     * Constructor with a code and a message
     * @param code Code associated with the exception
     * @param message Exception message
     */
    public ServiceException(final String code, final String message) {
        this(code, message, null);
    }

    /**
     * Constructor with full parameters
     * @param code Code associated with the exception
     * @param message Exception message
     * @param errors Map with a message for each error found
     */
    public ServiceException(final String code, final String message, final Map<String, Object> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }
}
