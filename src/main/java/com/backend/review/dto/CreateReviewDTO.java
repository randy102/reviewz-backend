package com.backend.review.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewDTO {
    @NotNull
    private String idMovie;

    @NotNull
    private String content;

    @NotNull
    @Max(10) @Min(1)
    private int star;
}
