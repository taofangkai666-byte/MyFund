package com.fundtracker.service;

import com.fundtracker.dto.ProfitStatistics;
import com.fundtracker.entity.Fund;
import com.fundtracker.entity.FundHolding;
import com.fundtracker.repository.FundHoldingRepository;
import com.fundtracker.repository.FundRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ProfitService {

    private final FundHoldingRepository holdingRepository;
    private final FundRepository fundRepository;

    public ProfitService(FundHoldingRepository holdingRepository, FundRepository fundRepository) {
        this.holdingRepository = holdingRepository;
        this.fundRepository = fundRepository;
    }

    public ProfitStatistics calculateProfit(Long userId) {
        // 临时使用userId=1，后续需要用户系统
        if (userId == null) {
            userId = 1L;
        }

        // 获取所有持仓
        java.util.List<FundHolding> holdings = holdingRepository.findByUserId(userId);

        BigDecimal totalInvestment = BigDecimal.ZERO;
        BigDecimal currentValue = BigDecimal.ZERO;

        for (FundHolding holding : holdings) {
            Fund fund = fundRepository.selectById(holding.getFundId());
            if (fund != null) {
                BigDecimal cost = holding.getBuyPrice().multiply(holding.getShares());
                BigDecimal current = fund.getCurrentPrice().multiply(holding.getShares());

                totalInvestment = totalInvestment.add(cost);
                currentValue = currentValue.add(current);
            }
        }

        BigDecimal profit = currentValue.subtract(totalInvestment);
        BigDecimal profitRate = totalInvestment.compareTo(BigDecimal.ZERO) > 0
                ? profit.divide(totalInvestment, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                : BigDecimal.ZERO;

        return ProfitStatistics.builder()
                .totalInvestment(totalInvestment)
                .currentValue(currentValue)
                .totalProfit(profit)
                .profitRate(profitRate)
                .fundCount(holdings.size())
                .build();
    }

    public java.util.List<FundHolding> calculateHoldingsProfit(java.util.List<FundHolding> holdings) {
        for (FundHolding holding : holdings) {
            if (holding.getFund() != null) {
                Fund fund = holding.getFund();

                // 计算当前市值
                BigDecimal currentValue = fund.getCurrentPrice().multiply(holding.getShares());
                holding.setCurrentPrice(fund.getCurrentPrice());
                holding.setCurrentValue(currentValue);

                // 计算收益
                BigDecimal profit = currentValue.subtract(holding.getTotalCost());
                holding.setProfit(profit);

                // 计算收益率
                BigDecimal profitRate = holding.getTotalCost().compareTo(BigDecimal.ZERO) > 0
                        ? profit.divide(holding.getTotalCost(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                        : BigDecimal.ZERO;
                holding.setProfitRate(profitRate);
            }
        }
        return holdings;
    }
}