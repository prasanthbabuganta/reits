package com.reitsplatform.api.controllers;

import com.reitsplatform.api.dto.REITResponse;
import com.reitsplatform.service.REITService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reits")
@RequiredArgsConstructor
public class REITController {

    private final REITService reitService;

    @GetMapping
    public ResponseEntity<List<REITResponse>> getAllREITs() {
        List<REITResponse> reits = reitService.getAllREITs();
        return ResponseEntity.ok(reits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<REITResponse> getREITById(@PathVariable Long id) {
        REITResponse reit = reitService.getREITById(id);
        return ResponseEntity.ok(reit);
    }

    @GetMapping("/search")
    public ResponseEntity<List<REITResponse>> searchREITs(@RequestParam String q) {
        List<REITResponse> reits = reitService.searchREITs(q);
        return ResponseEntity.ok(reits);
    }

    @GetMapping("/sector/{sector}")
    public ResponseEntity<List<REITResponse>> getREITsBySector(@PathVariable String sector) {
        List<REITResponse> reits = reitService.getREITsBySector(sector);
        return ResponseEntity.ok(reits);
    }

    @GetMapping("/top-dividend-yielders")
    public ResponseEntity<List<REITResponse>> getTopDividendYielders(
            @RequestParam(defaultValue = "10") int limit) {
        List<REITResponse> reits = reitService.getTopDividendYielders(limit);
        return ResponseEntity.ok(reits);
    }
}
