package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.model.response.ProductPageControllerResponse;
import br.com.luizalabs.customerservice.exeptions.BadRequestException;
import br.com.luizalabs.customerservice.integration.ProductIntegration;
import br.com.luizalabs.customerservice.integration.model.ProductIntegrationResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private ControllerFacade facade;

    @GetMapping("/")
    public Mono<ProductPageControllerResponse> getProducts(@RequestParam String  page ){
        return facade.findProductsByPage(page);
    }

}
