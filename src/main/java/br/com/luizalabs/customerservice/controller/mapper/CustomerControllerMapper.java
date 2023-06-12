package br.com.luizalabs.customerservice.controller.mapper;

import br.com.luizalabs.customerservice.controller.model.request.CustomerControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.CustomerControllerResponse;
import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;

import static java.util.Optional.ofNullable;

@Log4j2
public class CustomerControllerMapper {

    public static CustomerControllerResponse mapToCustomerControllerResponse(CustomerImplModel customerImplModel) {
        return CustomerControllerResponse.builder()
                .id(customerImplModel.getId())
                .name(customerImplModel.getName())
                .email(customerImplModel.getEmail())
                .favoriteProducts(ProductControllerMapper
                        .mapperToSetFavoriteProducts(customerImplModel.getFavoriteProductImplModels()))
                .build();
    }

    public static CustomerImplModel mapperToCustomerImpl(CustomerControllerRequest customer) {
        return ofNullable(customer)
                .map(req -> CustomerImplModel.builder()
                        .name(req.getName())
                        .email(req.getEmail())
                        .favoriteProductImplModels(new HashSet<>())
                        .build())
                .orElse(null);

    }

    public static CustomerImplModel mapperToCustomerUpdated(CustomerImplModel old, CustomerImplModel current) {
        return ofNullable(current)
                .map(currentCustomer -> CustomerImplModel.builder()
                        .id(old.getId())
                        .name(currentCustomer.getName() != null ? currentCustomer.getName() : old.getName())
                        .email(currentCustomer.getEmail() != null ? currentCustomer.getEmail() : old.getEmail())
                        .favoriteProductImplModels(old.getFavoriteProductImplModels())
                        .build())
                .orElse(null);
    }
}
