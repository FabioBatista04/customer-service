package br.com.luizalabs.customerservice.impl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaImplModel {

    private Integer pageNumber;
    private Integer pageSize;
}
