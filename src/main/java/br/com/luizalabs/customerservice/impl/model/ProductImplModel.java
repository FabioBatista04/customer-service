package br.com.luizalabs.customerservice.impl.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductImplModel {

    private String id;
    private String title;
    private String image;
    private double price;
    private String brand;
    private Double review;
}
