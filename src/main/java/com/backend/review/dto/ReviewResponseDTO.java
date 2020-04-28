package com.backend.review.dto;

import com.backend.movie.MovieEntity;
import com.backend.review.ReviewEntity;
import com.backend.user.UserEntity;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO extends ReviewEntity {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Movie {
        private String nameVn;
        private String nameEn;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User{
        private String username;
        private String img;
    }

    private Movie movie;
    private User user;
}

