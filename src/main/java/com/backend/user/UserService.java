package com.backend.user;

import com.backend.security.*;
import com.backend.user.dto.CreateUserDTO;
import com.backend.user.dto.LoginDTO;
import com.backend.user.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginDTO input) throws NoSuchAlgorithmException {
        UserEntity existedUser = userRepository.findByUsername(input.getUsername());

        if (existedUser == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not found: User");

        String hashedPassword = HashService.hash(input.getPassword());

        if (!existedUser.getPassword().equals(hashedPassword))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect Password");

        return jwtUtil.sign(existedUser);
    }

    public UserEntity register(RegisterDTO user) throws Exception {
        String hashedPassword = HashService.hash(user.getPassword());
        Set<RoleEntity> roles = new HashSet<>();
        UserEntity existedUser = userRepository.findByUsername(user.getUsername());

        if(existedUser != null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The username has existed");
        }

        roles.add(new RoleEntity(RoleEnum.ROLE_USER.toString()));

        return userRepository.save(new UserEntity(user.getUsername(), hashedPassword, roles, ""));
    }
}
