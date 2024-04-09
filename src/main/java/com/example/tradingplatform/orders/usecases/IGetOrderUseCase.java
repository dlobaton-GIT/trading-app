package com.example.tradingplatform.orders.usecases;

import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
import reactor.core.publisher.Mono;

public interface IGetOrderUseCase {

    /**
     * Method to retrieve an order from database
     * @param orderId order identifier
     * @return Mono containing Order entity if status OK, 404 for not found
     */
    Mono<Orders> execute(final int orderId);
}
