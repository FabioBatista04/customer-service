package br.com.luizalabs.customerservice.integration;

import br.com.luizalabs.customerservice.integration.model.ProductIntegrationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@AllArgsConstructor
public class ProductIntegration {

    private final WebClient webClient;
    private final String URI = "product/";

    public Mono<ProductIntegrationResponse> findByProductId(String id) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI + "{id}/")
                        .build(id))
                .retrieve()
                .bodyToMono(ProductIntegrationResponse.class)
                .onErrorResume(throwable -> {
                    log.error("Error request : {}", id, throwable);
                    return Mono.empty();
                })
                .doOnNext(response -> log.info("Returning response from Api Products: {}", response));
    }

}
