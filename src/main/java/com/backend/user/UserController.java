package com.backend.user;
import com.backend.RouteConfig;

import com.backend.image.ImageEntity;
import com.backend.user.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping(value = RouteConfig.USER_BASE)
public class UserController {

    @Autowired
    private UserService userService;

    // 4.1
    @GetMapping()
    public List<UserEntity> AllUser() throws ArrayIndexOutOfBoundsException {
        return userService.getAllUser();
    }

    // 4.2
    @PostMapping()
    public UserEntity createUser(@RequestBody() CreateUserDTO input) {
        return userService.createUser(input);
    }

    // 4.3
    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable("id") String id, @RequestBody() UpdateUserDTO input) throws NoSuchAlgorithmException {
        return userService.updateUser(id, input);
    }

    // 4.4
    @PostMapping("/login")
    public String login(@RequestBody LoginDTO input) throws Exception {
        return userService.login(input);
    }

    // 4.5
    @DeleteMapping("/{id}")
    public UserEntity deleteUser(@PathVariable("id") String id) throws Exception {
        return userService.deleteUser(id);
    }

    // 4.6
    @PostMapping("/register")
    public UserEntity register(@RequestBody RegisterDTO user) throws Exception {
        return userService.register(user);
    }

    // 4.9
    @GetMapping("/detail/{id}")
    public UserEntity detailUser(@PathVariable("id") String id) throws Exception {
        return userService.detailUser(id);
    }

    // 4.10
    @PutMapping("/password/{id}")
    public UserEntity changePassword(@PathVariable("id") String id, @RequestBody ChangePasswordDTO input) throws NoSuchAlgorithmException {
        return userService.changePassword(id, input);
    }

    //6.3
    @DeleteMapping("/image/{id}")
    public ImageEntity deleteImage(@PathVariable("id") String id) throws Exception{
        return userService.deleteImage(id);
    }
}