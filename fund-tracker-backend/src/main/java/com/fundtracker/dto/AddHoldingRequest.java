package com.fundtracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddHoldingRequest {
    private String fundCode;
    private BigDecimal buyPrice;
    private BigDecimal shares;
    private LocalDate buyDate;
}