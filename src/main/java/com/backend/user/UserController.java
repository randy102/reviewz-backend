package com.backend.user;
import com.backend.image.ImageEntity;
import com.backend.image.ImageRepository;
import com.backend.security.*;

import com.backend.user.dto.CreateUserDTO;
import com.backend.user.dto.LoginDTO;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

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
        UserEntity existeduser = userRepository.findByUsername(user.getUsername());
        if(existeduser!=null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The registered name is duplicated");
        }

        if (user.isAdmin()) {
            if (RoleEnum.ROLE_ADMIN.equals(this)) {
                roles.add(new RoleEntity(RoleEnum.ROLE_ADMIN.toString()));
            } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non-administrative users");
        }
        else roles.add(new RoleEntity(RoleEnum.ROLE_USER.toString()));

        return userRepository.save(new UserEntity(user.getUsername(), hashedPassword, roles, user.getImg()));
    }
}
