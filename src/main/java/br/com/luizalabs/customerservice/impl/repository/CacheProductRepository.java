package br.com.luizalabs.customerservice.impl.repository;

import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Log4j2
@Repository
public class CacheProductRepository {
    @Qualifier("redisOperationsProduct")
    private final ReactiveRedisTemplate<String, ProductImplModel> productRedisTemplate;

    public CacheProductRepository(ReactiveRedisTemplate<String,ProductImplModel> template){
        this.productRedisTemplate = template;
    }


    @Value("${app-config.redis.ttl}")
    private Integer ttl;

    public Mono<Boolean> save(String key, ProductImplModel value){
        return productRedisTemplate
                .opsForValue()
                .set(key,value)
                .then(productRedisTemplate.expire(key, Duration.ofSeconds(ttl)))
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to save data key: {}", key,throwable);
                    return Mono.just(false);
                });
    }

    public Mono<ProductImplModel> get(String key){
        return productRedisTemplate
                .opsForValue()
                .get(key)
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to retrieve data Key: {}", key,throwable);
                    return Mono.empty();
                });
    }

    public Mono<Boolean> existsForKey(String key){
        return productRedisTemplate
                .hasKey(key)
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to query data key: {}", key,throwable);
                    return Mono.just(false);
                });
    }

}
