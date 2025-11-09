package com.reitsplatform.service;

import com.reitsplatform.api.dto.HoldingResponse;
import com.reitsplatform.api.dto.PortfolioResponse;
import com.reitsplatform.domain.entities.Holding;
import com.reitsplatform.domain.entities.Portfolio;
import com.reitsplatform.domain.repositories.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolioByUserId(Long userId) {
        Portfolio portfolio = portfolioRepository.findByUserIdWithHoldings(userId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found for user"));

        return mapToResponse(portfolio);
    }

    @Transactional
    public void updatePortfolioMetrics(Long userId) {
        Portfolio portfolio = portfolioRepository.findByUserIdWithHoldings(userId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal totalInvested = BigDecimal.ZERO;
        BigDecimal annualDividendIncome = BigDecimal.ZERO;

        for (Holding holding : portfolio.getHoldings()) {
            // Update individual holding metrics
            holding.updateMetrics(holding.getReit().getCurrentPrice());

            totalValue = totalValue.add(holding.getCurrentValue());
            totalInvested = totalInvested.add(holding.getTotalInvested());

            // Calculate annual dividend income
            BigDecimal reitDividendYield = holding.getReit().getDividendYield();
            if (reitDividendYield != null) {
                BigDecimal holdingAnnualDividend = holding.getCurrentValue()
                        .multiply(reitDividendYield)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                annualDividendIncome = annualDividendIncome.add(holdingAnnualDividend);
            }
        }

        portfolio.setTotalValue(totalValue);
        portfolio.setTotalInvested(totalInvested);
        portfolio.setUnrealizedGain(totalValue.subtract(totalInvested));
        portfolio.setAnnualDividendIncome(annualDividendIncome);

        portfolioRepository.save(portfolio);
    }

    private PortfolioResponse mapToResponse(Portfolio portfolio) {
        List<HoldingResponse> holdings = portfolio.getHoldings().stream()
                .map(this::mapHoldingToResponse)
                .collect(Collectors.toList());

        BigDecimal unrealizedGainPercentage = BigDecimal.ZERO;
        if (portfolio.getTotalInvested().compareTo(BigDecimal.ZERO) > 0) {
            unrealizedGainPercentage = portfolio.getUnrealizedGain()
                    .divide(portfolio.getTotalInvested(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return PortfolioResponse.builder()
                .portfolioId(portfolio.getId())
                .totalValue(portfolio.getTotalValue())
                .totalInvested(portfolio.getTotalInvested())
                .unrealizedGain(portfolio.getUnrealizedGain())
                .unrealizedGainPercentage(unrealizedGainPercentage)
                .realizedGain(portfolio.getRealizedGain())
                .totalDividendsReceived(portfolio.getTotalDividendsReceived())
                .annualDividendIncome(portfolio.getAnnualDividendIncome())
                .holdings(holdings)
                .build();
    }

    private HoldingResponse mapHoldingToResponse(Holding holding) {
        return HoldingResponse.builder()
                .holdingId(holding.getId())
                .reitId(holding.getReit().getId())
                .reitTicker(holding.getReit().getTicker())
                .reitName(holding.getReit().getName())
                .sector(holding.getReit().getSector().name())
                .totalUnits(holding.getTotalUnits())
                .averageCost(holding.getAverageCost())
                .currentPrice(holding.getReit().getCurrentPrice())
                .totalInvested(holding.getTotalInvested())
                .currentValue(holding.getCurrentValue())
                .unrealizedGain(holding.getUnrealizedGain())
                .unrealizedGainPercentage(holding.getUnrealizedGainPercentage())
                .dividendsReceived(holding.getDividendsReceived())
                .build();
    }
}
