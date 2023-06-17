package br.com.luizalabs.customerservice.controller.mapper;


import br.com.luizalabs.customerservice.controller.model.response.FavoriteProductsControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.MetaControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.ProductControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.ProductPageControllerResponse;
import br.com.luizalabs.customerservice.impl.mapper.ProductImplMapper;
import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import br.com.luizalabs.customerservice.impl.model.MetaImplModel;
import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import br.com.luizalabs.customerservice.impl.model.ProductPageImpl;
import br.com.luizalabs.customerservice.integration.model.MetaIntegrationResponse;
import br.com.luizalabs.customerservice.integration.model.ProductIntegrationResponse;
import br.com.luizalabs.customerservice.integration.model.ProductPageIntegrationResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductControllerMapper {

    public static ProductControllerResponse mapFavoriteProductsTo(ProductImplModel productImplModel) {

        return Optional.ofNullable(productImplModel)
                .map(product -> ProductControllerResponse.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .review(product.getReview())
                        .build())
                .orElse(null);
    }

    public static Set<ProductControllerResponse> mapperToSetFavoriteProducts(Set<ProductImplModel> favoriteProductImplModels) {
        return Optional.ofNullable(favoriteProductImplModels)
                .map(set -> set.stream()
                        .map(ProductControllerMapper::mapFavoriteProductsTo)
                        .collect(Collectors.toSet())
                ).orElse(null);
    }


    public static ProductPageControllerResponse mapperToProductPageImpl(ProductPageImpl productIntegrationResponse) {
        return Optional.ofNullable(productIntegrationResponse)
                .map(product -> ProductPageControllerResponse.builder()
                        .meta(mapperToMeta(product.getMeta()))
                        .products(mapperProductList(product.getProducts()))
                        .build())
                .orElse(null);

    }

    private static MetaControllerResponse mapperToMeta(MetaImplModel meta) {
        return Optional.ofNullable(meta)
                .map( m -> MetaControllerResponse.builder()
                        .pageSize(m.getPageSize())
                        .pageNumber(m.getPageNumber())
                        .build())
                .orElse(null);
    }

    private static List<ProductControllerResponse> mapperProductList(List<ProductImplModel> products){
        return Optional.ofNullable(products)
                .map(p -> p.stream()
                        .map(ProductControllerMapper::mapFavoriteProductsTo)
                        .collect(Collectors.toList()))
                .orElse(null);
    }


    public static FavoriteProductsControllerResponse mapperToFavoriteProducts(CustomerImplModel customerImplModel) {
        return Optional.ofNullable(customerImplModel)
                .map(product -> FavoriteProductsControllerResponse.builder()
                        .products(mapperToSetFavoriteProducts(product.getFavoriteProductImplModels()))
                        .build())
                .orElse(null);
    }
}
