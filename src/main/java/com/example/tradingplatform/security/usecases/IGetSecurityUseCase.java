package com.example.tradingplatform.security.usecases;

import com.example.tradingplatform.shared.externalservices.h2db.model.Security;
import reactor.core.publisher.Mono;

public interface IGetSecurityUseCase {

    /**
     * Method to retrieve a security from database
     *
     * @param securityName security name
     * @return Mono containing Security entity if status OK, 404 and 409 code errors
     */
    Mono<Security> execute(final String securityName);
}
