package com.example.tradingplatform.trades.usecases;

import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import reactor.core.publisher.Mono;

public interface IGetTradeByIdUseCase {

    /**
     * Method to retrieve a Trade from database
     * @param tradeId trade identifier
     * @return Mono containing Trade entity if status OK, 404 for not found
     */
    Mono<Trade> execute(final int tradeId);
}
