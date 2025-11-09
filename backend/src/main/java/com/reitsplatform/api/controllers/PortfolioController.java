package com.reitsplatform.api.controllers;

import com.reitsplatform.api.dto.PortfolioResponse;
import com.reitsplatform.domain.entities.User;
import com.reitsplatform.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<PortfolioResponse> getPortfolio(
            @AuthenticationPrincipal User user) {
        PortfolioResponse portfolio = portfolioService.getPortfolioByUserId(user.getId());
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/refresh")
    public ResponseEntity<PortfolioResponse> refreshPortfolio(
            @AuthenticationPrincipal User user) {
        portfolioService.updatePortfolioMetrics(user.getId());
        PortfolioResponse portfolio = portfolioService.getPortfolioByUserId(user.getId());
        return ResponseEntity.ok(portfolio);
    }
}
