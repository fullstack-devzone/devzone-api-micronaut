package com.sivalabs.devzone.users.services;

import com.sivalabs.devzone.users.entities.RoleEnum;
import com.sivalabs.devzone.users.entities.User;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.Arrays;

@Singleton
@Transactional
@RequiredArgsConstructor
public class SecurityService {
    private final UserService userService;

    public User loginUser() {
        User loginUser = new User();
        loginUser.setId(1L);
        return loginUser;
    }

    public boolean isCurrentUserAdmin() {
        return isUserHasAnyRole(loginUser(), RoleEnum.ROLE_ADMIN);
    }

    private boolean isUserHasAnyRole(User loginUser, RoleEnum... roles) {
        return Arrays.asList(roles).contains(loginUser.getRole());
    }
}
