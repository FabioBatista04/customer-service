package br.com.luizalabs.customerservice.controller.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerControllerResponse {

    private String id;
    private String name;
    private String email;
    private Set<ProductControllerResponse> favoriteProducts;
}
