package com.reitsplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Holding> holdings = new ArrayList<>();

    @Column(name = "total_value")
    @Builder.Default
    private BigDecimal totalValue = BigDecimal.ZERO;

    @Column(name = "total_invested")
    @Builder.Default
    private BigDecimal totalInvested = BigDecimal.ZERO;

    @Column(name = "unrealized_gain")
    @Builder.Default
    private BigDecimal unrealizedGain = BigDecimal.ZERO;

    @Column(name = "realized_gain")
    @Builder.Default
    private BigDecimal realizedGain = BigDecimal.ZERO;

    @Column(name = "total_dividends_received")
    @Builder.Default
    private BigDecimal totalDividendsReceived = BigDecimal.ZERO;

    @Column(name = "annual_dividend_income")
    @Builder.Default
    private BigDecimal annualDividendIncome = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addHolding(Holding holding) {
        holdings.add(holding);
        holding.setPortfolio(this);
    }

    public void removeHolding(Holding holding) {
        holdings.remove(holding);
        holding.setPortfolio(null);
    }
}
