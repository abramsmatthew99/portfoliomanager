package com.skilldelity.portfoliomanager.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalAnalysisResults {
    
    //Buy and Sell statistics
    private BigDecimal sma20;
    private BigDecimal sma50;
    private BigDecimal sma200;
    private double maxDrawdown;

    //Growth Statistics
    private double cagr;
    private double volatility;
    private double riskAdjustedReturn;

    /*
    Compares sma20 to sma50. Returns true if Sma20 > sma50 for a positive crossover
    */
    public boolean isBullishCrossover() {
        if (sma20 == null || sma50 == null) return false;
        return sma20.compareTo(sma50) > 0; 
    }

    /*
     Compares the current price to the sma200. Returns true if
     the price is above the 200 day trend
     */
    public boolean isBullishTrend(BigDecimal currentPrice) {
        if (sma200 == null || currentPrice == null) return false;
        return currentPrice.compareTo(sma200) > 0;
    }
}
