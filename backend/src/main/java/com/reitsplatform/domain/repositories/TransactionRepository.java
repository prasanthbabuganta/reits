package com.reitsplatform.domain.repositories;

import com.reitsplatform.domain.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(String transactionId);

    List<Transaction> findByUserId(Long userId);

    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    List<Transaction> findByUserIdAndStatus(Long userId, Transaction.TransactionStatus status);

    List<Transaction> findByReitId(Long reitId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND " +
           "t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findByUserIdAndDateRange(Long userId,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.status = :status AND " +
           "t.createdAt < :beforeDate")
    List<Transaction> findPendingTransactions(Transaction.TransactionStatus status,
                                             LocalDateTime beforeDate);

    @Query("SELECT SUM(t.totalAmount) FROM Transaction t WHERE t.user.id = :userId AND " +
           "t.type = :type AND t.status = 'COMPLETED'")
    java.math.BigDecimal getTotalTransactionAmount(Long userId, Transaction.TransactionType type);
}
