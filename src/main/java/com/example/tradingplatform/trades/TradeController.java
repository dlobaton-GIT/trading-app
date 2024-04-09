package com.example.tradingplatform.trades;

import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import com.example.tradingplatform.shared.miscellaneous.annotations.CommonApiResponses;
import com.example.tradingplatform.trades.usecases.IGetTradeByIdUseCase;
import com.example.tradingplatform.trades.usecases.IGetTradeByOrderIdsUseCase;
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
@Tag(name = "Trades", description = "V1.0 - 2024/04/09 - Trade functionalities")
public class TradeController {

    // Use case interfaces
    private final IGetTradeByIdUseCase getTradeUseCase;
    private final IGetTradeByOrderIdsUseCase getTradeByOrderIdsUseCase;

    @Operation(
            summary = "Get trade",
            description = "Service to retrieve a specific trade from trading platform",
            security = {@SecurityRequirement(name = "BearerAuth")},
            parameters = {@Parameter(name = ID, in = ParameterIn.PATH, required = true)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping(value = GET_TRADE_PATH)
    public Mono<ResponseEntity<Trade>> getTrade(
            @PathVariable(value = ID) @NotNull final Integer tradeId
    ) {
        return getTradeUseCase
                .execute(tradeId)
                .map(res -> ResponseEntity.status(HttpStatus.OK).body(res));
    }

    @Operation(
            summary = "Get trade",
            description = "Service to retrieve a specific trade giving a buyOrderId and sellOrderId",
            security = {@SecurityRequirement(name = "BearerAuth")},
            parameters = {
                    @Parameter(name = BUY_ORDER_ID, in = ParameterIn.PATH, required = true),
                    @Parameter(name = SELL_ORDER_ID, in = ParameterIn.PATH, required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping(value = GET_TRADE_BY_ORDER_IDS_PATH)
    public Mono<ResponseEntity<Trade>> getTradeByOrderIds(
            @PathVariable(value = BUY_ORDER_ID) @NotNull final Integer buyOrderId,
            @PathVariable(value = SELL_ORDER_ID) @NotNull final Integer sellOrderId
    ) {
        return getTradeByOrderIdsUseCase
                .execute(buyOrderId, sellOrderId)
                .map(res -> ResponseEntity.status(HttpStatus.OK).body(res));
    }

}
