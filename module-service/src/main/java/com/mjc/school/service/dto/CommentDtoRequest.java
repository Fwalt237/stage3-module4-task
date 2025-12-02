package com.mjc.school.service.dto;

import com.mjc.school.service.validated.Mandatory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CommentDtoRequest(
        @NotNull @Positive
        Long id,

        @NotBlank(groups = Mandatory.class)
        @Size(min=5,max=255)
        String content,

        @NotNull(groups = Mandatory.class)
        Long newsId
) {
}
