package br.com.luizalabs.customerservice.impl;

import br.com.luizalabs.customerservice.controller.mapper.CustomerControllerMapper;
import br.com.luizalabs.customerservice.exeptions.BadRequestException;
import br.com.luizalabs.customerservice.exeptions.NotFoundException;
import br.com.luizalabs.customerservice.impl.facade.CustomerImplFacade;
import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import br.com.luizalabs.customerservice.impl.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.HashSet;
import java.util.Map;

@Log4j2
@Service
@AllArgsConstructor
public class CustomerImpl {

    private final CustomerImplFacade customerImplFacade;
    private final CustomerRepository customerRepository;

    public Mono<CustomerImplModel> createCustomer(CustomerImplModel customerImplModel) {
        return customerRepository.findByEmail(customerImplModel.getEmail())
                .doOnSuccess(this::validEmail)
                .switchIfEmpty(customerRepository.save(customerImplModel));
    }

    public Mono<CustomerImplModel> updateCustomer(String id, CustomerImplModel customerImplModel) {
        return customerRepository.findByEmail(customerImplModel.getEmail())
                .switchIfEmpty(findCustomerById(id)
                        .map(old -> CustomerControllerMapper.mapperToCustomerUpdated(old, customerImplModel))
                        .flatMap(customerRepository::save))
                .zipWith(findCustomerById(id))
                .doOnSuccess(this::validEmailExists)
                .map(Tuple2::getT1)
                .map(customerDataBase -> CustomerControllerMapper.mapperToCustomerUpdated(customerDataBase, customerImplModel))
                .flatMap(customerRepository::save);
    }

    public Mono<CustomerImplModel> findCustomerById(String id) {
        log.info("Finding product by ID: {}", id);
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer Not Found", Map.of("customer_id", id))));
    }

    public Flux<CustomerImplModel> findAllCustomers(int page, int size) {
        var pageablePage = Math.max(page, 1) - 1;
        var pageableSize = size > 0 && size < 20 ? size : 20;
        var message = String.format("Page %d Not Found", page);
        return customerRepository.findAll(PageRequest.of(pageablePage, pageableSize))
                .switchIfEmpty(Mono.error(new NotFoundException(message, Map.of("Page", String.valueOf(message)))));
    }

    public Mono<Void> deleteCustomerById(String id) {
        log.info("Deleting client with ID: {}", id);
        return customerRepository.deleteById(id);
    }

    public Flux<ProductImplModel> findFavoriteProducts(String id) {
        return findCustomerById(id)
                .flatMapIterable(CustomerImplModel::getFavoriteProductImplModels);
    }

    public Flux<ProductImplModel> addFavoriteProduct(String id, String productId) {
        return findCustomerById(id)
                .zipWith(customerImplFacade.findProductById(productId)
                        .switchIfEmpty(Mono.error(
                                new NotFoundException("Product Not Found", Map.of("product_id", productId)))))
                .doOnSuccess(this::validProductExists)
                .doOnSuccess(this::addProduct)
                .map(Tuple2::getT1)
                .flatMap(customerRepository::save)
                .flatMapIterable(CustomerImplModel::getFavoriteProductImplModels);
    }

    public Mono<Void> deleteFavoriteProduct(String id, String productId) {
        return findCustomerById(id)
                .flatMap(customer -> {
                    if (deleteProduct(customer, productId)) {
                        return customerRepository.save(customer).then();
                    }
                    return Mono.empty();
                });
    }

    private void validEmail(CustomerImplModel customerImplModel) {
        if (customerImplModel != null)
            throw new BadRequestException("Email exists", Map.of("email", customerImplModel.getEmail()));
    }

    private void validEmailExists(Tuple2<CustomerImplModel, CustomerImplModel> objects) {
        var customerFoundById = objects.getT2();
        var customerFoundByEmail = objects.getT1();
        if (!customerFoundById.getId().equals(customerFoundByEmail.getId()))
            throw new BadRequestException("Email exists", Map.of("email", customerFoundByEmail.getEmail()));
    }

    private boolean deleteProduct(CustomerImplModel customerImplModel, String productId) {
        var product = ProductImplModel.builder().id(productId).build();
        var products = customerImplModel.getFavoriteProductImplModels();
        if (products != null && !products.isEmpty()) {
            if (products.contains(product)) {
                return products.remove(product);
            }
        }
        return false;
    }

    private void addProduct(Tuple2<CustomerImplModel, ProductImplModel> tuple2) {
        var customer = tuple2.getT1();
        var newProduct = tuple2.getT2();
        if (customer.getFavoriteProductImplModels() == null) {
            customer.setFavoriteProductImplModels(new HashSet<>());
            customer.getFavoriteProductImplModels().add(newProduct);
        } else {
            customer.getFavoriteProductImplModels().add(newProduct);
        }

    }

    public void validProductExists(Tuple2<CustomerImplModel, ProductImplModel> tuple2) {
        var product = tuple2.getT2();
        if (product.getId() == null)
            throw new NotFoundException("Product Not Found", Map.of());
    }


}
