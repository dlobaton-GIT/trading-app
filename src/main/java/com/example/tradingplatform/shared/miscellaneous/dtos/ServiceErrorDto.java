package com.example.tradingplatform.shared.miscellaneous.dtos;


import com.example.tradingplatform.shared.miscellaneous.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * Class to map exceptions thrown inside the application.
 * This class is used as a response model in case something went wrong
 */
@Getter
@Schema(name = "ErrorResponse")
public class ServiceErrorDto {

    // Node names in the JSON
    private static final String ERRORS_NODE = "errors";
    private static final String ERROR_DETAILS_NODE = "errorDetails";
    private static final String TIMESTAMP_NODE = "timestamp";

    @JsonProperty(Constants.CODE)
    private final int code;

    @JsonProperty(Constants.MESSAGE)
    private final String message;

    @JsonProperty(ERRORS_NODE)
    private final List<String> errors;

    @JsonProperty(ERROR_DETAILS_NODE)
    private final Map<String, Object> errorDetails;

    @JsonProperty(TIMESTAMP_NODE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.TIMESTAMP_FORMAT)
    private final LocalDateTime timestamp;

    /**
     * Constructor with only the code and exception message
     * @param code Code associated with the exception
     * @param message Exception message
     */
    public ServiceErrorDto(final int code, final String message) {
        this(code, message, null, null);
    }

    /**
     * Constructor with code, exception message and a list of errors
     * @param code Code associated with the exception
     * @param message Exception message
     * @param errors Errors that have produced the exception to be thrown
     */
    public ServiceErrorDto(final int code, final String message, final List<String> errors) {
        this(code, message, errors, null);
    }

    /**
     * Constructor with code, exception message and a map with errors
     * @param code Code associated with the exception
     * @param message Exception message
     * @param errorDetails Map containing details about each error
     *                      that has made the exception to be thrown
     */
    public ServiceErrorDto(final int code, final String message, final Map<String, Object> errorDetails) {
        this(code, message, null, errorDetails);
    }

    /**
     * Constructor with all parameters
     * @param code Code associated with the exception
     * @param message Exception message
     * @param errors Errors that have produced the exception to be thrown
     * @param errorDetails Map containing details about each error
     *                      that has made the exception to be thrown
     */
    public ServiceErrorDto(final int code, final String message, final List<String> errors, final Map<String, Object> errorDetails) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.errorDetails = errorDetails;
        this.timestamp = LocalDateTime.now();
    }
}
