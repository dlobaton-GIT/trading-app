package com.example.tradingplatform.security;

import com.example.tradingplatform.security.usecases.IGetSecurityUseCase;
import com.example.tradingplatform.shared.externalservices.h2db.model.Security;
import com.example.tradingplatform.shared.miscellaneous.annotations.CommonApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;

import static com.example.tradingplatform.shared.miscellaneous.utils.Constants.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(V1)
@Validated
@CommonApiResponses
@Tag(name = "Security", description = "V1.0 - 2024/04/09 - Security functionalities")
public class SecurityController {

    // Use case interfaces
    private final IGetSecurityUseCase getSecurityUseCase;

    @Operation(
            summary = "Get security",
            description = "Service to retrieve a specific security from trading platform",
            security = {@SecurityRequirement(name = "BearerAuth")},
            parameters = {@Parameter(name = USERNAME, in = ParameterIn.PATH, required = true)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping(value = GET_SECURITY_PATH)
    public Mono<ResponseEntity<Security>> getSecurity(
            @PathVariable(value = SECURITY_NAME) @NotBlank final String securityName
    ) {
        return getSecurityUseCase
                .execute(securityName)
                .map(res -> ResponseEntity.status(HttpStatus.OK).body(res));
    }

}
