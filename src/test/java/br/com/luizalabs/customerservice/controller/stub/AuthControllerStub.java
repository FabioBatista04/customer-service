package br.com.luizalabs.customerservice.controller.stub;

import br.com.luizalabs.customerservice.controller.model.request.UserControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.AuthControllerResponse;

public class AuthControllerStub {

    public static AuthControllerResponse authControllerResponseStub() {
        return AuthControllerResponse.builder()
                .token("token")
                .build();
    }
    public static UserControllerRequest userControllerRequestStub() {
        return UserControllerRequest.builder()
                .username("username")
                .password("password")
                .build();
    }
}
