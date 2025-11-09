package com.reitsplatform.domain.repositories;

import com.reitsplatform.domain.entities.Portfolio;
import com.reitsplatform.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByUser(User user);

    Optional<Portfolio> findByUserId(Long userId);

    @Query("SELECT p FROM Portfolio p JOIN FETCH p.holdings WHERE p.user.id = :userId")
    Optional<Portfolio> findByUserIdWithHoldings(Long userId);

    boolean existsByUserId(Long userId);
}
