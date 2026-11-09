package com.example.elibrary.dto;

import com.example.elibrary.domain.BorrowRecord;

import java.time.LocalDateTime;

public record BorrowingResponse(
        Long id,
        Long userId,
        Long bookId,
        String bookTitle,
        String status,
        LocalDateTime borrowedAt,
        LocalDateTime returnedAt
) {
    public static BorrowingResponse from(BorrowRecord record) {
        return new BorrowingResponse(
                record.getId(),
                record.getUserId(),
                record.getBook().getId(),
                record.getBook().getTitle(),
                record.getStatus().name(),
                record.getBorrowedAt(),
                record.getReturnedAt()
        );
    }
}
