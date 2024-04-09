package com.example.tradingplatform.shared.externalservices.h2db.repositories;


import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ITradeRepository extends ReactiveCrudRepository<Trade, Integer> {

    /**
     * Method to find trade by id
     * @param tradeId trade identifier
     * @return Mono containing User entity if status OK, void Mono otherwise
     */
    @Query("SELECT * FROM Trade t WHERE t.id= :tradeId")
    Mono<Trade> findTradeById(final int tradeId);

    @Query("SELECT * FROM Trade t WHERE t.buy_order_id= :buyOrderId AND t.sell_order_id= :sellOrderId")
    Mono<Trade> findTradeByOrderIds(final int buyOrderId, final int sellOrderId);

}
