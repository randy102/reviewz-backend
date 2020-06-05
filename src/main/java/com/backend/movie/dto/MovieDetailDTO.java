package com.backend.movie.dto;
import com.backend.actor.ActorEntity;
import com.backend.category.CategoryEntity;
import com.backend.director.DirectorEntity;
import com.backend.review.ReviewEntity;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailDTO extends MovieResponseDTO {
    private List<CategoryEntity> categoriesObj;
    private List<ReviewEntity> reviews;
    private List<ActorEntity> actorsObj;
    private List<DirectorEntity> directorsObj;
}
