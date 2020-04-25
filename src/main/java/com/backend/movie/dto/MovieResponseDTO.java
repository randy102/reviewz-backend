package com.backend.movie.dto;

import com.backend.category.CategoryEntity;
import com.backend.movie.MovieEntity;
import lombok.*;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDTO extends MovieEntity {
    private float starAvg;
}
