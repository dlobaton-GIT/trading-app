package com.example.tradingplatform.orders.usecases.impl;

import com.example.tradingplatform.orders.usecases.IGetOrderUseCase;
import com.example.tradingplatform.shared.config.error.ServiceException;
import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.IOrdersRepository;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.example.tradingplatform.shared.miscellaneous.utils.Messages.LOG_END_FORMAT;
import static com.example.tradingplatform.shared.miscellaneous.utils.Messages.LOG_START_FORMAT;

@Slf4j
@Service
@Builder
public class GetOrderInteractor implements IGetOrderUseCase {

    private static final String ACTION = "Get order";

    // Repositories
    private final IOrdersRepository orderRepository;

    // Mappers
    private final Gson gson;

    /**
     * Method to retrieve an order from database
     * @param orderId order identifier
     * @return Mono containing Order entity if status OK, 404 for not found
     */
    public Mono<Orders> execute(final int orderId) {
        log.info(LOG_START_FORMAT, ACTION, null, orderId, null, null);

        return orderRepository
                .findOrderById(orderId)
                .doOnError(err -> {
                    log.error("Error retrieving order: {}", err.getMessage());
                    throw new ServiceException(String.valueOf(HttpStatus.CONFLICT.value()),
                            err.getLocalizedMessage());
                })
                .switchIfEmpty(Mono.fromCallable(() -> {
                    log.warn("Order invalid: not found");
                    throw new ServiceException(String.valueOf(HttpStatus.NOT_FOUND.value()),
                            "Order not found in database");
                }))
                .doOnNext(res -> log.info(LOG_END_FORMAT, ACTION, null, orderId, null, res));
    }
}
