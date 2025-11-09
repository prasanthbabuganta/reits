package com.reitsplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class REIT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ticker;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String exchange;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sector sector;

    @Column(name = "market_cap")
    private BigDecimal marketCap;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "previous_close")
    private BigDecimal previousClose;

    @Column(name = "dividend_yield")
    private BigDecimal dividendYield;

    @Column(name = "price_to_book")
    private BigDecimal priceToBook;

    @Column(name = "occupancy_rate")
    private BigDecimal occupancyRate;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String currency;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "total_assets")
    private BigDecimal totalAssets;

    @Column(name = "nav_per_unit")
    private BigDecimal navPerUnit;

    @Column(name = "gearing_ratio")
    private BigDecimal gearingRatio;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "trading_enabled")
    @Builder.Default
    private Boolean tradingEnabled = true;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    public enum Sector {
        RETAIL,
        OFFICE,
        INDUSTRIAL,
        HOSPITALITY,
        HEALTHCARE,
        DATA_CENTER,
        RESIDENTIAL,
        DIVERSIFIED,
        SPECIALTY
    }
}
