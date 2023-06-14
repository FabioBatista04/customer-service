package br.com.luizalabs.customerservice.impl;
import br.com.luizalabs.customerservice.impl.mapper.ProductImplMapper;
import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import br.com.luizalabs.customerservice.impl.model.ProductPageImpl;
import br.com.luizalabs.customerservice.integration.ProductIntegration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class FacadeImpl {

    private final ProductIntegration productIntegration;


    public Mono<ProductImplModel> findProductById(String id) {
        return productIntegration.findByProductId(id).map(ProductImplMapper::mapperToProductImpl);
    }

    public Mono<ProductPageImpl> findProductByPage(int page) {
        return productIntegration.findProductsByPage(page).map(ProductImplMapper::mapperToProductPageImpl);
    }
}
