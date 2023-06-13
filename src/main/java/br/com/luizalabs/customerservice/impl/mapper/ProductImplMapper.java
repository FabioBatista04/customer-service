package br.com.luizalabs.customerservice.impl.mapper;

import br.com.luizalabs.customerservice.impl.model.MetaImplModel;
import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import br.com.luizalabs.customerservice.impl.model.ProductPageImpl;
import br.com.luizalabs.customerservice.integration.model.MetaIntegrationResponse;
import br.com.luizalabs.customerservice.integration.model.ProductIntegrationResponse;
import br.com.luizalabs.customerservice.integration.model.ProductPageIntegrationResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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



    public static ProductPageImpl mapperToProductPageImpl(ProductPageIntegrationResponse productIntegrationResponse) {
        return Optional.ofNullable(productIntegrationResponse)
                .map(product -> ProductPageImpl.builder()
                        .meta(mapperToMeta(product.getMeta()))
                        .products(mapperProductList(product.getProducts()))
                        .build())
                .orElse(null);

    }

    private static MetaImplModel mapperToMeta(MetaIntegrationResponse meta) {
        return Optional.ofNullable(meta)
                .map( m -> MetaImplModel.builder()
                        .pageSize(m.getPageSize())
                        .pageNumber(m.getPageNumber())
                        .build())
                .orElse(null);
    }

    private static List<ProductImplModel> mapperProductList(List<ProductIntegrationResponse> products){
        return Optional.ofNullable(products)
                .map(p -> p.stream()
                        .map(ProductImplMapper::mapperToProductImpl)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

}
