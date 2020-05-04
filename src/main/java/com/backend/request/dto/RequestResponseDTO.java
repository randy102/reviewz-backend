package com.backend.request.dto;

import com.backend.request.RequestEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponseDTO extends RequestEntity {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User{
        private String username;
        private String img;
    }

    private User user;
}
