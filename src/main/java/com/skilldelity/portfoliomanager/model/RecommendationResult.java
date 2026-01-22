package com.skilldelity.portfoliomanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This represents the Buy / Sell / Hold recommendation for a single stock.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResult {

    // The stock ticker symbol
    private String ticker;

    // The recommended action based on historical analysis.
    private Action action;

    // Confidence score (0–100) representing strength of the recommendation.

    private int confidence;

    // Explanation describing why this recommendation was made.
    private String rationale;

    // Allowed recommendation actions. Fixed list of allowed values.
    public enum Action {
        BUY,
        SELL,
        HOLD
    }
}
