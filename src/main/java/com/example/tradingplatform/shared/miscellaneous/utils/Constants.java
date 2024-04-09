package com.example.tradingplatform.shared.miscellaneous.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Constants {

    // Formats
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    // Fields names
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String MESSAGE = "message";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SECURITY_NAME = "securityName";
    public static final String SECURITY_ID = "security_id";
    public static final String TYPE = "type";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";
    public static final String FULL_FILLED = "full_filled";
    public static final String ID = "id";
    public static final String BUY_ORDER_ID = "buyOrderId";
    public static final String SELL_ORDER_ID = "sellOrderId";

    // Endpoint paths

    public static final String V1 = "/v1";
    public static final String BUY_ORDER_PATH = "/buy-order";
    public static final String SELL_ORDER_PATH = "/sell-order";
    public static final String GET_SECURITY_PATH = "/get-security/{securityName}";
    public static final String GET_ORDER_PATH = "/get-order/{id}";
    public static final String GET_TRADE_PATH = "/get-trade/{id}";
    public static final String GET_TRADE_BY_ORDER_IDS_PATH = "/get-trade/{buyOrderId}/{sellOrderId}";

    // Patterns
    public static final String ALPHANUMERIC_MAX_50_CHARS_PATTERN = "[a-zA-Z0-9]{0," + 50 + "}";
    public static final String ALPHANUMERIC_MAX_10_CHARS_PATTERN = "[a-zA-Z0-9]{0," + 10 + "}";

    // Field values
    public static final String OPEN_API_INTEGER_TYPE = "Integer";
    public static final String OPEN_API_BOOLEAN_TYPE = "Boolean";
    public static final String OPEN_API_STRING_TYPE = "String";
    public static final String STR_NO = "NO";
}
