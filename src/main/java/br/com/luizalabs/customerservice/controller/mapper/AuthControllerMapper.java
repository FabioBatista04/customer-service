package br.com.luizalabs.customerservice.controller.mapper;

import br.com.luizalabs.customerservice.controller.model.request.UserControllerRequest;
import br.com.luizalabs.customerservice.impl.model.UserImplModel;

import java.util.Optional;

public class AuthControllerMapper {

    public static UserImplModel mapperToUserImplModel(UserControllerRequest userRequest){
        return Optional.ofNullable(userRequest)
                .map(user -> UserImplModel.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roleUsers(user.getRoles())
                        .build())
                .orElse(null);
    }

}
