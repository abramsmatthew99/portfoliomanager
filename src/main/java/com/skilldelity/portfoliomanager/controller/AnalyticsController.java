package com.skilldelity.portfoliomanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skilldelity.portfoliomanager.model.ProjectionResult;
import com.skilldelity.portfoliomanager.model.RecommendationResult;
import com.skilldelity.portfoliomanager.service.ProjectionService;
import com.skilldelity.portfoliomanager.service.RecommendationService;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    
    private final RecommendationService recommendationService;
    private final ProjectionService projectionService;

    public AnalyticsController(RecommendationService recommendationService, ProjectionService projectionService) {
        this.recommendationService = recommendationService;
        this.projectionService = projectionService;
    }

    /**
     * Gets stock recommendation for the given symbol.
     * @param symbol
     * @return RecommendationResult
     */
    @GetMapping("/recommendation")
    public RecommendationResult getRecommendation(@RequestParam String symbol) {
        return recommendationService.getRecommendation(symbol);
    }

    /**
     * Gets portfolio projection until target age.
     * @param userId
     * @param targetAge
     * @return ProjectionResult
     */ 
    @GetMapping("/projection")
    public ProjectionResult getProjection(@RequestParam long userId, @RequestParam(defaultValue = "65") int targetAge)  {
            return projectionService.project(userId, targetAge);
    }
    
}