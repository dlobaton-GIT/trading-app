package com.example.tradingplatform.shared.externalservices.h2db.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@JsonAutoDetect
@Table(name = "Orders")
public class Orders {
    @Id
    Integer id;
    
    Integer user_id;
    
    Integer security_id;
    
    String type;
    
    Integer price;
    
    Integer quantity;
    
    Boolean full_filled;
}
