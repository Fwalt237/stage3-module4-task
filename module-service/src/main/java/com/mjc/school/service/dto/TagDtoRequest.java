package com.mjc.school.service.dto;

import com.mjc.school.service.validated.Mandatory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TagDtoRequest(
        @Positive
        Long id,

        @NotBlank(groups = Mandatory.class)
        @Size(min=3,max=15)
        String name
) {
}
