package com.backend.user;
import com.backend.security.RoleEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@ToString
@Document(collection = "mr_user")
@NoArgsConstructor
public class UserEntity {
    @Id
    private String id;
    private String username;
    private String password;
    private Set<RoleEntity> roles;
    private String img;

    public UserEntity(String username, String password, Set<RoleEntity> roles, String img) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.img = img == null ? "" : img;
    }

    public UserEntity(String id, String username, String password, Set<RoleEntity> roles, String img) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.img = img == null ? "" : img;
    }
}
