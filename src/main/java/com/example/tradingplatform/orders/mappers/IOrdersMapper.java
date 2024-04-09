package com.example.tradingplatform.orders.mappers;

import com.example.tradingplatform.orders.dtos.response.CreateOrderResponseDto;
import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;
import org.mapstruct.Mapper;

@Mapper
public interface IOrdersMapper {

    CreateOrderResponseDto toSellSecurityResponseDto(final Orders orders);

}

