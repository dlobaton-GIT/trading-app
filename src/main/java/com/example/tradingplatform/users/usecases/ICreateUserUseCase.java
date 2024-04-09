package com.example.tradingplatform.users.usecases;

import com.example.tradingplatform.shared.externalservices.h2db.model.Users;
import reactor.core.publisher.Mono;

public interface ICreateUserUseCase {

    /**
     * Method to validate user authorization
     * @param authorization user password
     * @param username user name
     * @return User entity
     */
    Mono<Users> execute(final String authorization, final String username);
}
