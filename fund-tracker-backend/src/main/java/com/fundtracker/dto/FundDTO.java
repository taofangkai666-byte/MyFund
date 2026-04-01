package com.fundtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundDTO {
    private Long id;
    private String fundCode;
    private String fundName;
    private String fundType;
    private String company;
    private BigDecimal currentPrice;
    private LocalDate priceDate;
}