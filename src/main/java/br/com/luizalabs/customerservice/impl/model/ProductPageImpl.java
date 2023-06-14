package br.com.luizalabs.customerservice.impl.model;

import br.com.luizalabs.customerservice.integration.model.ProductIntegrationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageImpl {

    private MetaImplModel meta;
    private List<ProductImplModel> products;

}
