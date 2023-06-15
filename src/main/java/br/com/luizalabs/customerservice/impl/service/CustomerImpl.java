package br.com.luizalabs.customerservice.impl.service;

import br.com.luizalabs.customerservice.controller.mapper.CustomerControllerMapper;
import br.com.luizalabs.customerservice.exeptions.GenericException;
import br.com.luizalabs.customerservice.impl.FacadeImpl;
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

import static br.com.luizalabs.customerservice.impl.model.ErrorEnum.BAD_REQUEST;
import static br.com.luizalabs.customerservice.impl.model.ErrorEnum.NOT_FOUND;

@Log4j2
@Service
@AllArgsConstructor
public class CustomerImpl {

    private final CustomerRepository customerRepository;
    private final ProductImpl productImpl;
    private final CacheCustomerImpl cacheCustomerImpl;


    public Mono<CustomerImplModel> createCustomer(CustomerImplModel customerImplModel) {
        return customerRepository.findByEmail(customerImplModel.getEmail())
                .doOnSuccess(this::validEmail)
                .switchIfEmpty(customerRepository.save(customerImplModel))
                .flatMap(customer -> cacheCustomerImpl.save(customer.getId(),customer).map(success -> customer));
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
                .flatMap(customerRepository::save)
                .flatMap(customer -> cacheCustomerImpl.save(customer.getId(),customer).map(success -> customer));
    }

    public Mono<CustomerImplModel> findCustomerById(String id) {
        log.info("Finding product by ID: {}", id);
        return cacheCustomerImpl.existsKey(id)
                        .flatMap(hasCache -> {
                            if (hasCache) return cacheCustomerImpl.get(id);
                            return customerRepository.findById(id)
                                    .flatMap(customer -> cacheCustomerImpl
                                            .save(customer.getId(),customer).map(success -> customer));
                        })
                .switchIfEmpty(Mono.error(
                        new GenericException(NOT_FOUND,"Customer Not Found", Map.of("customer_id", id))));
    }

    public Flux<CustomerImplModel> findAllCustomers(int page, int size) {
        var pageablePage = Math.max(page, 1) - 1;
        var pageableSize = size > 0 && size < 20 ? size : 20;
        var message = String.format("Page %d Not Found", page);
        return customerRepository.findAll(PageRequest.of(pageablePage, pageableSize))
                .switchIfEmpty(Mono.error(
                        new GenericException(NOT_FOUND,message, Map.of("Page", String.valueOf(message)))));
    }

    public Mono<Void> deleteCustomerById(String id) {
        log.info("Deleting client with ID: {}", id);
        return customerRepository.deleteById(id);
    }

    public Flux<ProductImplModel> findFavoriteProducts(String id) {
        return findCustomerById(id)
                .flatMapIterable(CustomerImplModel::getFavoriteProductImplModels);
    }

    public Mono<CustomerImplModel> addFavoriteProduct(String id, String productId) {
        return Mono.zip(findCustomerById(id), productImpl.findProductById(productId))
                .doOnSuccess(this::addProductToCustomer)
                .map(Tuple2::getT1)
                .flatMap(customerRepository::save)
                .flatMap(customer -> cacheCustomerImpl.save(customer.getId(),customer).map(success -> customer));
    }

    public Mono<Void> deleteFavoriteProduct(String id, String productId) {
        return findCustomerById(id)
                .flatMap(customer -> {
                    if (deleteProductFromCustomer(customer, productId)) {
                        return customerRepository.save(customer).then();
                    }
                    return Mono.empty();
                });
    }

    private void validEmail(CustomerImplModel customerImplModel) {
        if (customerImplModel != null) throw new GenericException(
                BAD_REQUEST,"Email exists", Map.of("email", customerImplModel.getEmail()));
    }

    private void validEmailExists(Tuple2<CustomerImplModel, CustomerImplModel> objects) {
        var customerFoundById = objects.getT2();
        var customerFoundByEmail = objects.getT1();
        if (!customerFoundById.getId().equals(customerFoundByEmail.getId())) throw new GenericException(
                BAD_REQUEST,"Email exists", Map.of("email", customerFoundByEmail.getEmail()));
    }

    private boolean deleteProductFromCustomer(CustomerImplModel customerImplModel, String productId) {
        var product = ProductImplModel.builder().id(productId).build();
        var products = customerImplModel.getFavoriteProductImplModels();
        if (products != null && !products.isEmpty()) {
            if (products.contains(product)) {
                return products.remove(product);
            }
        }
        return false;
    }

    private void addProductToCustomer(Tuple2<CustomerImplModel, ProductImplModel> tuple2) {
        var customer = tuple2.getT1();
        var newProduct = tuple2.getT2();
        if (customer.getFavoriteProductImplModels() == null) {
            customer.setFavoriteProductImplModels(new HashSet<>());
            customer.getFavoriteProductImplModels().add(newProduct);
        } else {
            customer.getFavoriteProductImplModels().add(newProduct);
        }

    }

}
