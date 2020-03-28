package com.backend.user;

import com.backend.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

import java.security.NoSuchAlgorithmException;

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
    public String login(@RequestBody LoginDTO input) throws Exception {

        UserEntity existedUser = userRepository.findByUsername(input.getUsername());

        if (existedUser == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not found: User");

        String hashedPassword = HashService.hash(input.getPassword());

        if (!existedUser.getPassword().equals(hashedPassword))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect Password");

        final UserDetails userDetails = userDetailsService.loadUserByUsername(input.getUsername());

        return jwtUtil.sign(userDetails);
    }

    @PutMapping("/register")
    public UserEntity createUser(@RequestBody CreateUserDto user) throws NoSuchAlgorithmException {
        String hashedPassword = HashService.hash(user.getPassword());
        Set<RoleEntity> roles = new HashSet<>();

        roles.add(new RoleEntity(RoleEnum.ROLE_USER.toString()));

        if (user.isAdmin())
            roles.add(new RoleEntity(RoleEnum.ROLE_ADMIN.toString()));

        return userRepository.save(new UserEntity(user.getUsername(), hashedPassword, roles, user.getImg()));
    }

    @PostMapping("/upload")
    public String uploadToLocalFileSystem(@RequestParam("file") MultipartFile upload) throws IOException {
        MultipartFile multipartFile = upload;
        String fileName = multipartFile.getOriginalFilename();
        File file = new File(this.getFolderUpload(), fileName);
        multipartFile.transferTo(file);
        return "Success";
    }

    public File getFolderUpload() {
        File folderUpload = new File(System.getProperty("user.home") + "/Uploads");
        if (!folderUpload.exists()) {
            folderUpload.mkdirs();
        }
        return folderUpload;
    }

}
