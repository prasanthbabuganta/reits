package com.reitsplatform.domain.repositories;

import com.reitsplatform.domain.entities.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {

    List<Dividend> findByReitId(Long reitId);

    @Query("SELECT d FROM Dividend d WHERE d.reit.id = :reitId ORDER BY d.exDividendDate DESC")
    List<Dividend> findByReitIdOrderByExDividendDateDesc(Long reitId);

    @Query("SELECT d FROM Dividend d WHERE d.paymentDate BETWEEN :startDate AND :endDate " +
           "ORDER BY d.paymentDate ASC")
    List<Dividend> findUpcomingDividends(LocalDate startDate, LocalDate endDate);

    @Query("SELECT d FROM Dividend d WHERE d.isProcessed = false AND d.paymentDate <= :date")
    List<Dividend> findUnprocessedDividends(LocalDate date);

    @Query("SELECT d FROM Dividend d WHERE d.reit.id = :reitId AND " +
           "d.exDividendDate >= :startDate ORDER BY d.exDividendDate DESC")
    List<Dividend> findRecentDividends(Long reitId, LocalDate startDate);
}
