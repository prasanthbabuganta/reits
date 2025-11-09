package com.reitsplatform.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class REITResponse {
    private Long id;
    private String ticker;
    private String name;
    private String exchange;
    private String sector;
    private BigDecimal marketCap;
    private BigDecimal currentPrice;
    private BigDecimal previousClose;
    private BigDecimal dividendYield;
    private BigDecimal priceToBook;
    private BigDecimal occupancyRate;
    private String country;
    private String currency;
    private String description;
    private String logoUrl;
    private Boolean tradingEnabled;
}
