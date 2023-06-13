package br.com.luizalabs.customerservice.integration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaIntegrationResponse {

    private Integer pageNumber;
    private Integer pageSize;
}
