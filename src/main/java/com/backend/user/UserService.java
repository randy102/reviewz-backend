package com.backend.user;

import com.backend.Error;
import com.backend.security.*;
import com.backend.user.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private JwtUtil jwtUtil;

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

    public Optional<UserEntity> deleteUser(String id) throws Exception{
        Optional<UserEntity> user = userRepository.findById(id);

        if(user.isPresent()){
            userRepository.deleteById(id);
            return user;
        }
        else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found");
    }

    public Optional<UserEntity> detailUser(String id) throws Exception {
        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user;
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found");
    }

    /**
     * @forAdmin to reset password, change user's role
     * @forUser to change img, username
     * @return UserEntity
     */
    public UserEntity updateUser(String id, UpdateUserDTO input) throws NoSuchAlgorithmException {
        UserEntity existedUser = userRepository.findById(id).orElse(null);
        boolean currentUserIsAdmin = currentUser.getInfo().hasRole(RoleEnum.ROLE_ADMIN);

        if (existedUser == null)
            throw Error.NotFoundError("User");

        //Return true if existed user is Admin
        boolean hasRoleAdmin = existedUser.getRoles().stream().anyMatch(roleEntity -> roleEntity.getAuthority().equals(RoleEnum.ROLE_ADMIN.toString()));

        // If role changed (adding or remove ROLE_ADMIN)
        if (input.isAdmin() != hasRoleAdmin) {
            // Must be admin to proceed
            if (!currentUserIsAdmin)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission");

            if (!input.isAdmin()) { // If remove ROLE_ADMIN
                Set<RoleEntity> modifiedRoles = existedUser.getRoles().stream()
                        .filter(roleEntity -> !roleEntity.getAuthority().equals(RoleEnum.ROLE_ADMIN.value))
                        .collect(Collectors.toSet());
                existedUser.setRoles(modifiedRoles);
            } else { // If add ROLE_ADMIN
                Set<RoleEntity> modifiedRoles = existedUser.getRoles();
                modifiedRoles.add(new RoleEntity(RoleEnum.ROLE_ADMIN));
                existedUser.setRoles(modifiedRoles);
            }
        }

        // If reset password
        if(!input.getPassword().isEmpty()){
            // Must be admin to proceed
            if(!currentUserIsAdmin)
                throw Error.NoPermissionError();

            existedUser.setPassword(HashService.hash(input.getPassword()));
        }

        // If username changed
        if(!input.getUsername().isEmpty()){
            // Check existed username
            UserEntity existedUsername = userRepository.findByUsername(input.getUsername());
            if(existedUsername != null)
                throw Error.DuplicatedError("User");

            existedUser.setUsername(input.getUsername());
        }


        if(!input.getImg().isEmpty())
            existedUser.setImg(input.getImg());

        return userRepository.save(existedUser);
    }


    @Secured("ROLE_ADMIN")
    public UserEntity createUser(CreateUserDTO input){
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(new RoleEntity(RoleEnum.ROLE_USER));
        if(input.isAdmin())
            roles.add(new RoleEntity(RoleEnum.ROLE_ADMIN));

        // If username has existed
        UserEntity existedUsername = userRepository.findByUsername(input.getUsername());
        if(existedUsername != null)
            throw Error.DuplicatedError("User");

        UserEntity userToCreate = new UserEntity(
                input.getUsername(),
                input.getPassword(),
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
