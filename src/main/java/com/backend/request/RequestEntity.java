package com.backend.request;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "mr_request")
@RequiredArgsConstructor
@NoArgsConstructor
public class RequestEntity {
    @Id String id;
    @NonNull String idUser;
    @NonNull String movieName;
    @NonNull String info;
    @NonNull boolean resolved;
    @NonNull long createdAt;
}
