package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.model.request.CustomerControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.CustomerControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.ProductControllerResponse;
import br.com.luizalabs.customerservice.controller.stub.ProductControllerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static br.com.luizalabs.customerservice.controller.stub.CustomerControllerRequestStub.customerControllerRequestStub;
import static br.com.luizalabs.customerservice.controller.stub.CustomerControllerRequestStub.customerControllerResponseStub;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
@WebFluxTest
@ExtendWith(SpringExtension.class)
class CustomerControllerTest {

    @Autowired
    private WebTestClient webClientTest;

    @MockBean
    private ControllerFacade facade;

    private CustomerControllerRequest request;

    private CustomerControllerResponse response;


    @BeforeEach
    void setCustomer() {
        webClientTest = WebTestClient.bindToController(new CustomerController(facade))
                .configureClient()
                .build();
        request = customerControllerRequestStub();
        response = customerControllerResponseStub();
    }


    @Test
    @Disabled
    void testCreateSuccess() {


        when(facade.createCustomer(request)).thenReturn(Mono.just(response));

        webClientTest
                .post().uri("/customers")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(CustomerControllerResponse.class)
                .isEqualTo(response);
        ;
    }

    @Test
    @Disabled
    public void testCreateInvalidName() {

        request.setName(null);

        webClientTest.post().uri("/customers")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @Disabled
    public void testCreateInvalidEmail() {

        request.setEmail("invalid_email");

        webClientTest.post().uri("/customers")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @Disabled
    void testUpdateSuccess() {

        when(facade.updateCustomer("123", request)).thenReturn(Mono.just(response));

        webClientTest.put().uri("/customers/123")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerControllerResponse.class)
                .isEqualTo(response);
    }

    @Test
    @Disabled
    void testUpdateBadRequestName() {

        request.setName("");

        webClientTest.put().uri("/customers/123")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateBadRequestEmail() {

        request.setEmail("test.com.co");

        webClientTest.put().uri("/customers/123")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @Disabled
    void testFindByIdSuccess() {

        when(facade.findCustomerById("1")).thenReturn(Mono.just(response));

        webClientTest.get().uri("/customers/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerControllerResponse.class)
                .isEqualTo(response);
    }

    @Test
    @Disabled
    void findAllCustomers() {

        when(facade.findAllCustomers(0, 20)).thenReturn(Flux.just(response));

        webClientTest.get().uri("/customers")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CustomerControllerResponse.class)
                .isEqualTo(List.of(response));
    }

    @Test
    @Disabled
    void findAllCustomersWithPage() {

        when(facade.findAllCustomers(500, 20)).thenReturn(Flux.just(response));

        webClientTest.get().uri("/customers?page=500")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CustomerControllerResponse.class)
                .isEqualTo(List.of(response));
        ;
    }

    @Test
    @Disabled
    void findAllCustomersWithSize() {

        when(facade.findAllCustomers(0, 500)).thenReturn(Flux.just(response));

        webClientTest.get().uri("/customers?size=500")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CustomerControllerResponse.class)
                .isEqualTo(List.of(response));
        ;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteCustomerSuccess() {
        webClientTest.delete().uri("/customers/1")
                .exchange()
                .expectStatus()
                .isNoContent();

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void findFavoriteProductsSuccess() {

        when(facade.findAllCustomers(0, 500)).thenReturn(Flux.just(response));

        webClientTest.get().uri("/customers/1/favorites")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Disabled
    void addProductToFavoritesSuccess() {

        var product1 = ProductControllerStub.productControllerResponseStub();
        var product2 = ProductControllerStub.productControllerResponse2Stub();
        var product3 = ProductControllerStub.productControllerResponse3Stub();

        when(facade.addFavoriteProduct("1", "1")).thenReturn(Flux.just(product1, product2, product3));

        webClientTest.post().uri("/customers/1/favorites/1")
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBodyList(ProductControllerResponse.class)
                .isEqualTo(List.of(product1, product2, product3));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteFavoriteProductSSuccess() {

        when(facade.deleteFavoriteProduct("1", "1")).thenReturn(Mono.empty());

        webClientTest.delete().uri("/customers/1/favorites/1")
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}