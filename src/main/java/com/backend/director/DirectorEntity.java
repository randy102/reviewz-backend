package com.backend.director;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "mr_director")
@NoArgsConstructor
public class DirectorEntity{
        @Id
        private String id;
        private String name;

        public DirectorEntity(String name){ this.name = name;}

        public DirectorEntity(String id, String name){
            this.id = id;
            this.name = name;
        }
}
