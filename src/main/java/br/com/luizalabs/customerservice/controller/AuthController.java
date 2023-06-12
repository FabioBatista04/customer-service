package br.com.luizalabs.customerservice.controller;

import br.com.luizalabs.customerservice.controller.model.request.UserControllerRequest;
import br.com.luizalabs.customerservice.controller.model.response.AuthControllerResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import static org.springframework.http.HttpStatus.CREATED;

@Log4j2
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class AuthController {

    private ControllerFacade facade;



    @PostMapping("/login")
    @ResponseStatus(CREATED)
    public Mono<AuthControllerResponse> login(@Valid @RequestBody UserControllerRequest request) {
        log.info("aaa");
        return  facade.generateToken(request);

    }

    @PostMapping("/create")
    @ResponseStatus(CREATED)
    public Mono<AuthControllerResponse> create(@Valid @RequestBody UserControllerRequest request) {
        return facade.createUser(request);
    }


}
