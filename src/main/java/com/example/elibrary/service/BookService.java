package com.example.elibrary.service;

import com.example.elibrary.domain.Book;
import com.example.elibrary.dto.BookResponse;
import com.example.elibrary.exception.NotFoundException;
import com.example.elibrary.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> search(String keyword, int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        Page<Book> books = bookRepository.search(
                keyword == null || keyword.isBlank() ? null : keyword.trim(),
                PageRequest.of(Math.max(page, 0), safeSize, Sort.by("id").ascending())
        );
        return books.map(BookResponse::from);
    }

    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        return BookResponse.from(book);
    }
}
