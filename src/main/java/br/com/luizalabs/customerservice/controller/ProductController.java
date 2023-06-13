package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.model.response.ProductPageControllerResponse;
import br.com.luizalabs.customerservice.integration.ProductIntegration;
import br.com.luizalabs.customerservice.integration.model.ProductIntegrationResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private ControllerFacade facade;

    @GetMapping
    public Mono<ProductPageControllerResponse> getProducts(int page){
        return facade.findProductsByPage(page);
    }

}
