package com.skilldelity.portfoliomanager.service;

import org.springframework.stereotype.Service;

import com.skilldelity.portfoliomanager.engine.TechnicalIndicatorCalculator;
import com.skilldelity.portfoliomanager.model.ProjectionResult;

@Service
public class ProjectionService {
    
    private final TechnicalIndicatorCalculator technicalIndicatorCalculator;

    public ProjectionService(TechnicalIndicatorCalculator technicalIndicatorCalculator) {
        this.technicalIndicatorCalculator = technicalIndicatorCalculator;
    }

    public ProjectionResult project(long userId, int targetAge) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
