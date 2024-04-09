package com.example.tradingplatform.trades.usecases.impl;

import com.example.tradingplatform.shared.config.error.ServiceException;
import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.ITradeRepository;
import com.example.tradingplatform.trades.usecases.IGetTradeByOrderIdsUseCase;
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
public class GetTradeByOrderIdsInteractor implements IGetTradeByOrderIdsUseCase {

    private static final String ACTION = "Get trade by buyOrderId and sellOrderId";

    // Repositories
    private final ITradeRepository tradeRepository;

    // Mappers
    private final Gson gson;

    /**
     * Method to retrieve a Trade from database
     * @param buyOrderId buy order identifier
     * @param sellOrderId sell order identifier
     * @return Mono containing Trade entity if status OK, 404 for not found
     */
    public Mono<Trade> execute(final Integer buyOrderId, final Integer sellOrderId) {
        log.info(LOG_START_FORMAT, ACTION, null, null, null, null);

        return tradeRepository
                .findTradeByOrderIds(buyOrderId, sellOrderId)
                .switchIfEmpty(Mono.fromCallable(() -> {
                    log.warn("Trade invalid: not found");
                    throw new ServiceException(String.valueOf(HttpStatus.NOT_FOUND.value()),
                            "Trade not found in database");
                }))
                .doOnError(err -> {
                    log.error("Error retrieving trade: {}", err.getMessage());
                    throw new ServiceException(String.valueOf(HttpStatus.CONFLICT.value()),
                            err.getLocalizedMessage());
                })
                .doOnNext(res -> log.info(LOG_END_FORMAT, ACTION, null, null, null, res));
    }
}
