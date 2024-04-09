package com.example.tradingplatform.shared.externalservices.h2db.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Value
@Builder
@JsonAutoDetect
@Table(name = "Trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    Integer sell_order_id;

    Integer buy_order_id;

    Integer price;

    Integer quantity;
}
