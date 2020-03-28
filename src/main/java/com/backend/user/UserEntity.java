package com.backend.user;

import com.backend.security.HashService;
import com.backend.security.RoleEntity;
import com.mongodb.client.model.Collation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;




import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;

@Document(collection = "test_user")
@Getter
@Setter
@ToString
public class UserEntity {
    @Id
    private String id;
    private String username;
    private String password;
    private Set<RoleEntity> roles;
    private String img;

    public UserEntity(String username, String password, Set<RoleEntity> roles, String img) throws NoSuchAlgorithmException {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.img = img == null ? "" : img;
    }
}
