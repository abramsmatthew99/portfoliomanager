package com.skilldelity.portfoliomanager.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO that holds the results of technical analysis calculations.
 * This class encompasses all analysis results, containing both momentum indicators for
 * trading recommendations and growth statistics for retirement projections.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalAnalysisResults {
    
    /**
     * The Simple Moving Average over the last 20 periods (short-term trend).
     */
    private BigDecimal sma20;
    /**
     * The Simple Moving Average over the last 50 periods (medium-term trend).
     */
    private BigDecimal sma50;
    /**
     * The Simple Moving Average over the last 200 periods (long-term trend).
     */
    private BigDecimal sma200;
    /**
     * The maximum percentage drop from a peak to a trough over the analyzed period.
     * Expressed as a decimal (e.g., 0.20 for 20%).
     */
    private double maxDrawdown;

    /**
     * Compound Annual Growth Rate over the analyzed period.
     */
    private double cagr;
    /**
     * Annualized standard deviation of daily returns, representing risk.
     */
    private double volatility;
    /**
     * A calculated return rate adjusted for volatility risk.
     * Formula: CAGR - (0.5 * Volatility^2).
     */
    private double riskAdjustedReturn;

    /**
     * Determines if a "Golden Cross" event has occurred.
     *
     * @return true if the short-term trend (SMA 20) is greater than the medium-term trend (SMA 50).
     */
    public boolean isBullishCrossover() {
        if (sma20 == null || sma50 == null) return false;
        return sma20.compareTo(sma50) > 0; 
    }

    /**
     * Determines if the current price indicates a long-term bullish trend.
     *
     * @param currentPrice The most recent adjusted closing price.
     * @return true if the current price is strictly greater than the 200-day SMA.
     */
    public boolean isBullishTrend(BigDecimal currentPrice) {
        if (sma200 == null || currentPrice == null) return false;
        return currentPrice.compareTo(sma200) > 0;
    }
}
