package com.example.elibrary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BorrowRequest(
        @NotNull @Positive Long userId,
        @NotNull @Positive Long bookId
) {
}
