package com.example.tradingplatform.orders.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import static com.example.tradingplatform.shared.miscellaneous.utils.Constants.*;
import static com.example.tradingplatform.shared.miscellaneous.utils.Messages.*;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
@Schema(name = "CreateOrderRequestDto",
        description = "Input data for create order use case.")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateOrderRequestDto {

    @NotBlank(message = SECURITY_NAME_INVALID)
    @Pattern(regexp = ALPHANUMERIC_MAX_50_CHARS_PATTERN, message = SECURITY_NAME_INVALID)
    @Schema(name = SECURITY_NAME,
            example = "WSB",
            type = OPEN_API_STRING_TYPE,
            description = "Security name",
            requiredMode = Schema.RequiredMode.REQUIRED)
    String securityName;

    @NotBlank(message = USERNAME_INVALID)
    @Pattern(regexp = ALPHANUMERIC_MAX_10_CHARS_PATTERN, message = USERNAME_INVALID)
    @Schema(name = USERNAME,
            example = "Anonymous",
            type = OPEN_API_STRING_TYPE,
            description = "User name",
            requiredMode = Schema.RequiredMode.REQUIRED)
    String username;

    @NotNull(message = PRICE_INVALID)
    @Positive(message = PRICE_INVALID)
    @Schema(name = PRICE,
            example = "100",
            type = OPEN_API_INTEGER_TYPE,
            description = "Price to sell",
            requiredMode = Schema.RequiredMode.REQUIRED)
    Integer price;


    @NotNull(message = QUANTITY_INVALID)
    @Schema(name = QUANTITY,
            example = "50",
            type = OPEN_API_INTEGER_TYPE,
            description = "Quantity to sell",
            requiredMode = Schema.RequiredMode.REQUIRED)
    Integer quantity;

}
