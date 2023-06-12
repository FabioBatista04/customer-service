package br.com.luizalabs.customerservice.integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductIntegrationResponse {

    private String id;
    private String title;
    private double price;
    private String image;
    private String brand;
    private Double reviewScore;
}
