package br.com.luizalabs.customerservice.impl.service;

import br.com.luizalabs.customerservice.exeptions.GenericException;
import br.com.luizalabs.customerservice.impl.FacadeImpl;
import br.com.luizalabs.customerservice.impl.model.CustomerImplModel;
import br.com.luizalabs.customerservice.impl.model.ErrorEnum;
import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import br.com.luizalabs.customerservice.impl.model.ProductPageImpl;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;

import static br.com.luizalabs.customerservice.impl.model.ErrorEnum.NOT_FOUND;
@Log4j2
@Service
@AllArgsConstructor
public class ProductImpl {

    private FacadeImpl facade;
    private final CacheProductImpl cacheProductImpl;


    public Mono<ProductImplModel> findProductById(String id){
        return cacheProductImpl.existsKey(id)
                .flatMap(hasCache -> {
                    log.info("Finding product by ID: {}, hasCached: {}", id, hasCache);
                    if( hasCache )   return cacheProductImpl.get(id);
                    return facade.findProductById(id)
                            .doOnSuccess(this::validProductExists)
                            .flatMap(cacheProductImpl::saveAndReturn)
                            .switchIfEmpty(Mono.error(new GenericException(
                                    NOT_FOUND,"Product Not Found", Map.of("product_id", id))));
                });
    }
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

    public void validProductExists(ProductImplModel product) {
        if (product.getId() == null)
            throw new GenericException(NOT_FOUND,"Product Not Found", Map.of());
    }
}
