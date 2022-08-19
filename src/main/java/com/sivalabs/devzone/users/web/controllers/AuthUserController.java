package com.sivalabs.devzone.users.web.controllers;

import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import com.sivalabs.devzone.users.services.SecurityService;
import com.sivalabs.devzone.users.services.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller("/api")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class AuthUserController {
    private final UserService userService;

    @Get("/auth/me")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AuthUserDTO> me(Authentication authentication) {
        String email = authentication.getName();
        User loginUser = userService.getUserByEmail(email).orElse(null);
        if (loginUser != null) {
            AuthUserDTO userDTO =
                    AuthUserDTO.builder()
                            .name(loginUser.getName())
                            .email(loginUser.getEmail())
                            .role(loginUser.getRole())
                            .build();
            return HttpResponse.ok(userDTO);
        }
        return HttpResponse.status(HttpStatus.UNAUTHORIZED);
    }
}
