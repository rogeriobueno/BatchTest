package com.batch.batchtest.repository;

import com.batch.batchtest.db.ClientDB;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ClientRepository extends R2dbcRepository<ClientDB, Long> {
    Flux<ClientDB> findAllByActive(Boolean active);
}
