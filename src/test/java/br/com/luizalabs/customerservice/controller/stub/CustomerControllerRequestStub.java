package br.com.luizalabs.customerservice.controller.stub;

import br.com.luizalabs.customerservice.controller.model.request.CustomerControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.CustomerControllerResponse;

public class CustomerControllerRequestStub {


    public static CustomerControllerRequest customerControllerRequestStub(){
        var request = new CustomerControllerRequest();
        request.setName("Name");
        request.setEmail("test1@test.com.br");
        return request;

    }

    public static CustomerControllerResponse customerControllerResponseStub(){
        return CustomerControllerResponse.builder()
                .id("123")
                .name("Name")
                .email("test1@email.com.br")
                .build();

    }

}
