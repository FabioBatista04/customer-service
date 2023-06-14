package br.com.luizalabs.customerservice.impl.repository;

import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Log4j2
@AllArgsConstructor
public class CacheProductRepository {

    private final ReactiveRedisTemplate<String, ProductImplModel> reactiveRedisTemplate;

    @Value("${app-config.redis.ttl}")
    private Integer ttl;

    Mono<Boolean> save(String key, ProductImplModel value){
        return reactiveRedisTemplate
                .opsForValue()
                .set(key,value)
                .then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(ttl)))
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to save data key: {}", key,throwable);
                    return Mono.just(false);
                });
    }

    Mono<ProductImplModel> get(String key, ProductImplModel value){
        return reactiveRedisTemplate
                .opsForValue()
                .get(key)
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to retrieve data Key: {}", key,throwable);
                    return Mono.empty();
                });
    }

    Mono<Boolean> existsForKey(String key, ProductImplModel value){
        return reactiveRedisTemplate
                .hasKey(key)
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to query data key: {}", key,throwable);
                    return Mono.just(false);
                });
    }

}
