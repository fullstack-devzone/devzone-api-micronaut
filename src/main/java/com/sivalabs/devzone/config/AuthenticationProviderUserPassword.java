package com.sivalabs.devzone.config;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.services.PasswordEncoder;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ServerAuthentication;
import org.reactivestreams.Publisher;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.services.UserService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Singleton
@RequiredArgsConstructor
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            String username = authenticationRequest.getIdentity().toString();
            String password = authenticationRequest.getSecret().toString();
            User user = userService.getUserByEmail(username).orElse(null);

            if (user!= null && passwordEncoder.matches(password, user.getPassword())) {
                emitter.next(AuthenticationResponse.success(username));
                emitter.complete();
            } else {
                emitter.error(AuthenticationResponse.exception());
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }
}
