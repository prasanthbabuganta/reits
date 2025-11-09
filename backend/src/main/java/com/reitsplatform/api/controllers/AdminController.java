package com.reitsplatform.api.controllers;

import com.reitsplatform.domain.entities.REIT;
import com.reitsplatform.domain.entities.User;
import com.reitsplatform.domain.repositories.REITRepository;
import com.reitsplatform.domain.repositories.TransactionRepository;
import com.reitsplatform.domain.repositories.UserRepository;
import com.reitsplatform.service.REITService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final REITRepository reitRepository;
    private final TransactionRepository transactionRepository;
    private final REITService reitService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/pending-kyc")
    public ResponseEntity<List<User>> getPendingKycUsers() {
        List<User> users = userRepository.findPendingKycVerification();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}/verify-kyc")
    public ResponseEntity<Map<String, String>> verifyKyc(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setKycVerified(true);
        user.setKycVerificationDate(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "KYC verified successfully"));
    }

    @PostMapping("/reits")
    public ResponseEntity<REIT> createREIT(@RequestBody REIT reit) {
        reitService.createREIT(reit);
        return ResponseEntity.ok(reit);
    }

    @PutMapping("/reits/{id}")
    public ResponseEntity<REIT> updateREIT(@PathVariable Long id, @RequestBody REIT reit) {
        reitService.updateREIT(id, reit);
        return ResponseEntity.ok(reit);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalUsers = userRepository.count();
        long totalREITs = reitRepository.count();
        long totalTransactions = transactionRepository.count();

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Long newUsers = userRepository.countNewUsers(thirtyDaysAgo);

        return ResponseEntity.ok(Map.of(
                "totalUsers", totalUsers,
                "totalREITs", totalREITs,
                "totalTransactions", totalTransactions,
                "newUsersLast30Days", newUsers
        ));
    }
}
