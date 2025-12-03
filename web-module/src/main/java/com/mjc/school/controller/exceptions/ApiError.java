package com.mjc.school.controller.exceptions;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        String message,
        String code,
        Instant timestamp,
        String path,
        Map<String,String> errors
) {
}
