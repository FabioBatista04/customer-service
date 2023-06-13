package br.com.luizalabs.customerservice.impl;

import br.com.luizalabs.customerservice.impl.model.ProductPageImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductImpl {

    private FacadeImpl facade;


    public Mono<ProductPageImpl> getProductsByPage(int page){
        return facade.findProductByPage(page);
    }
}
