package br.com.luizalabs.customerservice.impl.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Builder
@Document(collection = "customers")
public class CustomerImplModel {

    @Id
    private String id;
    private String name;
    private String email;
    private Set<ProductImplModel> favoriteProductImplModels;
}
