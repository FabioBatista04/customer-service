package br.com.luizalabs.customerservice.controller.stub;


import br.com.luizalabs.customerservice.controller.model.response.ProductControllerResponse;

import java.util.Set;

public class ProductControllerStub {

    public static ProductControllerResponse productControllerResponseStub(){
        return ProductControllerResponse.builder()
                .id("1")
                .title("Title product")
                .price(10.0)
                .image("url_image")
                .review(0.)
                .build();
    }

    public static ProductControllerResponse productControllerResponse2Stub(){
        return ProductControllerResponse.builder()
                .id("2")
                .title("Title product 2")
                .price(20.0)
                .image("url_image 1")
                .review(00.)
                .build();
    }

    public static ProductControllerResponse productControllerResponse3Stub(){
        return ProductControllerResponse.builder()
                .id("3")
                .title("Title product 3")
                .price(30.0)
                .image("url_image 3")
                .review(3.)
                .build();
    }

    public static Set<ProductControllerResponse> productControllerResponseSetStub(){
        return Set.of(
                ProductControllerResponse.builder()
                        .id("1")
                        .title("Title product")
                        .price(10.0)
                        .image("url_image")
                        .review(1.)

                        .build(),
                ProductControllerResponse.builder()
                        .id("2")
                        .title("Title product 2")
                        .price(20.0)
                        .image("url_image 1")
                        .review(2.)
                        .build(),
                ProductControllerResponse.builder()
                        .id("3")
                        .title("Title product 3")
                        .price(30.0)
                        .image("url_image 3")
                        .review(3.)
                        .build()

        );
    }
}
