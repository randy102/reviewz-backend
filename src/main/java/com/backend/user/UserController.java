package com.backend.user;
import com.backend.security.*;

import com.backend.user.dto.CreateUserDTO;
import com.backend.user.dto.LoginDTO;
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

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO input) throws Exception {

        UserEntity existedUser = userRepository.findByUsername(input.getUsername());

        if (existedUser == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not found: User");

        String hashedPassword = HashService.hash(input.getPassword());

        if (!existedUser.getPassword().equals(hashedPassword))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect Password");

        return jwtUtil.sign(existedUser);
    }

    @PutMapping("/register")
    public UserEntity createUser(@RequestBody CreateUserDTO user) throws NoSuchAlgorithmException {
        String hashedPassword = HashService.hash(user.getPassword());
        Set<RoleEntity> roles = new HashSet<>();
        UserEntity existedUser = userRepository.findByUsername(user.getUsername());

        if(existedUser!=null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The registered name is duplicated");
        }

        if(user.isAdmin()){
            if(!currentUser.getInfo().hasRole(RoleEnum.ROLE_ADMIN))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non-administrative users");
            roles.add(new RoleEntity(RoleEnum.ROLE_ADMIN.toString()));
        }

        roles.add(new RoleEntity(RoleEnum.ROLE_USER.toString()));

        return userRepository.save(new UserEntity(user.getUsername(), hashedPassword, roles, user.getImg()));
    }
}
