package com.example.elibrary.repository;

import com.example.elibrary.domain.BorrowRecord;
import com.example.elibrary.domain.BorrowStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByUserIdAndStatusOrderByBorrowedAtDesc(Long userId, BorrowStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from BorrowRecord r where r.id = :id and r.userId = :userId")
    Optional<BorrowRecord> findByIdAndUserIdForUpdate(
            @Param("id") Long id, @Param("userId") Long userId);
}
