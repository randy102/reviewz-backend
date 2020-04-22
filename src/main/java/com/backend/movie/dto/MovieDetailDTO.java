package com.backend.movie.dto;
import com.backend.category.CategoryEntity;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class MovieDetailDTO {
    @Id private String id;
    @NonNull private List<CategoryEntity> categories;
    @NonNull private String img;
    @NonNull private String nameVn;
    @NonNull private String nameEn;
    @NonNull private String summary;
    @NonNull private long releaseDate;
    @NonNull private long createdAt;
}
