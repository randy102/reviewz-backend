package com.backend.movie.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MovieFilterDTO {
    private String keyword; // No accents
    private String category; // idCategory
    private int year; // Default 0 => all years
    private String lastRelease; // false => most recently released , true/null => oldest released

    private int limit; // Default 0 => no limit
    private int skip; // Default 0
}
