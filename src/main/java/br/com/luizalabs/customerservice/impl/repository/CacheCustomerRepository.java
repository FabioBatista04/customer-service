package br.com.luizalabs.customerservice.impl.repository;

import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Log4j2
@Repository
public class CacheCustomerRepository {
    @Qualifier("redisOperationsCustomer")
    private final ReactiveRedisTemplate<String, CustomerImplModel> customerRedisTemplate;

    public CacheCustomerRepository(ReactiveRedisTemplate<String, CustomerImplModel> template) {
        this.customerRedisTemplate = template;
    }


    @Value("${app-config.redis.ttl}")
    private Integer ttl;

    public Mono<Boolean> save(String key, CustomerImplModel value) {
        return customerRedisTemplate
                .opsForValue()
                .set(key, value)
                .then(customerRedisTemplate.expire(key, Duration.ofSeconds(ttl)))
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to save data key: {}", key, throwable);
                    return Mono.just(false);
                });
    }

    public Mono<CustomerImplModel> get(String key) {
        return customerRedisTemplate
                .opsForValue()
                .get(key)
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to retrieve data Key: {}", key, throwable);
                    return Mono.empty();
                });
    }

    public Mono<Boolean> existsForKey(String key) {
        return customerRedisTemplate
                .hasKey(key)
                .onErrorResume(throwable -> {
                    log.error("Error encountered while attempting to query data key: {}", key, throwable);
                    return Mono.just(false);
                });
    }

    public Mono<Boolean> remove(String key) {
        return customerRedisTemplate.opsForValue()
                .delete(key)
                .onErrorResume(throwable -> {
                    log.error("Error encountered while delete Customer Key: {}", key, throwable);
                    return Mono.empty();
                });
    }

}
