package br.com.luizalabs.customerservice.controller.model.response;

import br.com.luizalabs.customerservice.impl.model.ProductImplModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageControllerResponse {

    private MetaControllerResponse meta;
    private List<ProductControllerResponse> products;

}
