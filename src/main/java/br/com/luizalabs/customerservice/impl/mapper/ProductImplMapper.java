package br.com.luizalabs.customerservice.impl.mapper;

import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import br.com.luizalabs.customerservice.integration.model.ProductIntegrationResponse;

import java.util.Optional;

public class ProductImplMapper {

    public static ProductImplModel mapperToProductImpl(ProductIntegrationResponse productIntegrationResponse) {
        return Optional.ofNullable(productIntegrationResponse)
                .map(product -> ProductImplModel.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .brand(product.getBrand())
                        .review(product.getReviewScore())
                        .build())
                .orElse(null);
    }
}
