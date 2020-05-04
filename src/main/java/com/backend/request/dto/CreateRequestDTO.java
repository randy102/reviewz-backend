package com.backend.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestDTO {
    @NonNull String movieName;
    @NonNull String info;
}
