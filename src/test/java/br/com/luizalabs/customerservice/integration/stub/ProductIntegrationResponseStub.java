package br.com.luizalabs.customerservice.integration.stub;


import br.com.luizalabs.customerservice.integration.model.ProductIntegrationResponse;

public class ProductIntegrationResponseStub {

    public static ProductIntegrationResponse productIntegrationResponseStub(){
        return ProductIntegrationResponse.builder()
                .id("123")
                .title("Title Product")
                .image("url_image")
                .price(10.0)
                .brand("Brand")
                .reviewScore(0.)
                .build();
    }
}
