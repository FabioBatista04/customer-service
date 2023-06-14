package br.com.luizalabs.customerservice.config;
import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ProductConfiguration {

    @Bean
    ReactiveRedisOperations<String, ProductImplModel> redisOperations(ReactiveRedisConnectionFactory factory){
        Jackson2JsonRedisSerializer<ProductImplModel> serializer = new Jackson2JsonRedisSerializer<>(ProductImplModel.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String,ProductImplModel> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String,ProductImplModel> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory,context);
    }
}
