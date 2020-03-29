package com.backend.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "rm_image")
public class ImageEntity {
    @Id
    private String id;
    private Binary data;

    public ImageEntity(Binary data){
        this.data = data;
    }
}
