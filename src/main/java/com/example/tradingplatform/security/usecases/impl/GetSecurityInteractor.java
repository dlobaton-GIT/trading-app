package com.example.tradingplatform.security.usecases.impl;

import com.example.tradingplatform.security.usecases.IGetSecurityUseCase;
import com.example.tradingplatform.shared.config.error.ServiceException;
import com.example.tradingplatform.shared.externalservices.h2db.model.Security;
import com.example.tradingplatform.shared.externalservices.h2db.repositories.ISecurityRepository;
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
public class GetSecurityInteractor implements IGetSecurityUseCase {

    private static final String ACTION = "Get security";

    // Repositories
    private final ISecurityRepository securityRepository;

    // Mappers
    private final Gson gson;

    /**
     * Method to validate whether security is valid
     *
     * @param securityName security name
     * @return Mono containing Security entity if status OK, 404 and 409 code errors
     */
    public Mono<Security> execute(final String securityName) {
        log.info(LOG_START_FORMAT, ACTION, null, securityName, null, null);

        return securityRepository
                .findSecurityByName(securityName)
                .doOnError(err -> {
                    log.error("Error retrieving security: {}", err.getMessage());
                    throw new ServiceException(String.valueOf(HttpStatus.CONFLICT.value()),
                            err.getLocalizedMessage());
                })
                .switchIfEmpty(Mono.fromCallable(() -> {
                    log.warn("Security invalid: not found");
                    throw new ServiceException(String.valueOf(HttpStatus.NOT_FOUND.value()),
                            "Security not found in database");
                }))
                .doOnNext(res -> log.info(LOG_END_FORMAT, ACTION, null, securityName, null, res));
    }
}
