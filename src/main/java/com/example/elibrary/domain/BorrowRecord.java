package com.example.elibrary.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BorrowStatus status;

    @Column(nullable = false)
    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    protected BorrowRecord() {
    }

    public BorrowRecord(Long userId, Book book) {
        this.userId = userId;
        this.book = book;
        this.status = BorrowStatus.BORROWED;
        this.borrowedAt = LocalDateTime.now();
    }

    public void markReturned() {
        if (status == BorrowStatus.RETURNED) {
            throw new IllegalStateException("This borrowing has already been returned");
        }
        status = BorrowStatus.RETURNED;
        returnedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Book getBook() { return book; }
    public BorrowStatus getStatus() { return status; }
    public LocalDateTime getBorrowedAt() { return borrowedAt; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
}
