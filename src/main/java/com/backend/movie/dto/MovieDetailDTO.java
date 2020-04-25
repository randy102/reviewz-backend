package com.backend.movie.dto;
import com.backend.category.CategoryEntity;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class MovieDetailDTO extends MovieResponseDTO {
    @NonNull private List<CategoryEntity> categoriesObj;

}