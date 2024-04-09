package com.example.tradingplatform.shared.externalservices.h2db.mappers;

import com.example.tradingplatform.orders.dtos.request.CreateOrderRequestDto;
import com.example.tradingplatform.shared.config.error.ServiceException;
import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
import com.example.tradingplatform.shared.externalservices.h2db.model.Security;
import com.example.tradingplatform.shared.externalservices.h2db.model.Trade;
import com.example.tradingplatform.shared.externalservices.h2db.model.Users;
import com.example.tradingplatform.shared.miscellaneous.enums.OrderType;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.http.HttpStatus;
import reactor.util.function.Tuple2;

@Mapper
public interface IH2Mapper {

    /**
     * Method to build 'Orders' entity from input data
     * @param tuple tuple containing User and Security information
     * @param entryData request body
     * @return Orders Entity
     */
    default Orders toOrderEntity(
            final Tuple2<Users, Security> tuple,
            final CreateOrderRequestDto entryData,
            final OrderType orderType
    ){

        if(ObjectUtils.anyNull(tuple, entryData)){
            return null;
        }

        return Orders.builder()
                .user_id(tuple.getT1().getId())
                .security_id(tuple.getT2().getId())
                .quantity(entryData.getQuantity())
                .price(entryData.getPrice())
                .type(orderType.name())
                .full_filled(false)
                .build();
    }

    /**
     * Method to build 'Trade' entity
     * @param inputOrder input order
     * @param matchingOrder matching order for trade
     * @return Trade Entity
     */
    default Trade toTradeEntity(final Orders inputOrder, final Orders matchingOrder){
        if (inputOrder == null || matchingOrder == null) {
            throw new ServiceException(String.valueOf(HttpStatus.CONFLICT.value()),
                    "Both inputOrder and matchingOrder must be non-null to create a Trade");
        }

        final var buyOrder = inputOrder.getType().equals(OrderType.BUY.name())
                ? inputOrder
                : matchingOrder;
        final var sellOrder = (buyOrder == inputOrder)
                ? matchingOrder
                : inputOrder;

        return Trade.builder()
                .buy_order_id(buyOrder.getId())
                .sell_order_id(sellOrder.getId())
                .price(sellOrder.getPrice())
                // the quantity would be the minimum of both services
                .quantity(Math.min(sellOrder.getQuantity(), buyOrder.getQuantity()))
                .build();
    }

    /**
     * Method to build User entity given username and password
     * @param username user name
     * @param password user password
     * @return User entity
     */
    default Users toUserEntity(final String username, final String password){
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException(String.valueOf(HttpStatus.CONFLICT.value()),
                    "Both username and password must be non-null to create a User");
        }

        return Users.builder()
                .username(username)
                .password(password)
                .build();
    }

}
