package com.example.tradingplatform.shared.externalservices.h2db.repositories;


import com.example.tradingplatform.shared.externalservices.h2db.model.Users;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface IUsersRepository extends ReactiveCrudRepository<Users, Integer> {

    /**
     * Method to find user by name and password
     * @param username username
     * @param password password
     * @return Mono containing User entity if status OK, void Mono otherwise
     */
    @Query("SELECT * FROM Users u WHERE u.username= :username AND u.password= :password")
    Mono<Users> findUserByUsernameAndPassword(final String username, final String password);

}
