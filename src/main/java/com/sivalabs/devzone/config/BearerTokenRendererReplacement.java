package com.sivalabs.devzone.config;

import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import com.sivalabs.devzone.users.services.UserService;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerTokenRenderer;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Replaces(BearerTokenRenderer.class)
@Singleton
@RequiredArgsConstructor
public class BearerTokenRendererReplacement extends BearerTokenRenderer {
    private static final String BEARER_TOKEN_TYPE = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER;
    private final UserService userService;

    @Override
    public AccessRefreshToken render(Integer expiresIn, String accessToken, @Nullable String refreshToken) {
        return new AccessRefreshToken(accessToken, refreshToken, BEARER_TOKEN_TYPE, expiresIn);
    }

    @Override
    public AccessRefreshToken render(Authentication authentication, Integer expiresIn, String accessToken, @Nullable String refreshToken) {
        MyBearerAccessRefreshToken token = new MyBearerAccessRefreshToken(authentication.getName(), authentication.getRoles(), expiresIn, accessToken, refreshToken, BEARER_TOKEN_TYPE);
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElseThrow();
        AuthUserDTO authUserDTO = new AuthUserDTO(user.getName(), user.getEmail(), user.getRole());
        token.setUser(authUserDTO);
        return token;
    }
}
