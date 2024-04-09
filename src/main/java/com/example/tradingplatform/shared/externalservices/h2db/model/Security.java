package com.example.tradingplatform.shared.externalservices.h2db.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static com.example.tradingplatform.shared.miscellaneous.utils.Constants.NAME;

@Value
@Builder
@JsonAutoDetect
@Table(name = "Security")
public class Security {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Integer id;

    String name;
}
