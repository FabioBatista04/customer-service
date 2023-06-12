package br.com.luizalabs.customerservice.impl.repository;

import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<CustomerImplModel, String> {

    Mono<CustomerImplModel> findByEmail(String email);

    @Query("{ }")
    Flux<CustomerImplModel> findAll(Pageable pageable);

}

