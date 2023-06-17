package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.model.request.CustomerControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.CustomerControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.FavoriteProductsControllerResponse;
import br.com.luizalabs.customerservice.controller.model.response.ProductControllerResponse;
import br.com.luizalabs.customerservice.controller.stub.CustomerControllerRequestStub;
import br.com.luizalabs.customerservice.controller.stub.FavoriteProductsControllerStub;
import br.com.luizalabs.customerservice.controller.stub.ProductControllerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static br.com.luizalabs.customerservice.controller.stub.CustomerControllerRequestStub.customerControllerRequestStub;
import static br.com.luizalabs.customerservice.controller.stub.CustomerControllerRequestStub.customerControllerResponseStub;
import static br.com.luizalabs.customerservice.controller.stub.FavoriteProductsControllerStub.favoriteProductsControllerResponseStub;
import static br.com.luizalabs.customerservice.controller.stub.ProductControllerStub.productControllerResponseStub;
import static org.mockito.Mockito.when;
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {


    @Autowired
    private WebTestClient webClientTest;

    @MockBean
    private ControllerFacade facade;

    private CustomerControllerRequest request;

    private CustomerControllerResponse response;


    @BeforeEach
    void setCustomer() {
        request = customerControllerRequestStub();
        response = customerControllerResponseStub();
    }

    @Test
    @WithMockUser( roles = "USER")
    void testCreateSuccess() {
        when(facade.createCustomer(request)).thenReturn(Mono.just(response));

        webClientTest.post()
                .uri("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(CustomerControllerResponse.class)
                .isEqualTo(response);
    }

    @Test
    @WithMockUser( roles = "USER")
    void testFindFavoriteProducts(){
        var products = Set.of(productControllerResponseStub());
        var favoriteProducts = favoriteProductsControllerResponseStub(products);
        when(facade.findFavoriteProducts("1")).thenReturn(Mono.just(favoriteProducts));

        webClientTest.get()
                .uri("/customers/1/favorites")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(FavoriteProductsControllerResponse.class)
                .isEqualTo(favoriteProducts);
    }
    @Test
    void testCreateUnauthorized() {
        when(facade.createCustomer(request)).thenReturn(Mono.just(response));

        webClientTest.post()
                .uri("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser( roles = "USER")
    public void testCreateInvalidName() {

        request.setName(null);

        webClientTest.post().uri("/customers")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @WithMockUser( roles = "USER")
    public void testCreateInvalidEmail() {

        request.setEmail("invalid_email");

        webClientTest.post().uri("/customers")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @WithMockUser( roles = "USER")
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
    @WithMockUser( roles = "USER")
    void testUpdateBadRequestName() {

        request.setName("");

        webClientTest.put().uri("/customers/123")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateBadRequestEmail() {

        request.setEmail("test.com.co");

        webClientTest.put().uri("/customers/123")
                .body(Mono.just(request), CustomerControllerRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @WithMockUser( roles = "USER")
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
    @WithMockUser( roles = "USER")
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
    @WithMockUser( roles = "USER")
    void findAllCustomersWithPage() {

        when(facade.findAllCustomers(500, 20)).thenReturn(Flux.just(response));

        webClientTest.get().uri("/customers?page=500")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CustomerControllerResponse.class)
                .isEqualTo(List.of(response));
    }

    @Test
    @WithMockUser( roles = "USER")
    void findAllCustomersWithSize() {

        when(facade.findAllCustomers(0, 500)).thenReturn(Flux.just(response));

        webClientTest.get().uri("/customers?size=500")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CustomerControllerResponse.class)
                .isEqualTo(List.of(response));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteCustomerDenied() {
        webClientTest.delete().uri("/customers/1")
                .exchange()
                .expectStatus()
                .isForbidden();

    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCustomerSuccess() {
        webClientTest.delete().uri("/customers/1")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @WithMockUser(roles = "USER")
    void findFavoriteProductsSuccess() {

        when(facade.findAllCustomers(0, 500)).thenReturn(Flux.just(response));

        webClientTest.get().uri("/customers/1/favorites")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @WithMockUser(roles = "USER")
    void addProductToFavoritesSuccess() {
        var customer = CustomerControllerRequestStub.customerControllerResponseStub();
        var product1 = productControllerResponseStub();
        var product2 = ProductControllerStub.productControllerResponse2Stub();
        var product3 = ProductControllerStub.productControllerResponse3Stub();

        customer.setFavoriteProducts(Set.of(product1,product2,product3));

        when(facade.addFavoriteProduct("1", "1")).thenReturn(Mono.just(customer));

        webClientTest.post().uri("/customers/1/favorites/1")
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(CustomerControllerResponse.class)
                .isEqualTo(customer);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFavoriteProductSSuccess() {

        when(facade.deleteFavoriteProduct("1", "1")).thenReturn(Mono.empty());

        webClientTest.delete().uri("/customers/1/favorites/1")
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}