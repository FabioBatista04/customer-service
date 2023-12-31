package br.com.luizalabs.customerservice.impl;

import br.com.luizalabs.customerservice.exeptions.GenericException;
import br.com.luizalabs.customerservice.impl.model.ErrorEnum;
import br.com.luizalabs.customerservice.impl.model.ProductPageImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@AllArgsConstructor
public class ProductImpl {

    private FacadeImpl facade;


    public Mono<ProductPageImpl> getProductsByPage(String page){
        return Mono.just(page)
                .filter(p -> p.matches("^(?=.*\\d)[\\d0]+$") )
                .map(Integer::parseInt)
                .switchIfEmpty(Mono.error(new GenericException(
                        ErrorEnum.BAD_REQUEST,
                        "invalid param",
                        Map.of("page", "The value must be greater than zero."))))
                .flatMap(pag -> facade.findProductByPage(pag));
    }
}
