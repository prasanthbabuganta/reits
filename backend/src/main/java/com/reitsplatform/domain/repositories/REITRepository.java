package com.reitsplatform.domain.repositories;

import com.reitsplatform.domain.entities.REIT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface REITRepository extends JpaRepository<REIT, Long> {

    Optional<REIT> findByTicker(String ticker);

    List<REIT> findBySector(REIT.Sector sector);

    List<REIT> findByCountry(String country);

    List<REIT> findByExchange(String exchange);

    @Query("SELECT r FROM REIT r WHERE r.isActive = true AND r.tradingEnabled = true")
    List<REIT> findAllActiveAndTradeable();

    @Query("SELECT r FROM REIT r WHERE r.isActive = true AND " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.ticker) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<REIT> searchREITs(@Param("searchTerm") String searchTerm);

    @Query("SELECT r FROM REIT r WHERE r.sector = :sector AND r.isActive = true " +
           "ORDER BY r.marketCap DESC")
    List<REIT> findTopREITsBySector(@Param("sector") REIT.Sector sector);

    @Query("SELECT r FROM REIT r WHERE r.isActive = true ORDER BY r.dividendYield DESC")
    List<REIT> findTopDividendYielders(org.springframework.data.domain.Pageable pageable);
}
