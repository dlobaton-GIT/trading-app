package com.example.tradingplatform.users.usecases;

import com.example.tradingplatform.shared.externalservices.h2db.model.Users;
import reactor.core.publisher.Mono;

public interface IValidateUserUseCase {

    /**
     * Method to validate user authorization
     * @param username user name
     * @param authorization header authorization
     * @return User entity
     */
    Mono<Users> validateUserAuthorization(final String username, final String authorization);
}
