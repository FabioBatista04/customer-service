package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.model.request.CustomerControllerRequest;
import br.com.luizalabs.customerservice.controller.model.request.UserControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.AuthControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.CustomerControllerResponse;
import br.com.luizalabs.customerservice.controller.stub.AuthControllerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static br.com.luizalabs.customerservice.controller.stub.CustomerControllerRequestStub.customerControllerRequestStub;
import static br.com.luizalabs.customerservice.controller.stub.CustomerControllerRequestStub.customerControllerResponseStub;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @Autowired
    private WebTestClient webClientTest;

    @MockBean
    private ControllerFacade facade;

    private UserControllerRequest request;

    private AuthControllerResponse response;


    @BeforeEach
    void setCustomer() {
        request = AuthControllerStub.userControllerRequestStub();
        response = AuthControllerStub.authControllerResponseStub();
    }

    @Test
    void testLogin() {
        when(facade.generateToken(request)).thenReturn(Mono.just(response));

        webClientTest.post()
                .uri("/users/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserControllerRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(AuthControllerResponse.class)
                .isEqualTo(response);
    }
    @Test
    void testGenerateTokenWithInvalidParam() {
        request.setUsername("");

        webClientTest.post()
                .uri("/users/create")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();

        request.setUsername("username");
        request.setPassword("");

        webClientTest.post()
                .uri("/users/create")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void testCreate() {
        when(facade.createUser(request)).thenReturn(Mono.just(response));

        webClientTest.post()
                .uri("/users/create")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserControllerRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(AuthControllerResponse.class)
                .isEqualTo(response);
    }

    @Test
    void testCreateUserWithInvalidParam() {
        request.setUsername("");

        webClientTest.post()
                .uri("/users/create")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();

        request.setUsername("username");
        request.setPassword("");

        webClientTest.post()
                .uri("/users/create")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}