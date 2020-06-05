package com.backend.movie;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@ToString
@Document(collection = "mr_movie")
@RequiredArgsConstructor
@NoArgsConstructor
public class MovieEntity {
    @Id private String id;
    @NonNull private Set<String> categories;
    @NonNull private Set<String> actors;
    @NonNull private Set<String> directors;
    @NonNull private String img;
    @NonNull private String nameVn;
    @NonNull private String nameEn;
    @NonNull private String summary;
    @NonNull private String summaryR;
    @NonNull private String nameVnR;
    @NonNull private long releaseDate;
    @NonNull private long createdAt;
}
