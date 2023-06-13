package br.com.luizalabs.customerservice.integration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("page_number")
    private Integer pageNumber;
    @JsonProperty("page_size")
    private Integer pageSize;
}
