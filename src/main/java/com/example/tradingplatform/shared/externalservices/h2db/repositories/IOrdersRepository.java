package com.example.tradingplatform.shared.externalservices.h2db.repositories;


import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IOrdersRepository extends ReactiveCrudRepository<Orders, Integer> {

    /**
     * Method to find order by id
     * @param orderId order identifier
     * @return Mono containing User entity if status OK, void Mono otherwise
     */
    @Query("SELECT * FROM Orders o WHERE o.id= :orderId")
    Mono<Orders> findOrderById(final int orderId);

    @Query("SELECT s.* " +
            "FROM Orders b " +
            "INNER JOIN Orders s ON b.security_id = s.security_id " +
            "WHERE b.type = 'BUY' AND s.type = 'SELL' AND b.price >= s.price " +
            "AND s.full_filled = false " +
            "AND b.quantity > 0 " +
            "AND s.quantity > 0 " +
            "AND b.id = :buyOrderId " +
            "AND b.security_id = :securityId " +
            "ORDER BY s.price ASC, b.price DESC " +
            "LIMIT 1")
    Mono<Orders> findSellTrade(final int buyOrderId, final int securityId);

    @Query("SELECT b.* " +
            "FROM Orders s " +
            "INNER JOIN Orders b ON b.security_id = s.security_id " +
            "WHERE b.type = 'BUY' AND s.type = 'SELL' AND b.price >= s.price " +
            "AND b.full_filled = false " +
            "AND b.quantity > 0 " +
            "AND s.quantity > 0 " +
            "AND s.id = :sellOrderId " +
            "AND s.security_id = :securityId " +
            "ORDER BY s.price ASC, b.price DESC " +
            "LIMIT 1")
    Mono<Orders> findBuyTrade(final int sellOrderId, final int securityId);

    @Modifying
    @Query("UPDATE Orders o SET o.full_filled = :fulfilled, o.quantity = :quantity WHERE o.id = :orderId")
    Mono<Orders> updateOrderData(int orderId, boolean fulfilled, int quantity);

}
