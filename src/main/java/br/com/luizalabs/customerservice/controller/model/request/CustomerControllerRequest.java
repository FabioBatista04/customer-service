package br.com.luizalabs.customerservice.controller.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerControllerRequest {

    @NotBlank(message = "The name field cannot be blank.")
    private String name;
    @NotBlank(message = "The name field cannot be blank.")
    @Email(message = "The email field must follow the pattern.")
    private String email;
}
