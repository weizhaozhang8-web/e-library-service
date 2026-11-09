package com.example.elibrary.service;

import com.example.elibrary.domain.Book;
import com.example.elibrary.domain.BorrowRecord;
import com.example.elibrary.domain.BorrowStatus;
import com.example.elibrary.dto.BorrowRequest;
import com.example.elibrary.dto.BorrowingResponse;
import com.example.elibrary.exception.BusinessException;
import com.example.elibrary.exception.NotFoundException;
import com.example.elibrary.repository.BookRepository;
import com.example.elibrary.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BorrowingService {

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowingService(BookRepository bookRepository,
                            BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional
    public BorrowingResponse borrow(BorrowRequest request) {
        Book book = bookRepository.findByIdForUpdate(request.bookId())
                .orElseThrow(() -> new NotFoundException("Book not found: " + request.bookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("No copies are currently available");
        }

        book.borrowOneCopy();
        BorrowRecord record = borrowRecordRepository.save(
                new BorrowRecord(request.userId(), book)
        );
        return BorrowingResponse.from(record);
    }

    @Transactional
    public BorrowingResponse returnBook(Long borrowingId, Long userId) {
        BorrowRecord record = borrowRecordRepository.findByIdAndUserIdForUpdate(borrowingId, userId)
                .orElseThrow(() -> new NotFoundException("Borrowing record not found"));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new BusinessException("This borrowing has already been returned");
        }

        Book book = bookRepository.findByIdForUpdate(record.getBook().getId())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        record.markReturned();
        book.returnOneCopy();
        return BorrowingResponse.from(record);
    }

    @Transactional(readOnly = true)
    public List<BorrowingResponse> getCurrentBorrowings(Long userId) {
        return borrowRecordRepository
                .findByUserIdAndStatusOrderByBorrowedAtDesc(userId, BorrowStatus.BORROWED)
                .stream()
                .map(BorrowingResponse::from)
                .toList();
    }
}
