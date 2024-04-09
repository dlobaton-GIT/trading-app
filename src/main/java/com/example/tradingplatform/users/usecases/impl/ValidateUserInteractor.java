package com.example.tradingplatform.users.usecases.impl;

import com.example.tradingplatform.shared.config.error.ServiceException;
import com.example.tradingplatform.shared.externalservices.h2db.model.Users;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.IUsersRepository;
import com.example.tradingplatform.users.usecases.IValidateUserUseCase;
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
public class ValidateUserInteractor implements IValidateUserUseCase {

    private static final String ACTION = "Validate user credentials";

    // Repositories
    private final IUsersRepository usersRepository;

    // Mappers
    private final Gson gson;

    @Override
    public Mono<Users> validateUserAuthorization(final String username, final String authorization) {
        log.info(LOG_START_FORMAT, ACTION, username, null, null, null);

        return usersRepository
                .findUserByUsernameAndPassword(username, authorization.substring(7))
                .doOnError(err -> {
                    log.error("Error retrieving user: {}", err.getMessage());
                    throw new ServiceException(String.valueOf(HttpStatus.CONFLICT.value()),
                            err.getLocalizedMessage());
                })
                .switchIfEmpty(Mono.fromCallable(() -> {
                    log.warn("User invalid: unauthorized");
                    throw new ServiceException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                            HttpStatus.UNAUTHORIZED.getReasonPhrase());
                }))
                .doOnNext(res -> log.info(LOG_END_FORMAT, ACTION, username, null, null, res));
    }
}
