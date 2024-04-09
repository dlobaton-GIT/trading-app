package com.example.tradingplatform.shared.externalservices.h2db.repositories;


import com.example.tradingplatform.shared.externalservices.h2db.model.Security;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ISecurityRepository extends ReactiveCrudRepository<Security, Integer> {

    /**
     * Method to find security by name
     * @param securityName security name
     * @return Mono containing User entity if status OK, void Mono otherwise
     */
    @Query("SELECT * FROM Security s WHERE s.name= :securityName")
    Mono<Security> findSecurityByName(final String securityName);

}
