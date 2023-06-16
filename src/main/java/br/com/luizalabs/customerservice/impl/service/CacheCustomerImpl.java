package br.com.luizalabs.customerservice.impl.service;

import br.com.luizalabs.customerservice.controller.model.response.CustomerControllerResponse;
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
    public Mono<CustomerImplModel> saveAndReturn(String key, CustomerImplModel customerImplModel) {
        return save(key, customerImplModel)
                .map(isCached -> {
                    if( isCached ) {
                        log.info("Saved cache to Customer ID {}", key);
                        return customerImplModel;
                    }
                    log.error("Erro save cache customer ID {}", key);
                    return customerImplModel;
                });
    }

    public Mono<Boolean> remove(String key){
        return cacheCustomerImpl.remove(key)
                .map(isRemoved -> {
                    if(!isRemoved)
                        log.error("Error encountered while remove in Redis customer with ID: {} ", key);
                    return isRemoved;
                });
    }
    public Mono<Void> removeAndEmpty(String key){
        return remove(key)
                .flatMap(isRemoved -> Mono.empty());
    }

    public Mono<CustomerImplModel> returnCustomerIfExists(String key) {
        return existsKey(key)
                .filter(exists -> exists)
                .flatMap(contains -> get(key));
    }
}
