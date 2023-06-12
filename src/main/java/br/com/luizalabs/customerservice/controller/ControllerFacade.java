package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.mapper.AuthControllerMapper;
import br.com.luizalabs.customerservice.controller.mapper.CustomerControllerMapper;
import br.com.luizalabs.customerservice.controller.mapper.ProductControllerMapper;
import br.com.luizalabs.customerservice.controller.model.request.CustomerControllerRequest;
import br.com.luizalabs.customerservice.controller.model.request.UserControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.AuthControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.CustomerControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.ProductControllerResponse;
import br.com.luizalabs.customerservice.impl.AuthImpl;
import br.com.luizalabs.customerservice.impl.CustomerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ControllerFacade {

    private final CustomerImpl customerImpl;
    private final AuthImpl authImpl;


    public Mono<CustomerControllerResponse> createCustomer(CustomerControllerRequest customer) {
        return customerImpl.createCustomer(CustomerControllerMapper.mapperToCustomerImpl(customer))
                .map(CustomerControllerMapper::mapToCustomerControllerResponse);
    }

    public Mono<CustomerControllerResponse> updateCustomer(String id, CustomerControllerRequest customer) {
        return customerImpl.updateCustomer(id, CustomerControllerMapper.mapperToCustomerImpl(customer))
                .map(CustomerControllerMapper::mapToCustomerControllerResponse);
    }

    public Mono<CustomerControllerResponse> findCustomerById(String id) {
        return customerImpl.findCustomerById(id).map(CustomerControllerMapper::mapToCustomerControllerResponse);
    }

    public Flux<CustomerControllerResponse> findAllCustomers(int page, int size) {
        return customerImpl.findAllCustomers(page, size).map(CustomerControllerMapper::mapToCustomerControllerResponse);
    }

    public Flux<ProductControllerResponse> findFavoriteProducts(String id) {
        return customerImpl.findFavoriteProducts(id).map(ProductControllerMapper::mapFavoriteProductsTo);
    }

    public Flux<ProductControllerResponse> addFavoriteProduct(String id, String productId) {
        return customerImpl.addFavoriteProduct(id, productId).map(ProductControllerMapper::mapFavoriteProductsTo);
    }

    public Mono<Void> deleteCustomer(String id) {
        return customerImpl.deleteCustomerById(id);
    }

    public Mono<Void> deleteFavoriteProduct(String id, String productId) {
        return customerImpl.deleteFavoriteProduct(id, productId);

    }

    public Mono<AuthControllerResponse> createUser(UserControllerRequest request) {
        return authImpl.createUser(AuthControllerMapper.mapperToUserImplModel(request)).map(AuthControllerResponse::new);
    }

    public Mono<AuthControllerResponse> generateToken(UserControllerRequest request) {
        return authImpl.generateToken(AuthControllerMapper.mapperToUserImplModel(request)).map(AuthControllerResponse::new);
    }
}
