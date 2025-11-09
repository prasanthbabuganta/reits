package com.reitsplatform.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class HoldingResponse {
    private Long holdingId;
    private Long reitId;
    private String reitTicker;
    private String reitName;
    private String sector;
    private BigDecimal totalUnits;
    private BigDecimal averageCost;
    private BigDecimal currentPrice;
    private BigDecimal totalInvested;
    private BigDecimal currentValue;
    private BigDecimal unrealizedGain;
    private BigDecimal unrealizedGainPercentage;
    private BigDecimal dividendsReceived;
}
