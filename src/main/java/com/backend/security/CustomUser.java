package com.backend.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
public class CustomUser extends User {
    private String id;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public Boolean hasRole(RoleEnum role){
        Collection<GrantedAuthority> roles = this.getAuthorities();
        return roles.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.toString()));
    }

}
