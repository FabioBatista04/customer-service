package br.com.luizalabs.customerservice.impl.stub;


import br.com.luizalabs.customerservice.impl.model.ProductImplModel;

import java.util.Set;

public class ProductImplModelStub {

    public static ProductImplModel productImplModelStub(){
        return ProductImplModel.builder()
                .id("1")
                .title("Title product")
                .price(10.0)
                .image("url_image")
                .review(1.)
                .build();
    }

    public static ProductImplModel productImplModel2Stub(){
        return ProductImplModel.builder()
                .id("2")
                .title("Title product 2")
                .price(20.0)
                .image("url_image 1")
                .review(2.)
                .build();
    }

    public static ProductImplModel productImplModel3Stub(){
        return ProductImplModel.builder()
                .id("3")
                .title("Title product 3")
                .price(30.0)
                .image("url_image 3")
                .review(3.)
                .build();
    }

    public static Set<ProductImplModel> productImplModelSetStub(){
        return Set.of(
                ProductImplModel.builder()
                        .id("1")
                        .title("Title product")
                        .price(10.0)
                        .image("url_image")
                        .review(1.)

                        .build(),
                ProductImplModel.builder()
                        .id("2")
                        .title("Title product 2")
                        .price(20.0)
                        .image("url_image 1")
                        .review(2.)
                        .build(),
                ProductImplModel.builder()
                        .id("3")
                        .title("Title product 3")
                        .price(30.0)
                        .image("url_image 3")
                        .review(3.)
                        .build()

        );
    }
}
