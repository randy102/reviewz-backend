package com.backend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity implements GrantedAuthority {
    private String role;

    public RoleEntity(RoleEnum roleEnum){
        this.role = roleEnum.value;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
