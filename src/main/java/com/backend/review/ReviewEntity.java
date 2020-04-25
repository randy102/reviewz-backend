package com.backend.review;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
@Document(collection = "mr_review")
@RequiredArgsConstructor
@NoArgsConstructor
public class ReviewEntity {
    @Id String id;
    @NonNull private String idUser;
    @NonNull private String idMovie;
    @NonNull private String content = "";
    @NonNull private @Max(10) @Min(1) int star;
    @NonNull private boolean isVerified;
    @NonNull private long createdAt;
}
