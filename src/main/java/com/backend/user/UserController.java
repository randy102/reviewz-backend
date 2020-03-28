package com.backend.user;

import com.backend.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
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
    public String login(@RequestBody LoginDTO input) throws Exception{
        try{
            UserEntity existedUser = userRepository.findByUsername(input.getUsername());

            if(existedUser == null)
                throw new BadCredentialsException("Not found: User");

            String hashedPassword = HashService.hash(input.getPassword());
            System.out.println(existedUser.getId());
            System.out.println(hashedPassword);
            System.out.println(existedUser.getPassword());
            System.out.println(!existedUser.getPassword().equals(hashedPassword));
            if(!existedUser.getPassword().equals(hashedPassword))
                throw new BadCredentialsException("Incorrect Password");


        }

        catch (BadCredentialsException err){
            throw new Exception("Incorrect username or password", err);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(input.getUsername());

        return jwtUtil.sign(userDetails);
    }

    @PutMapping("/register")
    public UserEntity createUser(@RequestBody CreateUserDto input) throws NoSuchAlgorithmException {
        String hashedPassword = HashService.hash(input.getPassword());

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(new RoleEntity(RoleEnum.ROLE_USER.toString()));

        if(input.isAdmin())
            roles.add(new RoleEntity(RoleEnum.ROLE_USER.toString()));

        return userRepository.save(new UserEntity(input.getUsername(), hashedPassword, roles, input.getImg()));
    }
}
