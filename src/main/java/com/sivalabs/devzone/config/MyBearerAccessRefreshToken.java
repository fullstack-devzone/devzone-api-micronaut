package com.sivalabs.devzone.config;

import com.sivalabs.devzone.users.models.AuthUserDTO;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

import java.util.Collection;

public class MyBearerAccessRefreshToken extends BearerAccessRefreshToken {
    private AuthUserDTO user;

    public MyBearerAccessRefreshToken(String name, Collection<String> roles, Integer expiresIn, String accessToken, String refreshToken, String bearerTokenType) {
        super(name, roles, expiresIn, accessToken, refreshToken, bearerTokenType);
    }

    public AuthUserDTO getUser() {
        return user;
    }

    public void setUser(AuthUserDTO user) {
        this.user = user;
    }
}
