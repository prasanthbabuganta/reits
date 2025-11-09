package com.reitsplatform.service;

import com.reitsplatform.api.dto.REITResponse;
import com.reitsplatform.domain.entities.REIT;
import com.reitsplatform.domain.repositories.REITRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class REITService {

    private final REITRepository reitRepository;

    @Transactional(readOnly = true)
    public List<REITResponse> getAllREITs() {
        return reitRepository.findAllActiveAndTradeable().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public REITResponse getREITById(Long id) {
        REIT reit = reitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("REIT not found"));
        return mapToResponse(reit);
    }

    @Transactional(readOnly = true)
    public List<REITResponse> searchREITs(String searchTerm) {
        return reitRepository.searchREITs(searchTerm).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<REITResponse> getREITsBySector(String sector) {
        REIT.Sector sectorEnum = REIT.Sector.valueOf(sector.toUpperCase());
        return reitRepository.findBySector(sectorEnum).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<REITResponse> getTopDividendYielders(int limit) {
        return reitRepository.findTopDividendYielders(PageRequest.of(0, limit)).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public REITResponse createREIT(REIT reit) {
        REIT savedREIT = reitRepository.save(reit);
        return mapToResponse(savedREIT);
    }

    @Transactional
    public REITResponse updateREIT(Long id, REIT reitDetails) {
        REIT reit = reitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("REIT not found"));

        // Update fields
        reit.setCurrentPrice(reitDetails.getCurrentPrice());
        reit.setDividendYield(reitDetails.getDividendYield());
        reit.setPriceToBook(reitDetails.getPriceToBook());
        reit.setOccupancyRate(reitDetails.getOccupancyRate());
        reit.setMarketCap(reitDetails.getMarketCap());

        REIT updatedREIT = reitRepository.save(reit);
        return mapToResponse(updatedREIT);
    }

    private REITResponse mapToResponse(REIT reit) {
        return REITResponse.builder()
                .id(reit.getId())
                .ticker(reit.getTicker())
                .name(reit.getName())
                .exchange(reit.getExchange())
                .sector(reit.getSector().name())
                .marketCap(reit.getMarketCap())
                .currentPrice(reit.getCurrentPrice())
                .previousClose(reit.getPreviousClose())
                .dividendYield(reit.getDividendYield())
                .priceToBook(reit.getPriceToBook())
                .occupancyRate(reit.getOccupancyRate())
                .country(reit.getCountry())
                .currency(reit.getCurrency())
                .description(reit.getDescription())
                .logoUrl(reit.getLogoUrl())
                .tradingEnabled(reit.getTradingEnabled())
                .build();
    }
}
