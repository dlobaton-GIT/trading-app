package com.example.tradingplatform.orders;

import com.example.tradingplatform.orders.usecases.IGetOrderUseCase;
import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
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

import javax.validation.constraints.NotNull;

import static com.example.tradingplatform.shared.miscellaneous.utils.Constants.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(V1)
@Validated
@CommonApiResponses
@Tag(name = "Orders", description = "V1.0 - 2024/04/09 - Order functionalities")
public class OrdersController {

    // Use case interfaces
    private final IGetOrderUseCase getOrderUseCase;

    @Operation(
            summary = "Get order",
            description = "Service to retrieve a specific order from trading platform",
            security = {@SecurityRequirement(name = "BearerAuth")},
            parameters = {@Parameter(name = USERNAME, in = ParameterIn.PATH, required = true)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping(value = GET_ORDER_PATH)
    public Mono<ResponseEntity<Orders>> getOrder(
            @PathVariable(value = ID) @NotNull final Integer orderId
    ) {
        return getOrderUseCase
                .execute(orderId)
                .map(res -> ResponseEntity.status(HttpStatus.OK).body(res));
    }

}
