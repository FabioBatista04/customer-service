package br.com.luizalabs.customerservice.controller.stub;

import br.com.luizalabs.customerservice.controller.model.response.FavoriteProductsControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.ProductControllerResponse;

import java.util.Set;

public class FavoriteProductsControllerStub {
    public static FavoriteProductsControllerResponse favoriteProductsControllerResponseStub(
            Set<ProductControllerResponse> products
    ) {
        return FavoriteProductsControllerResponse.builder()
                .products(products)
                .build();
    }
}
