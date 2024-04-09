package com.example.tradingplatform.users;

import com.example.tradingplatform.orders.dtos.request.CreateOrderRequestDto;
import com.example.tradingplatform.orders.dtos.response.CreateOrderResponseDto;
import com.example.tradingplatform.orders.usecases.ICreateOrderUseCase;
import com.example.tradingplatform.shared.miscellaneous.annotations.CommonApiResponses;
import com.example.tradingplatform.shared.miscellaneous.enums.OrderType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.example.tradingplatform.shared.miscellaneous.utils.Constants.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(V1)
@Validated
@CommonApiResponses
@Tag(name = "Users", description = "V1.0 - 2024/04/09 - User functionalities")
public class UsersController {

    // Use case interfaces
    private final ICreateOrderUseCase createOrderUseCase;

    @Operation(
            summary = "Buy order",
            description = "Service to put a buy order",
            security = {@SecurityRequirement(name = "BearerAuth")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrderRequestDto.class)))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = CreateOrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PostMapping(value = BUY_ORDER_PATH)
    public Mono<ResponseEntity<CreateOrderResponseDto>> buySecurity(
            @Parameter(hidden = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION) final String authorization,
            @RequestBody final CreateOrderRequestDto entryData
    ) {

        return createOrderUseCase
                .execute(authorization, entryData, OrderType.BUY)
                .map(res -> ResponseEntity.status(HttpStatus.CREATED).body(res))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(
            summary = "Sell order",
            description = "Service to put a sell order",
            security = {@SecurityRequirement(name = "BearerAuth")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrderRequestDto.class)))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = CreateOrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PostMapping(value = SELL_ORDER_PATH)
    public Mono<ResponseEntity<CreateOrderResponseDto>> sellSecurity(
            @Parameter(hidden = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION) final String authorization,
            @RequestBody final CreateOrderRequestDto entryData
    ) {

        return createOrderUseCase
                .execute(authorization, entryData, OrderType.SELL)
                .map(res -> ResponseEntity.status(HttpStatus.CREATED).body(res))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

}
