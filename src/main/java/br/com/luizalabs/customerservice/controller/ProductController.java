package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.model.response.ProductPageControllerResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
