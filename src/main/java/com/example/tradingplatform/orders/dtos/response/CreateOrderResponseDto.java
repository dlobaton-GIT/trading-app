package com.example.tradingplatform.orders.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.example.tradingplatform.shared.miscellaneous.utils.Constants.*;
import static com.example.tradingplatform.shared.miscellaneous.utils.Messages.*;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
@Schema(name = "CreateOrderResponseDto",
        description = "Output data for create order use case.")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateOrderResponseDto {

    @NotNull(message = ORDER_ID_INVALID)
    @Positive(message = ORDER_ID_INVALID)
    @Schema(name = ID,
            example = "3",
            type = OPEN_API_INTEGER_TYPE,
            description = "Order identifier",
            requiredMode = Schema.RequiredMode.REQUIRED)
    Integer id;

    @NotNull(message = USER_ID_INVALID)
    @Positive(message = USER_ID_INVALID)
    @Schema(name = USER_ID,
            example = "12",
            type = OPEN_API_INTEGER_TYPE,
            description = "User identifier",
            requiredMode = Schema.RequiredMode.REQUIRED)
    Integer user_id;

    @NotNull(message = SECURITY_ID_INVALID)
    @Positive(message = SECURITY_ID_INVALID)
    @Schema(name = SECURITY_ID,
            example = "2",
            type = OPEN_API_INTEGER_TYPE,
            description = "Security identifier",
            requiredMode = Schema.RequiredMode.REQUIRED)
    Integer security_id;

    @NotBlank(message = TYPE_INVALID)
    @Schema(name = TYPE,
            example = "SELL",
            type = OPEN_API_STRING_TYPE,
            description = "Order type",
            requiredMode = Schema.RequiredMode.REQUIRED)
    String type;

    @NotNull(message = PRICE_INVALID)
    @Positive(message = PRICE_INVALID)
    @Schema(name = PRICE,
            example = "100",
            type = OPEN_API_INTEGER_TYPE,
            description = "Order price",
            requiredMode = Schema.RequiredMode.REQUIRED)
    Integer price;

    @NotNull(message = QUANTITY_INVALID)
    @Positive(message = QUANTITY_INVALID)
    @Schema(name = QUANTITY,
            example = "50",
            type = OPEN_API_INTEGER_TYPE,
            description = "Order quantity",
            requiredMode = Schema.RequiredMode.REQUIRED)
    Integer quantity;

    @NotNull(message = FULL_FILLED_INVALID)
    @Schema(name = FULL_FILLED,
            example = "true",
            type = OPEN_API_BOOLEAN_TYPE,
            description = "Order full filled indicator",
            requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean full_filled;

}

