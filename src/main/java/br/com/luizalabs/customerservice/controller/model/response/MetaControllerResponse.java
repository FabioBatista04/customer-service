package br.com.luizalabs.customerservice.controller.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaControllerResponse {

    private Integer pageNumber;
    private Integer pageSize;
}
