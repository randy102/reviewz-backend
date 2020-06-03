package com.backend.actor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "mr_actor")
@NoArgsConstructor
public class ActorEntity {
    @Id
    private String id;
    private String name;

    public ActorEntity(String name){ this.name = name; }

    public ActorEntity(String id, String name){
        this.id = id;
        this.name = name;
    }

}
