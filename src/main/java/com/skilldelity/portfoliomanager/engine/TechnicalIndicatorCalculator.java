package com.skilldelity.portfoliomanager.engine;

import com.skilldelity.portfoliomanager.model.PricePoint;
import com.skilldelity.portfoliomanager.model.TechnicalAnalysisResults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Stateless engine responsible for performing financial mathematical calculations.
 * This component handles the computation of technical indicators like SMA
 * and statistical growth metrics such as CAGR.
 */
@Component
public class TechnicalIndicatorCalculator {
    
    //cconstants for rounding and trading
    private static final MathContext MC = new MathContext(4, RoundingMode.HALF_UP);
    private static final int TRADING_DAYS_PER_YEAR = 252;

    /**
     * Analyzes a list of historical price points to produce a comprehensive  report.
     * Calculates SMAs, Volatility, Max Drawdown, and Risk-Adjusted Return.
     *
     * @param history A list of PricePoint objects, assumed to be sorted chronologically.
     * @return A populated TechnicalAnalysisResults DTO containing all calculated metrics.
     */
    public TechnicalAnalysisResults analyze(List<PricePoint> history) {
        // Safety Check: Return empty default object if no data
        if (history == null || history.isEmpty()) {
            return new TechnicalAnalysisResults();
        }

        BigDecimal s20 = calculateSMA(history, 20);
        BigDecimal s50 = calculateSMA(history, 50);
        BigDecimal s200 = calculateSMA(history, 200);
        
        double dd = calculateMaxDrawdown(history);
        double vol = calculateAnnualizedVolatility(history);
        double cagr = calculateCAGR(history);

        //  Calculate Risk-Adjusted Return
        // Formula: CAGR - (0.5 * Volatility^2)
        double riskAdj = cagr - (0.5 * Math.pow(vol, 2));

        return TechnicalAnalysisResults.builder()
                .sma20(s20)
                .sma50(s50)
                .sma200(s200)
                .maxDrawdown(dd)
                .volatility(vol)
                .cagr(cagr)
                .riskAdjustedReturn(riskAdj)
                .build();
    }


    /**
     * Projects the future value of an investment based on a compound rate.
     * Formula: PV * (1 + rate)^years.
     *
     * @param currentBalance The starting value of the investment.
     * @param rate The annual growth rate to apply (preferably Risk-Adjusted Return).
     * @param years The number of years to project into the future.
     * @return The projected future value as a double.
     */
    public double projectFutureValue(BigDecimal currentBalance, double rate, int years) {
        if (currentBalance == null) return 0.0;
        return currentBalance.doubleValue() * Math.pow(1 + rate, years);
    }


    /**
     * Calculates the Simple Moving Average (SMA) for a specific period.
     *
     * @param history The full price history.
     * @param period The lookback period.
     * @return The average price over the period, or ZERO if insufficient history exists.
     */
    private BigDecimal calculateSMA(List<PricePoint> history, int period) {
        if (history.size() < period) return BigDecimal.ZERO;

        
        // Currently assuming data is sorted oldest to newest
        int startIndex = history.size() - period;
        List<PricePoint> window = history.subList(startIndex, history.size());

        BigDecimal sum = BigDecimal.ZERO;
        for (PricePoint p : window) {
            sum = sum.add(p.getAdjClose());
        }

        return sum.divide(BigDecimal.valueOf(period), MC);
    }

    /**
     * Calculates the annualized volatility (standard deviation) based on daily returns.
     *
     * @param history The full price history.
     * @return The annualized volatility as a decimal.
     */
    private double calculateAnnualizedVolatility(List<PricePoint> history) {
        if (history.size() < 2) return 0.0;

        //  Calculate Daily Returns
        double[] returns = new double[history.size() - 1];
        for (int i = 1; i < history.size(); i++) {
            double today = history.get(i).getAdjClose().doubleValue();
            double yesterday = history.get(i - 1).getAdjClose().doubleValue();

            if (yesterday == 0) returns[i - 1] = 0.0;
            else returns[i - 1] = (today - yesterday) / yesterday;
        }

        //  Get Mean of Prices
        double sum = 0.0;
        for (double r : returns) sum += r;
        double mean = sum / returns.length;

        // Get Variance
        double varianceSum = 0.0;
        for (double r : returns) varianceSum += Math.pow(r - mean, 2);
        
        
        double stdDev = Math.sqrt(varianceSum / (returns.length - 1));

        //  Annualize: StdDev * Sqrt(252)
        return stdDev * Math.sqrt(TRADING_DAYS_PER_YEAR);
    }

    /**
     * Calculates the maximum drawdown (largest percentage drop from peak) in the last year.
     *
     * @param history The full price history.
     * @return The max drawdown as a positive decimal.
     */
    private double calculateMaxDrawdown(List<PricePoint> history) {
        // Look at the last year or full history if shorter
        int lookback = Math.min(history.size(), TRADING_DAYS_PER_YEAR);
        int startIndex = history.size() - lookback;
        
        double maxDrawdown = 0.0;
        double peak = -1.0;

        for (int i = startIndex; i < history.size(); i++) {
            double price = history.get(i).getAdjClose().doubleValue();
            
            // Update Peak
            if (price > peak) {
                peak = price;
            }

            // Update Max Drawdown if current drop is larger
            double drawdown = (peak - price) / peak;
            if (drawdown > maxDrawdown) {
                maxDrawdown = drawdown;
            }
        }
        return maxDrawdown; //returns percent as a decimal
    }

    /**
     * Calculates the Compound Annual Growth Rate (CAGR) over the available history.
     *
     * @param history The full price history.
     * @return The CAGR as a decimal.
     */
    private double calculateCAGR(List<PricePoint> history) {
        if (history.isEmpty()) return 0.0;

        double startPrice = history.get(0).getAdjClose().doubleValue();
        double endPrice = history.get(history.size() - 1).getAdjClose().doubleValue();
        
        // Calculate years accurately based on data size
        double years = history.size() / (double) TRADING_DAYS_PER_YEAR;

        if (startPrice <= 0 || years <= 0) return 0.0;

        // Formula: (End / Start)^(1 / Years) - 1
        return Math.pow(endPrice / startPrice, 1.0 / years) - 1.0;
    }
}
