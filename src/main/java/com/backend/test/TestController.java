package com.backend.test;

import com.backend.security.CurrentUser;
import com.backend.security.CustomUser;
import com.backend.security.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    CurrentUser currentUser; //Get current logged in user

    @GetMapping("")
    @Secured("ROLE_USER") //Only for who has ROLE_USER
    public String hello(){
        Boolean hasRoleUser = currentUser.getInfo().hasRole(RoleEnum.ROLE_USER); // Check role
        String username = currentUser.getInfo().getUsername(); // Get user name
        String id = currentUser.getInfo().getId(); //Get user ID

        return "Hello user: " + username + " id: " + id + " has role USER: " + hasRoleUser;
    }
}
