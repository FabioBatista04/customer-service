package br.com.luizalabs.customerservice.impl;

import br.com.luizalabs.customerservice.authentication.JWTUtil;
import br.com.luizalabs.customerservice.exeptions.GenericException;
import br.com.luizalabs.customerservice.impl.model.RoleUser;
import br.com.luizalabs.customerservice.impl.model.UserImplModel;
import br.com.luizalabs.customerservice.impl.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static br.com.luizalabs.customerservice.impl.model.ErrorEnum.BAD_REQUEST;
import static br.com.luizalabs.customerservice.impl.model.ErrorEnum.UNAUTHORIZED;

@Log4j2
@Service
@AllArgsConstructor
public class AuthImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JWTUtil jwtUtil;

    public Mono<String> createUser(UserImplModel userImplaModel) {
        var user = UserImplModel.builder()
                .username(userImplaModel.getUsername())
                .password(encoder.encode(userImplaModel.getPassword()))
                .roleUsers(validRolesUser(userImplaModel.getRoleUsers())).build();

        return userRepository.findByUsername(user.getUsername())
                .doOnSuccess(this::usernameFoundError)
                .switchIfEmpty(userRepository.save(user))
                .map(jwtUtil::generateToken);
    }

    private void usernameFoundError(UserImplModel userImplaModel) {
        if (userImplaModel != null) throw new GenericException(
                BAD_REQUEST,"username exists", Map.of("username", "Please provide another username."));
    }
    public Mono<String> generateToken(UserImplModel userImplModel) {
        return userRepository.findByUsername(userImplModel.getUsername())
                .filter(userDetail -> encoder.matches(userImplModel.getPassword(), userDetail.getPassword()))
                .map(jwtUtil::generateToken)
                .switchIfEmpty(Mono.error(new GenericException(UNAUTHORIZED,"user unauthorized")));
    }

    private List<RoleUser> validRolesUser(List<RoleUser> roleUsers) {
        if (roleUsers != null && !roleUsers.isEmpty()) return roleUsers;
        return List.of(RoleUser.ROLE_USER);
    }
}
