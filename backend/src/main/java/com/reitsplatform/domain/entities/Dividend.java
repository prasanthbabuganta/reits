package com.reitsplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dividends")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dividend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reit_id", nullable = false)
    private REIT reit;

    @Column(name = "amount_per_unit", nullable = false)
    private BigDecimal amountPerUnit;

    @Column(name = "ex_dividend_date", nullable = false)
    private LocalDate exDividendDate;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DividendType type = DividendType.REGULAR;

    @Column(nullable = false)
    private String currency;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_processed")
    @Builder.Default
    private Boolean isProcessed = false;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

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

    public enum Frequency {
        MONTHLY, QUARTERLY, SEMI_ANNUAL, ANNUAL
    }

    public enum DividendType {
        REGULAR, SPECIAL, CAPITAL_RETURN
    }
}
