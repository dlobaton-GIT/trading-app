package com.example.tradingplatform.users.usecases.impl;

import com.example.tradingplatform.shared.config.error.ServiceException;
import com.example.tradingplatform.shared.externalservices.h2db.mappers.IH2Mapper;
import com.example.tradingplatform.shared.externalservices.h2db.model.Users;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.IUsersRepository;
import com.example.tradingplatform.users.usecases.ICreateUserUseCase;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.example.tradingplatform.shared.miscellaneous.utils.Messages.LOG_END_FORMAT;
import static com.example.tradingplatform.shared.miscellaneous.utils.Messages.LOG_START_FORMAT;

@Slf4j
@Service
@Builder
public class CreateUserInteractor implements ICreateUserUseCase {

    public static final String ACTION = "Create user";

    // Repositories
    private final IUsersRepository usersRepository;

    // Mappers
    private final IH2Mapper h2Mapper;
    private final Gson gson;

    @Override
    public Mono<Users> execute(final String username, final String authorization) {
        log.info(LOG_START_FORMAT, ACTION, username, null, null, null);

        return usersRepository
                .save(h2Mapper.toUserEntity(username, authorization.substring(7)))
                .switchIfEmpty(Mono.fromCallable(() -> {
                    log.warn("Empty response receiving while creating user");
                    throw new ServiceException(String.valueOf(HttpStatus.CONFLICT.value()),
                            HttpStatus.CONFLICT.getReasonPhrase());
                }))
                .doOnError(err -> {
                    log.error("Error creating user: {}", err.getMessage());
                    throw new ServiceException(String.valueOf(HttpStatus.CONFLICT.value()),
                            err.getLocalizedMessage());
                })
                .doOnNext(res -> log.debug(LOG_END_FORMAT, ACTION, username, null, null));
    }
}
