package com.example.tradingplatform.orders.usecases.impl;

import com.example.tradingplatform.orders.dtos.request.CreateOrderRequestDto;
import com.example.tradingplatform.orders.dtos.response.CreateOrderResponseDto;
import com.example.tradingplatform.orders.mappers.IOrdersMapper;
import com.example.tradingplatform.orders.usecases.ICreateOrderUseCase;
import com.example.tradingplatform.security.usecases.IGetSecurityUseCase;
import com.example.tradingplatform.shared.externalservices.h2db.mappers.IH2Mapper;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.IOrdersRepository;
import com.example.tradingplatform.shared.miscellaneous.enums.OrderType;
import com.example.tradingplatform.trades.usecases.TradeGeneratorService;
import com.example.tradingplatform.users.usecases.IValidateUserUseCase;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.example.tradingplatform.shared.miscellaneous.utils.Messages.LOG_END_FORMAT;
import static com.example.tradingplatform.shared.miscellaneous.utils.Messages.LOG_START_FORMAT;

@Slf4j
@Service
@Builder
public class CreateOrderInteractor implements ICreateOrderUseCase {

    public static final String ACTION = "Sell security";

    // Services
    private final IValidateUserUseCase usersUseCases;
    private final IGetSecurityUseCase securityUseCases;
    private final TradeGeneratorService generateTradeUseCase;

    // Repositories
    private final IOrdersRepository ordersRepository;

    // Mappers
    private final IOrdersMapper ordersMapper;
    private final IH2Mapper h2Mapper;
    private final Gson gson;

    /**
     * Service that allows a user to purchase a specific security
     * @param authorization password uf the user
     * @param entryData request body
     * @param orderType order type
     * @return SellSecurityResponseDto with output data
     */
    @Override
    public Mono<CreateOrderResponseDto> execute(
            final String authorization,
            final CreateOrderRequestDto entryData,
            final OrderType orderType
    ) {
        log.info(LOG_START_FORMAT, ACTION, entryData.getUsername(), entryData.getSecurityName(),
                null, new Gson().toJson(entryData));

        return usersUseCases.validateUserAuthorization(entryData.getUsername(), authorization)
                .zipWith(securityUseCases.execute(entryData.getSecurityName()))
                .flatMap(tuple -> {
                    log.info("Creating new {} order for security: {} with price: {} and amount {}",
                            orderType.name(), entryData.getSecurityName(), entryData.getPrice(),
                            entryData.getQuantity());

                    return ordersRepository
                            .save(h2Mapper.toOrderEntity(tuple, entryData, orderType));
                })
                .switchIfEmpty(Mono.fromCallable(() -> {
                    log.warn("Empty response while creating order");
                    return null;
                }))
                .doOnError(err -> log.error("Error creating {} order: {}", orderType.name(), err.getMessage()))
                .flatMap(order -> {
                    log.debug("Order {} successfully created : {}", order.getId(), gson.toJson(order));
                    generateTradeUseCase.execute(order, orderType.name()).subscribe();

                    log.debug("Building output data");

                    final var finalResponse = ordersMapper.toSellSecurityResponseDto(order);
                    log.info(LOG_END_FORMAT, ACTION, entryData.getUsername(), entryData.getSecurityName(),
                            null, new Gson().toJson(finalResponse));

                    return Mono.just(finalResponse);
                });
    }


}
