package com.mjc.school.service.dto;

import com.mjc.school.service.validated.Mandatory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewsDtoRequest(

        @Positive
        Long id,

        @NotBlank(groups = Mandatory.class)
        @Size(min=5,max=30)
        String title,

        @NotBlank(groups = Mandatory.class)
        @Size(min=5,max=255)
        String content,

        @NotNull(groups = Mandatory.class)
        Long authorId,

        List<Long> tagIds
) {
}
