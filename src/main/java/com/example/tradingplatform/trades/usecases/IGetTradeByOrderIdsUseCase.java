package com.example.tradingplatform.trades.usecases;

import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import reactor.core.publisher.Mono;

public interface IGetTradeByOrderIdsUseCase {

    /**
     * Method to retrieve a Trade from database
     * @param buyOrderId buy order identifier
     * @param sellOrderId sell order identifier
     * @return Mono containing Trade entity if status OK, 404 for not found
     */
    Mono<Trade> execute(final Integer buyOrderId, final Integer sellOrderId);
}
