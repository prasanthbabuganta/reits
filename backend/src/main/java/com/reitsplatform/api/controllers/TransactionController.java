package com.reitsplatform.api.controllers;

import com.reitsplatform.api.dto.TransactionRequest;
import com.reitsplatform.domain.entities.Transaction;
import com.reitsplatform.domain.entities.User;
import com.reitsplatform.domain.repositories.TransactionRepository;
import com.reitsplatform.service.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final InvestmentService investmentService;
    private final TransactionRepository transactionRepository;

    @PostMapping("/buy")
    public ResponseEntity<Map<String, Object>> buyREIT(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = investmentService.buyREIT(user.getId(), request);
        return ResponseEntity.ok(Map.of(
                "message", "Purchase successful",
                "transactionId", transaction.getTransactionId(),
                "status", transaction.getStatus()
        ));
    }

    @PostMapping("/sell")
    public ResponseEntity<Map<String, Object>> sellREIT(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = investmentService.sellREIT(user.getId(), request);
        return ResponseEntity.ok(Map.of(
                "message", "Sale successful",
                "transactionId", transaction.getTransactionId(),
                "status", transaction.getStatus()
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<Page<Transaction>> getTransactionHistory(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Transaction> transactions = transactionRepository.findByUserId(user.getId(), pageRequest);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Transaction>> getPendingTransactions(
            @AuthenticationPrincipal User user) {
        List<Transaction> transactions = transactionRepository
                .findByUserIdAndStatus(user.getId(), Transaction.TransactionStatus.PENDING);
        return ResponseEntity.ok(transactions);
    }
}
