package com.example.tradingplatform.orders.usecases;

import com.example.tradingplatform.orders.dtos.request.CreateOrderRequestDto;
import com.example.tradingplatform.orders.dtos.response.CreateOrderResponseDto;
import com.example.tradingplatform.shared.miscellaneous.enums.OrderType;
import reactor.core.publisher.Mono;

/**
 * This interface provides POST sell-security functionality
 */
public interface ICreateOrderUseCase {
    /**
     * Service that allows a user to sell a specific security
     * @param authorization password uf the user
     * @param securityName security name
     * @param entryData request body
     * @return SellSecurityResponseDto with output data
     */
    Mono<CreateOrderResponseDto> execute(
            final String authorization,
            final CreateOrderRequestDto entryData,
            final OrderType orderType);
}
