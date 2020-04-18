package com.backend.user;

import com.backend.Error;
import com.backend.image.ImageRepository;
import com.backend.security.*;
import com.backend.user.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ImageRepository imageRepository;

    public String login(LoginDTO input) throws NoSuchAlgorithmException{
        UserEntity existedUser = userRepository.findByUsername(input.getUsername());
        if (existedUser == null)
            throw Error.NotFoundError("User");

        String hashedPassword = HashService.hash(input.getPassword());
        if (!existedUser.getPassword().equals(hashedPassword))
            throw Error.FormError("Password");

        return jwtUtil.sign(existedUser);
    }


    public UserEntity register(RegisterDTO user) throws Exception {
        String hashedPassword = HashService.hash(user.getPassword());
        Set<RoleEntity> roles = new HashSet<>();
        UserEntity existedUser = userRepository.findByUsername(user.getUsername());

        if (existedUser != null)
            throw Error.DuplicatedError("User");
        roles.add(new RoleEntity(RoleEnum.ROLE_USER));

        return userRepository.save(new UserEntity(user.getUsername(), hashedPassword, roles, ""));
    }


    public List<UserEntity> getAllUser() throws ArrayStoreException{
        return userRepository.findAll();
    }

    public UserEntity deleteUser(String id) throws Exception{
        UserEntity user = userRepository.findById(id).orElse(null);

        if(user == null){
            throw Error.NotFoundError("User");
        }

        userRepository.deleteById(id);
        return user;
    }

    public UserEntity detailUser(String id) throws Exception {
        UserEntity user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw Error.NotFoundError("User");
        }

        return user;
    }

    /**
     * @forAdmin to reset password, change user's role
     * @forUser to change img, username
     * @return UserEntity
     */
    public String updateUser(String id, UpdateUserDTO input) throws NoSuchAlgorithmException {
        UserEntity existedUser = userRepository.findById(id).orElse(null);
        boolean currentUserIsAdmin = currentUser.getInfo().hasRole(RoleEnum.ROLE_ADMIN);

        if (existedUser == null)
            throw Error.NotFoundError("User");

        // If role changed
        if (input.getRole() != null) {
             // Must be admin to proceed
            if (!currentUserIsAdmin)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission");

            Set<RoleEntity> newRoles = new HashSet<>();

            if (input.getRole().equals(RoleEnum.ROLE_ADMIN))
                newRoles.add(new RoleEntity(RoleEnum.ROLE_ADMIN));
            else
                newRoles.add(new RoleEntity(RoleEnum.ROLE_USER));

            existedUser.setRoles(newRoles);
        }

        // If reset password
        if(input.getPassword() != null)
            existedUser.setPassword(HashService.hash(input.getPassword()));

        // If username changed
        if(input.getUsername() != null){
            // Check existed username
            UserEntity existedUsername = userRepository.findByUsername(input.getUsername());
            if(existedUsername != null && !existedUsername.getId().equals(existedUser.getId()))
                throw Error.DuplicatedError("User");
            existedUser.setUsername(input.getUsername());
        }

        if(input.getImg() != null)
            existedUser.setImg(input.getImg());

        return jwtUtil.sign(userRepository.save(existedUser));
    }


    @Secured("ROLE_ADMIN")
    public UserEntity createUser(CreateUserDTO input) throws NoSuchAlgorithmException {
        Set<RoleEntity> roles = new HashSet<>();

        if(input.isAdmin())
            roles.add(new RoleEntity(RoleEnum.ROLE_ADMIN));
        else
            roles.add(new RoleEntity(RoleEnum.ROLE_USER));

        // If username has existed
        UserEntity existedUsername = userRepository.findByUsername(input.getUsername());
        if(existedUsername != null)
            throw Error.DuplicatedError("User");

        UserEntity userToCreate = new UserEntity(
                input.getUsername(),
                HashService.hash(input.getPassword()),
                roles,
                ""
        );

        return userRepository.save(userToCreate);
    }

    public UserEntity changePassword(String id, ChangePasswordDTO input) throws NoSuchAlgorithmException {
        UserEntity existed = userRepository.findById(id).orElse(null);
        String hashedOldPassword = HashService.hash(input.getOldPassword());
        String hashedNewPassword = HashService.hash(input.getNewPassword());

        if(existed == null)
            throw Error.NotFoundError("User");

        if(!existed.getPassword().equals(hashedOldPassword))
            throw Error.FormError("Password");

        existed.setPassword(hashedNewPassword);

        return userRepository.save(existed);
    }
}
