package com.backend.movie.dto;

import com.backend.category.CategoryEntity;
import com.backend.movie.MovieEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = "mr_movie")
@RequiredArgsConstructor
@NoArgsConstructor
public class MovieDetailDTO extends MovieEntity {
    @NonNull private List<CategoryEntity> categoriesObj;
}
