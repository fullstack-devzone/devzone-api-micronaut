package com.sivalabs.devzone.users.web.controllers;

import com.sivalabs.devzone.users.models.CreateUserRequest;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.services.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import static io.micronaut.http.HttpStatus.CREATED;

@Controller("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @Get("/{id}")
    public HttpResponse<UserDTO> getUser(@PathVariable Long id) {
        log.info("process=get_user, user_id={}", id);
        return userService
                .getUserById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Post
    @Status(CREATED)
    public UserDTO createUser(@Body @Valid CreateUserRequest createUserRequest) {
        log.info("process=create_user, user_email={}", createUserRequest.getEmail());
        UserDTO userDTO =
                new UserDTO(
                        null,
                        createUserRequest.getName(),
                        createUserRequest.getEmail(),
                        createUserRequest.getPassword(),
                        null);
        return userService.createUser(userDTO);
    }
}
