package com.backend.user;
import com.backend.security.*;

import com.backend.user.dto.CreateUserDTO;
import com.backend.user.dto.LoginDTO;
import com.backend.user.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO input) throws Exception {
        return userService.login(input);
    }

    @PutMapping("/register")
    public UserEntity createUser(@RequestBody RegisterDTO user) throws Exception {
        return userService.register(user);
    }
}
