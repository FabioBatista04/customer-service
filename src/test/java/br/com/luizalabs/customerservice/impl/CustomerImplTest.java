package br.com.luizalabs.customerservice.impl;

import br.com.luizalabs.customerservice.exeptions.GenericException;
import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import br.com.luizalabs.customerservice.impl.repository.CustomerRepository;
import br.com.luizalabs.customerservice.impl.stub.CustomerImplModelStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static br.com.luizalabs.customerservice.impl.stub.CustomerImplModelStub.customerImplModelStub;
import static br.com.luizalabs.customerservice.impl.stub.ProductImplModelStub.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FacadeImpl customerFacadeImpl;

    @InjectMocks
    private CustomerImpl customerImpl;


    @BeforeEach
    void testSetUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomerValidCustomerReturnsSavedCustomer() {
        CustomerImplModel customer = customerImplModelStub();

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Mono.empty());
        when(customerRepository.save(any(CustomerImplModel.class))).thenReturn(Mono.just(customer));

        StepVerifier.create(customerImpl.createCustomer(customer)).expectNext(customer).verifyComplete();
    }

    @Test
    void testCreateCustomerDuplicateEmailThrowsBadRequestException() {
        CustomerImplModel customer = customerImplModelStub();

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Mono.just(customer));
        when(customerRepository.save(any(CustomerImplModel.class))).thenReturn(Mono.just(customer));

        StepVerifier.create(customerImpl.createCustomer(customer)).expectError(GenericException.class).verify();
    }

    @Test
    void testUpdateCustomerWithEmailReturnsCustomerUpdated() {
        CustomerImplModel customer = customerImplModelStub();
        CustomerImplModel customerNew = customerImplModelStub();
        customerNew.setEmail("novo@email.com.br");
        customerNew.setName("novo nome");

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Mono.empty());
        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));

        when(customerRepository.save(any(CustomerImplModel.class))).thenReturn(Mono.just(customerNew));

        StepVerifier.create(customerImpl.updateCustomer(customer.getId(), customer)).expectNext(customerNew).verifyComplete();
    }

    @Test
    void testUpdateCustomerWithEmailReturnBadRequestEmailExists() {
        CustomerImplModel customer = customerImplModelStub();
        CustomerImplModel customerNew = CustomerImplModelStub.customerImplModelNewStub();

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Mono.just(customerNew));
        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));

        StepVerifier.create(customerImpl.updateCustomer(customer.getId(), customer)).expectError(GenericException.class).verify();
    }

    @Test
    void testUpdateCustomerWithEmailReturnBadRequestIdNotExists() {
        CustomerImplModel customer = customerImplModelStub();

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Mono.empty());
        when(customerRepository.findById(customer.getId())).thenReturn(Mono.empty());

        StepVerifier.create(customerImpl.updateCustomer(customer.getId(), customer)).expectError(GenericException.class).verify();
    }

    @Test
    void testFindCustomerByIdReturnSuccess() {
        CustomerImplModel customer = customerImplModelStub();

        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));

        StepVerifier.create(customerImpl.findCustomerById(customer.getId())).expectNext(customer).verifyComplete();
    }

    @Test
    void testFindCustomerByIdReturnNotFoundException() {
        CustomerImplModel customer = customerImplModelStub();

        when(customerRepository.findById(customer.getId())).thenReturn(Mono.empty());

        StepVerifier.create(customerImpl.findCustomerById(customer.getId())).expectError(GenericException.class).verify();
    }

    @Test
    void testFindAllCustomerReturnSuccess() {
        var customer = customerImplModelStub();

        var pageable = PageRequest.of(0, 1);

        when(customerRepository.findAll(pageable)).thenReturn(Flux.just(customer));

        StepVerifier.create(customerImpl.findAllCustomers(1, 1)).expectNext(customer).verifyComplete();
    }

    @Test
    void testFindAllCustomerReturnSuccessWith800() {
        var customer = customerImplModelStub();

        var pageable = PageRequest.of(0, 20);

        when(customerRepository.findAll(pageable)).thenReturn(Flux.just(customer));

        StepVerifier.create(customerImpl.findAllCustomers(1, 800)).expectNext(customer).verifyComplete();
    }

    @Test
    void testDeleteCustomerReturnSuccess() {
        when(customerRepository.deleteById("123")).thenReturn(Mono.empty());
        StepVerifier.create(customerImpl.deleteCustomerById("123")).verifyComplete();
    }

    @Test
    @Disabled
    void testFindFavoriteProductsReturnSuccess() {
        var customer = customerImplModelStub();
        var products = productImplModelSetStub();
        customer.setFavoriteProductImplModels(products);

        var product = productImplModelStub();
        var product2 = productImplModel2Stub();
        var product3 = productImplModel3Stub();


        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));

        StepVerifier.create(customerImpl.findFavoriteProducts(customer.getId()))
                .expectNext(product, product2,product3)
                .verifyComplete();
    }

    @Test
    void testFindFavoriteProductsReturnSetEmpty() {
        var customer = customerImplModelStub();

        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));

        StepVerifier.create(customerImpl.findFavoriteProducts(customer.getId())).verifyComplete();
    }

    @Test
    void testAddFavoriteProductReturnSuccess() {
        var customer = customerImplModelStub();
        var product = productImplModelStub();
        var product3 = productImplModel3Stub();
        var newCustomer = customerImplModelStub();


        customer.getFavoriteProductImplModels().add(product);

        newCustomer.getFavoriteProductImplModels().add(product);
        newCustomer.getFavoriteProductImplModels().add(product3);

        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));
        when(customerFacadeImpl.findProductById(product3.getId())).thenReturn(Mono.just(product3));
        when(customerRepository.save(newCustomer)).thenReturn(Mono.just(newCustomer));

        StepVerifier.create(customerImpl.addFavoriteProduct(customer.getId(), product3.getId()))
                .expectNext(product,product3)
                .verifyComplete();
    }

    @Test
    void testAddFavoriteProductReturnNotFound() {
        var customer = customerImplModelStub();
        var product3 = productImplModel3Stub();

        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));
        when(customerFacadeImpl.findProductById(product3.getId())).thenReturn(Mono.empty());

        StepVerifier.create(customerImpl.addFavoriteProduct(customer.getId(), product3.getId()))
                .expectError(GenericException.class)
                .verify();
    }

    @Test
    void testAddFavoriteProductReturnWithFieldsNull() {
        var customer = customerImplModelStub();
        var product3 = productImplModel3Stub();
        var productFieldsNull = productImplModelStub();

        productFieldsNull.setId(null);
        productFieldsNull.setTitle(null);
        productFieldsNull.setBrand(null);
        productFieldsNull.setImage(null);

        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));
        when(customerFacadeImpl.findProductById(product3.getId())).thenReturn(Mono.just(productFieldsNull));

        StepVerifier.create(customerImpl.addFavoriteProduct(customer.getId(), product3.getId()))
                .expectError(GenericException.class)
                .verify();
    }

    @Test
    void testDeleteFavoriteProductReturnNoContent() {
        var customer = customerImplModelStub();

        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));

        StepVerifier.create(customerImpl.deleteFavoriteProduct(customer.getId(), "123"))
                .verifyComplete();
    }

    @Test
    void testDeleteFavoriteProductReturnNotFoundException() {
        var customer = customerImplModelStub();

        when(customerRepository.findById(customer.getId())).thenReturn(Mono.empty());

        StepVerifier.create(customerImpl.deleteFavoriteProduct(customer.getId(), "123"))
                .expectError(GenericException.class)
                .verify();
    }

}
