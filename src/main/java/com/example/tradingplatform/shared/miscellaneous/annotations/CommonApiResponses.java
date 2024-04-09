package com.example.tradingplatform.shared.miscellaneous.annotations;


import com.example.tradingplatform.shared.miscellaneous.dtos.ServiceErrorDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation encapsulating responses that are common to several services
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ServiceErrorDto.class))}),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ServiceErrorDto.class))}),
        @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ServiceErrorDto.class))})
})
public @interface CommonApiResponses { }
