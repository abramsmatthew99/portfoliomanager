package com.skilldelity.portfoliomanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skilldelity.portfoliomanager.engine.ScoringEngine;
import com.skilldelity.portfoliomanager.engine.TechnicalIndicatorCalculator;
import com.skilldelity.portfoliomanager.model.PricePoint;
import com.skilldelity.portfoliomanager.model.RecommendationResult;

@Service
public class RecommendationService {
    
    private final PortfolioService portfolioService;
    private final MarketDataService marketDataService;
    private final TechnicalIndicatorCalculator indicatorCalculator;
    private final ScoringEngine scoringEngine;

    public RecommendationService(MarketDataService marketDataService, TechnicalIndicatorCalculator indicatorCalculator, ScoringEngine scoringEngine) {
        this.marketDataService = marketDataService;
        this.indicatorCalculator = indicatorCalculator;
        this.scoringEngine = scoringEngine;
    }

    public RecommendationResult getRecommendation(String symbol) {
        List<PricePoint> prices = marketDataService.getHistoricalPrices(symbol);
        return scoringEngine.getRecommendation(prices);
    }
}
