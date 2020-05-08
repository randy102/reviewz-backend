package com.backend.movie.dto;

import com.backend.category.CategoryEntity;
import com.backend.movie.MovieEntity;
import com.backend.review.ReviewEntity;
import lombok.*;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDTO extends MovieEntity {
    private float starAvg;
    private int rated;
    private List<ReviewEntity> reviews;
}
