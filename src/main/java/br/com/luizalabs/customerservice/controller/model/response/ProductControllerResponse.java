package br.com.luizalabs.customerservice.controller.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductControllerResponse {

    private String id;
    private String title;
    private String image;
    private Double price;
    private Double review;


}
