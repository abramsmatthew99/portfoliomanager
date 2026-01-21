package com.skilldelity.portfoliomanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Represents a single data point in a financial time series.
 * This is used to pass date and price information
 * between the Service and Engine layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricePoint {
    
    private LocalDate date;
    private BigDecimal adjClose;
}
