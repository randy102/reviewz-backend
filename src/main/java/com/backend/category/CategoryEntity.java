package com.backend.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "mr_category")
@NoArgsConstructor
public class CategoryEntity {
    @Id
    private String id;
    private String name;

    public CategoryEntity(String name){
        this.name = name;
    }

    public CategoryEntity( String id, String name){
        this.id = id;
        this.name = name;
    }

}
