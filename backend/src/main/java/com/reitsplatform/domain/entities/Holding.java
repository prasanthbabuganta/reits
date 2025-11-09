package com.reitsplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "holdings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "reit_id", nullable = false)
    private REIT reit;

    @Column(name = "total_units", nullable = false)
    @Builder.Default
    private BigDecimal totalUnits = BigDecimal.ZERO;

    @Column(name = "average_cost", nullable = false)
    @Builder.Default
    private BigDecimal averageCost = BigDecimal.ZERO;

    @Column(name = "total_invested", nullable = false)
    @Builder.Default
    private BigDecimal totalInvested = BigDecimal.ZERO;

    @Column(name = "current_value")
    @Builder.Default
    private BigDecimal currentValue = BigDecimal.ZERO;

    @Column(name = "unrealized_gain")
    @Builder.Default
    private BigDecimal unrealizedGain = BigDecimal.ZERO;

    @Column(name = "unrealized_gain_percentage")
    @Builder.Default
    private BigDecimal unrealizedGainPercentage = BigDecimal.ZERO;

    @Column(name = "dividends_received")
    @Builder.Default
    private BigDecimal dividendsReceived = BigDecimal.ZERO;

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

    public void updateMetrics(BigDecimal currentPrice) {
        this.currentValue = this.totalUnits.multiply(currentPrice);
        this.unrealizedGain = this.currentValue.subtract(this.totalInvested);
        if (this.totalInvested.compareTo(BigDecimal.ZERO) > 0) {
            this.unrealizedGainPercentage = this.unrealizedGain
                    .divide(this.totalInvested, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }
}
