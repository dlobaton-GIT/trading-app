package com.example.tradingplatform.trades.usecases.impl;

import com.example.tradingplatform.shared.externalservices.h2db.mappers.IH2Mapper;
import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.IOrdersRepository;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.ITradeRepository;
import com.example.tradingplatform.shared.miscellaneous.enums.OrderType;
import com.example.tradingplatform.trades.usecases.TradeGeneratorService;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Builder
public class TradeGeneratorImpl implements TradeGeneratorService {

    // Repositories
    private final IOrdersRepository ordersRepository;
    private final ITradeRepository tradeRepository;

    // Mappers
    private final IH2Mapper h2Mapper;
    private final Gson gson;

    @Override
    public Mono<Trade> execute(
            final Orders inputOrder,
            final String closingOrderType
    ) {
        log.info("Starting trade generator to create possible trades");
        final boolean inputOrderIsBuyOrder = inputOrder.getType().equals(OrderType.BUY.name());

        log.debug("Searching for possible trades within existing orders");
        return (inputOrderIsBuyOrder
                    ? ordersRepository.findSellTrade(inputOrder.getId(), inputOrder.getSecurity_id())
                    : ordersRepository.findBuyTrade(inputOrder.getId(), inputOrder.getSecurity_id()))
                .switchIfEmpty(Mono.fromCallable(() -> {
                    log.info("Unsuccessful trade research");
                    return null;
                }))
                .doOnError(err -> log.error("Error while searching for a trade: {}", err.getMessage()))
                .flatMap(matchingOrder -> {
                    log.info("A trade occurred between orders {} - {}",
                            gson.toJson(inputOrder),
                            gson.toJson(matchingOrder));

                    log.debug("Creating new trade");
                    return tradeRepository.save(h2Mapper.toTradeEntity(inputOrder, matchingOrder))
                            .zipWith(Mono.just(matchingOrder));
                })
                .doOnError(err -> log.error("Error while creating trade: {}", err.getMessage()))
                .flatMap(tuple -> {
                    log.info("Trade created: {}", new Gson().toJson(tuple.getT1()));

                    log.info("Updating both orders data after the trade");
                    final int matchingOrderRemainingQuantity =  tuple.getT2().getQuantity() - tuple.getT1().getQuantity();
                    final int inputOrderRemainingQuantity = inputOrder.getQuantity() - tuple.getT1().getQuantity();

                    return Mono.zip(
                            Mono.just(tuple.getT1()),
                            // update matching order data
                            ordersRepository.updateOrderData(
                                    tuple.getT2().getId(),
                                    matchingOrderRemainingQuantity == 0,
                                    matchingOrderRemainingQuantity),
                            // update input order data
                            ordersRepository.updateOrderData(
                                    inputOrder.getId(),
                                    inputOrderRemainingQuantity == 0,
                                    inputOrderRemainingQuantity)
                            );
                })
                .flatMap(res -> {
                    log.debug("Orders successfully updated: {} - {}",
                            gson.toJson(res.getT2()), gson.toJson(res.getT3()));

                    log.info("Trade generator finished successfully");
                    return Mono.just(res.getT1());
                });
    }
}
