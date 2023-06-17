package br.com.luizalabs.customerservice.impl.service;

import br.com.luizalabs.customerservice.controller.mapper.CustomerControllerMapper;
import br.com.luizalabs.customerservice.exeptions.GenericException;
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
                .flatMap(this::saveCustomerInCache);
    }

    public Mono<CustomerImplModel> updateCustomer(String customerId, CustomerImplModel customerImplModel) {

        return customerRepository.findByEmail(customerImplModel.getEmail())
                .switchIfEmpty(processCustomerUpdate(customerId, customerImplModel))
                .zipWith(findCustomerById(customerId))
                .doOnSuccess(this::validEmailExists)
                .flatMap(this::updateAndSaveCustomer)
                .flatMap(this::saveCustomerInCache);
    }

    public Mono<CustomerImplModel> findCustomerById(String customerId) {
        log.info("Finding product by ID: {}", customerId);
        return cacheCustomerImpl.existsKey(customerId)
                .flatMap(hasCache -> getByCacheOrRepository(hasCache, customerId))
                .switchIfEmpty(Mono.error(
                        new GenericException(NOT_FOUND, "Customer Not Found", Map.of("customer_id", customerId))));
    }


    public Flux<CustomerImplModel> findAllCustomers(int page, int size) {
        var pageable = createPageable(page, size);
        return customerRepository.findAll(pageable)
                .switchIfEmpty(Mono.error(
                        new GenericException(
                                NOT_FOUND, "Page " + page + " Not Found", Map.of("Page", String.valueOf(page)))));
    }

    public Mono<Void> deleteCustomerById(String id) {
        log.info("Deleting client with ID: {}", id);
        return customerRepository.deleteById(id)
                .then(cacheCustomerImpl.removeAndEmpty(id));
    }

    public Flux<ProductImplModel> findFavoriteProducts(String customerId) {
        return findCustomerById(customerId)
                .flatMapIterable(CustomerImplModel::getFavoriteProductImplModels);
    }

    public Mono<CustomerImplModel> addFavoriteProduct(String customerId, String productId) {
        return findCustomerAndProduct(customerId, productId)
                .doOnSuccess(this::addProductToCustomer)
                .flatMap(this::saveCustomer)
                .flatMap(this::saveCustomerInCache);
    }

    public Mono<Void> deleteFavoriteProduct(String customerId, String productId) {
        return findCustomerById(customerId)
                .doOnSuccess(customerFound -> deleteProductFromCustomer(customerFound, productId))
                .flatMap(customerRepository::save)
                .map(this::saveCustomerInCache)
                .then();
    }


    private Mono<CustomerImplModel> updateAndSaveCustomer(Tuple2<CustomerImplModel, CustomerImplModel> tuple) {
        var customerDataBase = tuple.getT1();
        var updatedCustomer = CustomerControllerMapper.mapperToCustomerUpdated(customerDataBase, tuple.getT2());
        return customerRepository.save(updatedCustomer);
    }

    private Mono<CustomerImplModel> processCustomerUpdate(String id, CustomerImplModel customerImplModel) {
        return findCustomerById(id)
                .map(old -> CustomerControllerMapper.mapperToCustomerUpdated(old, customerImplModel))
                .flatMap(customerRepository::save)
                .flatMap(this::saveCustomerInCache);
    }

    private Mono<Tuple2<CustomerImplModel, ProductImplModel>> findCustomerAndProduct(String customerId, String productId) {
        var customer = findCustomerById(customerId);
        var product = productImpl.findProductById(productId);
        return Mono.zip(customer, product);
    }

    private void validEmail(CustomerImplModel customerImplModel) {
        if (customerImplModel != null) throw new GenericException(
                BAD_REQUEST, "Email exists", Map.of("email", customerImplModel.getEmail()));
    }

    private void validEmailExists(Tuple2<CustomerImplModel, CustomerImplModel> objects) {
        var customerFoundById = objects.getT2();
        var customerFoundByEmail = objects.getT1();
        if (!customerFoundById.getId().equals(customerFoundByEmail.getId())) throw new GenericException(
                BAD_REQUEST, "Email exists", Map.of("email", customerFoundByEmail.getEmail()));
    }

    private void deleteProductFromCustomer(CustomerImplModel customerImplModel, String productId) {
        var product = ProductImplModel.builder().id(productId).build();
        var products = customerImplModel.getFavoriteProductImplModels();
        if (products != null && !products.isEmpty()) {
            products.remove(product);
        }
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

    private Mono<CustomerImplModel> saveCustomer(Tuple2<CustomerImplModel, ProductImplModel> tuple) {
        var customer = tuple.getT1();
        return customerRepository.save(customer);
    }

    private Mono<CustomerImplModel> saveCustomerInCache(CustomerImplModel customer) {
        return cacheCustomerImpl.saveAndReturn(customer.getId(), customer);
    }

    private Mono<CustomerImplModel> getByCacheOrRepository(Boolean hasCache, String customerId) {
        if (hasCache) return cacheCustomerImpl.get(customerId);
        log.info("Customer not found in cache, searching in database");
        return customerRepository.findById(customerId)
                .flatMap(customer -> cacheCustomerImpl
                        .saveAndReturn(customer.getId(), customer));
    }

    private PageRequest createPageable(int page, int size) {
        var pageablePage = Math.max(page, 1) - 1;
        var pageableSize = size > 0 && size < 20 ? size : 20;
        return PageRequest.of(pageablePage, pageableSize);
    }

}
