package br.com.luizalabs.customerservice.impl.stub;
import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;

import java.util.HashSet;

public class CustomerImplModelStub {

    public static CustomerImplModel customerImplModelStub(){
        return CustomerImplModel.builder()
                .id("123")
                .name("Name")
                .email("test@test.com.br")
                .favoriteProductImplModels(new HashSet<>())
                .build();
    }
    public static CustomerImplModel customerImplModelNewStub(){
        return CustomerImplModel.builder()
                .id("456")
                .name("Name 2")
                .email("test2@test.com.br")
                .favoriteProductImplModels(new HashSet<>())
                .build();

    }



}
