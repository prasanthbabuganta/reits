package com.reitsplatform.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PortfolioResponse {
    private Long portfolioId;
    private BigDecimal totalValue;
    private BigDecimal totalInvested;
    private BigDecimal unrealizedGain;
    private BigDecimal unrealizedGainPercentage;
    private BigDecimal realizedGain;
    private BigDecimal totalDividendsReceived;
    private BigDecimal annualDividendIncome;
    private List<HoldingResponse> holdings;
}
