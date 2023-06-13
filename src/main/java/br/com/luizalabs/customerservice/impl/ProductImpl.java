package br.com.luizalabs.customerservice.impl;

import br.com.luizalabs.customerservice.impl.model.ProductPageImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductImpl {

    private FacadeImpl facade;


    public Mono<ProductPageImpl> getProductsByPage(int page){
        return facade.findProductByPage(page);
    }
}
