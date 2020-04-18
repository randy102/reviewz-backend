package com.backend.user.dto;

import com.backend.security.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Null;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String username;
    private String password;
    private String img;
    private RoleEnum role; // ROLE_ADMIN, ROLE_USER
}
