package br.com.luizalabs.customerservice.impl.service;

import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import br.com.luizalabs.customerservice.impl.repository.CacheProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Log4j2
@Service
@AllArgsConstructor
public class CacheProductImpl {

    private final CacheProductRepository cacheProductRepository;


    public Mono<Boolean> existsKey(String key){
        return cacheProductRepository.existsForKey(key);
    }

    public Mono<ProductImplModel> get(String key){
        return cacheProductRepository.get(key);
    }

    public Mono<Boolean> save(String key,ProductImplModel productImplModel){
        return cacheProductRepository.save(key, productImplModel).map(isSave -> {
            if(!isSave)
                log.error("Error encountered while saving in Redis product with ID: {} ", productImplModel.getId());
            return isSave;
        });
    }

}
