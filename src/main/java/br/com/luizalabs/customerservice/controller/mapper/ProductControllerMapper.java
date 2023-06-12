package br.com.luizalabs.customerservice.controller.mapper;


import br.com.luizalabs.customerservice.controller.model.response.ProductControllerResponse;
import br.com.luizalabs.customerservice.impl.model.ProductImplModel;

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


}
