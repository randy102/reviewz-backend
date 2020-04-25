package com.backend.movie.dto;

import com.backend.category.CategoryEntity;
import com.backend.movie.MovieEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDTO extends MovieEntity {
    private float starAvg;
}
