package com.backend.user;
import com.backend.RouteConfig;

import com.backend.user.dto.CreateUserDTO;
import com.backend.user.dto.LoginDTO;
import com.backend.user.dto.RegisterDTO;
import com.backend.user.dto.UpdateUserDTO;
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

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO input) throws Exception {
        return userService.login(input);
    }

    @PostMapping("/register")
    public UserEntity createUser(@RequestBody RegisterDTO user) throws Exception {
        return userService.register(user);
    }


    @GetMapping("/")
    public List<UserEntity> AllUser() throws ArrayIndexOutOfBoundsException{
        return userService.getAllUser();
    }

    @DeleteMapping("/{id}")
    public Optional<UserEntity> deleteUser(@PathVariable("id") String id) throws Exception{
        return  userService.deleteUser(id);
    }
    @GetMapping("/detail/{id}")
    public Optional<UserEntity> detailUser(@PathVariable("id") String id) throws Exception{
        return userService.detailUser(id);
    }


    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable("id") String id, @RequestBody() UpdateUserDTO input) throws NoSuchAlgorithmException {
        return userService.updateUser(id, input);
    }

    @PostMapping()
    public UserEntity createUser(@RequestBody() CreateUserDTO input){
        return userService.createUser(input);
    }

}
