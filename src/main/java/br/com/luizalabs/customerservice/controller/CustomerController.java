package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.model.request.CustomerControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.CustomerControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.ProductControllerResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping(value = "/customers")
public class CustomerController {

    private final ControllerFacade controllerFacade;

    @PostMapping
    @ResponseStatus(CREATED)
    public Mono<CustomerControllerResponse> create(@Valid @RequestBody CustomerControllerRequest client) {
        return controllerFacade.createCustomer(client);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public Mono<CustomerControllerResponse> update(@PathVariable("id") String id,
                                                   @Valid @RequestBody CustomerControllerRequest client
    ) {
        return controllerFacade.updateCustomer(id, client);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Mono<CustomerControllerResponse> findById(@PathVariable(value = "id") String id) {
        return controllerFacade.findCustomerById(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public Flux<CustomerControllerResponse> findAllCustomers(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        return controllerFacade.findAllCustomers(page, size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable("id") String id) {
        return controllerFacade.deleteCustomer(id);
    }

    @GetMapping("/{id}/favorites")
    @ResponseStatus(OK)
    public Flux<ProductControllerResponse> findFavoriteProducts(@PathVariable String id) {
        return controllerFacade.findFavoriteProducts(id);
    }

    @PostMapping("/{id}/favorites/{product_id}")
    @ResponseStatus(CREATED)
    public Flux<ProductControllerResponse> addProductToFavorites(@PathVariable String id,
                                                                 @PathVariable String product_id
    ) {
        return controllerFacade.addFavoriteProduct(id, product_id);
    }

    @DeleteMapping("/{id}/favorites/{product_id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deleteFavoriteProduct(@PathVariable String id, @PathVariable String product_id) {
        return controllerFacade.deleteFavoriteProduct(id, product_id);
    }

}
