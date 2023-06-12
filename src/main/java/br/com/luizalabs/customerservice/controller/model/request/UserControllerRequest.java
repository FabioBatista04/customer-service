package br.com.luizalabs.customerservice.controller.model.request;

import br.com.luizalabs.customerservice.impl.model.RoleUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserControllerRequest {

    @NotBlank(message = "User name cannot be blank")
    @Size(min = 2, max = 20, message = "The username must have between 2 and 20 characters.")
    private String username;
    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "The password must have a minimum of 8 characters")
    private String password;

    private List<RoleUser> roles;
}
