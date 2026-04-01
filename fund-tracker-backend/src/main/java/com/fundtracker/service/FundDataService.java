package com.fundtracker.service;

import com.fundtracker.entity.Fund;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

public interface FundDataService {

    /**
     * 模拟获取基金数据
     * 在实际项目中，这里会调用第三方基金API
     */
    default Fund fetchFundData(String fundCode) {
        Random random = new Random();

        Fund fund = new Fund();
        fund.setFundCode(fundCode);
        fund.setFundName(getFundNameByCode(fundCode));
        fund.setFundType("混合型");
        fund.setCompany("基金公司");
        fund.setCurrentPrice(BigDecimal.valueOf(1 + random.nextDouble() * 2)); // 1-3之间的随机价格
        fund.setPriceDate(LocalDate.now());

        return fund;
    }

    private String getFundNameByCode(String fundCode) {
        // 模拟基金名称映射
        return switch (fundCode) {
            case "000001" -> "华夏成长混合";
            case "000002" -> "南方稳健成长";
            case "110022" -> "易方达消费行业";
            case "161725" -> "招商中证白酒";
            case "000478" -> "建信中证500指数增强";
            default -> fundCode + "基金";
        };
    }
}