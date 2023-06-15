package br.com.luizalabs.customerservice.impl.service;

import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import br.com.luizalabs.customerservice.impl.repository.CacheCustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Log4j2
@Repository
@AllArgsConstructor
public class CacheCustomerImpl {

    private final CacheCustomerRepository cacheCustomerImpl;


    public Mono<Boolean> existsKey(String key){
        return cacheCustomerImpl.existsForKey(key);
    }

    public Mono<CustomerImplModel> get(String key){
        return cacheCustomerImpl.get(key);
    }

    public Mono<Boolean> save(String key, CustomerImplModel customerImplModel){
        return cacheCustomerImpl.save(key, customerImplModel).map(isSave -> {
            if(!isSave)
                log.error("Error encountered while saving in Redis customer with ID: {} ", customerImplModel.getId());
            return isSave;
        });
    }
}
