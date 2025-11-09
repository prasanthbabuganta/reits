package com.reitsplatform.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @NotNull(message = "REIT ID is required")
    private Long reitId;

    @NotNull(message = "Transaction type is required")
    private String type; // BUY or SELL

    @NotNull(message = "Units is required")
    @DecimalMin(value = "0.01", message = "Units must be greater than 0")
    private BigDecimal units;

    private String paymentMethod;
}
