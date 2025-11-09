package com.reitsplatform.service;

import com.reitsplatform.api.dto.TransactionRequest;
import com.reitsplatform.domain.entities.*;
import com.reitsplatform.domain.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final REITRepository reitRepository;
    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final PortfolioService portfolioService;

    @Transactional
    public Transaction buyREIT(Long userId, TransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        REIT reit = reitRepository.findById(request.getReitId())
                .orElseThrow(() -> new RuntimeException("REIT not found"));

        if (!reit.getTradingEnabled()) {
            throw new RuntimeException("Trading is not enabled for this REIT");
        }

        BigDecimal pricePerUnit = reit.getCurrentPrice();
        BigDecimal totalAmount = pricePerUnit.multiply(request.getUnits());
        BigDecimal fees = calculateFees(totalAmount);
        BigDecimal totalCost = totalAmount.add(fees);

        // Check if user has sufficient balance
        if (user.getAccountBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Create transaction
        Transaction transaction = Transaction.builder()
                .user(user)
                .reit(reit)
                .type(Transaction.TransactionType.BUY)
                .units(request.getUnits())
                .pricePerUnit(pricePerUnit)
                .totalAmount(totalAmount)
                .fees(fees)
                .currency(reit.getCurrency())
                .status(Transaction.TransactionStatus.COMPLETED)
                .paymentMethod(request.getPaymentMethod())
                .executedAt(LocalDateTime.now())
                .build();

        transaction = transactionRepository.save(transaction);

        // Update user balance
        user.setAccountBalance(user.getAccountBalance().subtract(totalCost));
        userRepository.save(user);

        // Update portfolio holding
        updateHoldingAfterBuy(userId, reit.getId(), request.getUnits(), pricePerUnit);

        // Update portfolio metrics
        portfolioService.updatePortfolioMetrics(userId);

        return transaction;
    }

    @Transactional
    public Transaction sellREIT(Long userId, TransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        REIT reit = reitRepository.findById(request.getReitId())
                .orElseThrow(() -> new RuntimeException("REIT not found"));

        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Holding holding = holdingRepository.findByPortfolioIdAndReitId(portfolio.getId(), reit.getId())
                .orElseThrow(() -> new RuntimeException("You don't own this REIT"));

        if (holding.getTotalUnits().compareTo(request.getUnits()) < 0) {
            throw new RuntimeException("Insufficient units to sell");
        }

        BigDecimal pricePerUnit = reit.getCurrentPrice();
        BigDecimal totalAmount = pricePerUnit.multiply(request.getUnits());
        BigDecimal fees = calculateFees(totalAmount);
        BigDecimal totalProceeds = totalAmount.subtract(fees);

        // Create transaction
        Transaction transaction = Transaction.builder()
                .user(user)
                .reit(reit)
                .type(Transaction.TransactionType.SELL)
                .units(request.getUnits())
                .pricePerUnit(pricePerUnit)
                .totalAmount(totalAmount)
                .fees(fees)
                .currency(reit.getCurrency())
                .status(Transaction.TransactionStatus.COMPLETED)
                .executedAt(LocalDateTime.now())
                .build();

        transaction = transactionRepository.save(transaction);

        // Update user balance
        user.setAccountBalance(user.getAccountBalance().add(totalProceeds));
        userRepository.save(user);

        // Update portfolio holding
        updateHoldingAfterSell(holding, request.getUnits(), pricePerUnit);

        // Update portfolio metrics
        portfolioService.updatePortfolioMetrics(userId);

        return transaction;
    }

    private void updateHoldingAfterBuy(Long userId, Long reitId, BigDecimal units, BigDecimal price) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Holding holding = holdingRepository.findByPortfolioIdAndReitId(portfolio.getId(), reitId)
                .orElse(null);

        if (holding == null) {
            // Create new holding
            REIT reit = reitRepository.findById(reitId)
                    .orElseThrow(() -> new RuntimeException("REIT not found"));

            holding = Holding.builder()
                    .portfolio(portfolio)
                    .reit(reit)
                    .totalUnits(units)
                    .averageCost(price)
                    .totalInvested(units.multiply(price))
                    .build();
        } else {
            // Update existing holding
            BigDecimal newTotalInvested = holding.getTotalInvested().add(units.multiply(price));
            BigDecimal newTotalUnits = holding.getTotalUnits().add(units);
            BigDecimal newAverageCost = newTotalInvested.divide(newTotalUnits, 4, RoundingMode.HALF_UP);

            holding.setTotalUnits(newTotalUnits);
            holding.setAverageCost(newAverageCost);
            holding.setTotalInvested(newTotalInvested);
        }

        holdingRepository.save(holding);
    }

    private void updateHoldingAfterSell(Holding holding, BigDecimal units, BigDecimal price) {
        BigDecimal newTotalUnits = holding.getTotalUnits().subtract(units);

        if (newTotalUnits.compareTo(BigDecimal.ZERO) == 0) {
            // Remove holding if all units are sold
            holdingRepository.delete(holding);
        } else {
            BigDecimal soldInvestment = holding.getAverageCost().multiply(units);
            BigDecimal newTotalInvested = holding.getTotalInvested().subtract(soldInvestment);

            holding.setTotalUnits(newTotalUnits);
            holding.setTotalInvested(newTotalInvested);

            holdingRepository.save(holding);
        }
    }

    private BigDecimal calculateFees(BigDecimal amount) {
        // Simple fee calculation: 0.5% of transaction amount
        return amount.multiply(BigDecimal.valueOf(0.005))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
