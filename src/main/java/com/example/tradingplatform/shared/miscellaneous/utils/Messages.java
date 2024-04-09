package com.example.tradingplatform.shared.miscellaneous.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Messages {

    // Logs messages
    public static final String USERNAME_INVALID = "Required parameter 'username' must meet alphanumeric 50 chars format";
    public static final String USER_ID_INVALID = "Required parameter 'user_id' must be a positive number";
    public static final String SECURITY_ID_INVALID = "Required parameter 'security_id' must be a positive number";
    public static final String ORDER_ID_INVALID = "Required parameter 'orderId' must be a positive number";
    public static final String SECURITY_NAME_INVALID = "Required parameter 'secutiryName' must meet alphanumeric 10 chars format";
    public static final String PRICE_INVALID = "Required parameter 'price' must be a positive number";
    public static final String TYPE_INVALID = "Required parameter 'type' must meet alphanumeric 10 chars format";
    public static final String QUANTITY_INVALID = "Required parameter 'quantity' must be a positive number";
    public static final String FULL_FILLED_INVALID = "Required parameter 'full_filled' must be informed (boolean)";
    public static final String AUTHORIZATION_INVALID = "Authorization token 'password' must meet alphanumeric 10 chars format";
    public static final String AUTHORIZATION_INCORRECT = "Unauthorized: 'password' is not correct";
    public static final String LOG_START_FORMAT = "[START - {}, USER_NAME - {}, SECURITY_NAME - {}, TRADE_ID - {}]: {}";
    public static final String LOG_END_FORMAT = "[END - {}, USER_NAME - {}, SECURITY_NAME - {}, TRADE_ID - {}]: {}";
}
