package com.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

interface ICurrentUser {
    Authentication getAuthentication();
}

@Component
public class CurrentUser implements ICurrentUser {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public CustomUser getInfo(){
        return (CustomUser) getAuthentication().getPrincipal();
    }
}