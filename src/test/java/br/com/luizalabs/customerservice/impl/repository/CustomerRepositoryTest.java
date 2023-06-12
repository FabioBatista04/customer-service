package br.com.luizalabs.customerservice.impl.repository;

import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    @Disabled
    void TestFindByEmail() {

        var customer = CustomerImplModel.builder()
                .name("Name Test")
                .email("email@test.com.br")
                .build();
        Publisher<CustomerImplModel> setup = repository.deleteAll().thenMany(repository.save(customer));
        Mono<CustomerImplModel> find = repository.findByEmail(customer.getEmail());
        Publisher<CustomerImplModel> composite = Mono.from(setup).then(find);
        StepVerifier
                .create(composite)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Disabled
    void testFindByEmailNotFound() {

        var customer = CustomerImplModel.builder()
                .name("Name Test")
                .email("email@test.com.br")
                .build();
        Publisher<CustomerImplModel> setup = repository.deleteAll().thenMany(repository.save(customer));
        Mono<CustomerImplModel> find = repository.findByEmail("email");
        Publisher<CustomerImplModel> composite = Mono.from(setup).then(find);
        StepVerifier
                .create(composite)
                .expectNextCount(0)
                .verifyComplete();
    }


    @Test
    @Disabled
    void findAll() {
        var customer = CustomerImplModel.builder()
                .name("Name Test")
                .email("email@test.com.br")
                .build();
        var customer2 = CustomerImplModel.builder()
                .name("Name2 Test")
                .email("email2@test.com.br")
                .build();
        Publisher<CustomerImplModel> setup = repository.deleteAll().thenMany(repository.save(customer))
                .thenMany(repository.save(customer2));
        Flux<CustomerImplModel> find = repository.findAll(PageRequest.of(0, 5));
        Publisher<CustomerImplModel> composite = Flux.from(setup).thenMany(find);

        StepVerifier
                .create(composite)
                .expectNextCount(2)
                .verifyComplete();

    }
}