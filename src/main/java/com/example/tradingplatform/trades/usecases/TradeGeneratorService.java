package com.example.tradingplatform.trades.usecases;

import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import reactor.core.publisher.Mono;

/**
 * This interface provides POST generate-trade functionality
 */
public interface TradeGeneratorService {

    /**
     * Service that evaluates whether a trade occurs
     * @param order newly created order
     * @return void Mono
     */
    Mono<Trade> execute(final Orders order, final String closingOrderType);
}
