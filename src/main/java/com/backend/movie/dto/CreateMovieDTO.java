package com.backend.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMovieDTO {
    private Set<String> categories;
    private String img;
    private String nameVn;
    private String nameEn;
    private String summary;
    private long releaseDate;
}
