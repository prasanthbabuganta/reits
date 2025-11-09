package com.reitsplatform.domain.repositories;

import com.reitsplatform.domain.entities.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

    List<Holding> findByPortfolioId(Long portfolioId);

    Optional<Holding> findByPortfolioIdAndReitId(Long portfolioId, Long reitId);

    @Query("SELECT h FROM Holding h WHERE h.portfolio.user.id = :userId")
    List<Holding> findByUserId(Long userId);

    @Query("SELECT h FROM Holding h WHERE h.reit.id = :reitId")
    List<Holding> findByReitId(Long reitId);
}
