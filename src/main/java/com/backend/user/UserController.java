package com.backend.user;
import com.backend.RouteConfig;

import com.backend.user.dto.LoginDTO;
import com.backend.user.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = RouteConfig.USER_BASE)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO input) throws Exception {
        return userService.login(input);
    }

    @PostMapping("/register")
    public UserEntity createUser(@RequestBody RegisterDTO user) throws Exception {
        return userService.register(user);
    }
}
