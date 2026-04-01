package com.fundtracker.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProfitStatistics {
    private BigDecimal totalInvestment;
    private BigDecimal currentValue;
    private BigDecimal totalProfit;
    private BigDecimal profitRate;
    private Integer fundCount;
}