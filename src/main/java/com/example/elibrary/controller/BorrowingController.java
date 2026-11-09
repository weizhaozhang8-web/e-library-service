package com.example.elibrary.controller;

import com.example.elibrary.dto.BorrowRequest;
import com.example.elibrary.dto.BorrowingResponse;
import com.example.elibrary.service.BorrowingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@Validated
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping
    public BorrowingResponse borrow(@Valid @RequestBody BorrowRequest request) {
        return borrowingService.borrow(request);
    }

    @PostMapping("/{id}/return")
    public BorrowingResponse returnBook(
            @PathVariable @Positive Long id,
            @RequestParam @Positive Long userId) {
        return borrowingService.returnBook(id, userId);
    }

    @GetMapping
    public List<BorrowingResponse> getCurrentBorrowings(@RequestParam @Positive Long userId) {
        return borrowingService.getCurrentBorrowings(userId);
    }
}
