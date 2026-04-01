package com.fundtracker.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fundtracker.dto.*;
import com.fundtracker.entity.Fund;
import com.fundtracker.entity.FundHolding;
import com.fundtracker.repository.FundHoldingRepository;
import com.fundtracker.repository.FundRepository;
import com.fundtracker.service.FundService;
import com.fundtracker.service.ProfitService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FundController {

    private final FundService fundService;
    private final FundHoldingRepository holdingRepository;
    private final FundRepository fundRepository;
    private final ProfitService profitService;

    public FundController(FundService fundService, FundHoldingRepository holdingRepository,
                         FundRepository fundRepository, ProfitService profitService) {
        this.fundService = fundService;
        this.holdingRepository = holdingRepository;
        this.fundRepository = fundRepository;
        this.profitService = profitService;
    }

    @GetMapping("/funds")
    public ApiResponse<Map<String, Object>> getFunds(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {

        try {
            Page<Fund> fundPage = fundService.getFundsPage(page, size, keyword);
            List<FundDTO> fundDTOs = fundPage.getRecords().stream()
                    .map(fund -> FundDTO.builder()
                            .id(fund.getId())
                            .fundCode(fund.getFundCode())
                            .fundName(fund.getFundName())
                            .fundType(fund.getFundType())
                            .company(fund.getCompany())
                            .currentPrice(fund.getCurrentPrice())
                            .priceDate(fund.getPriceDate())
                            .build())
                    .collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("total", fundPage.getTotal());
            data.put("list", fundDTOs);

            return ApiResponse.success(data);
        } catch (Exception e) {
            return ApiResponse.error("获取基金列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/holdings")
    public ApiResponse<Map<String, Long>> addHolding(@RequestBody AddHoldingRequest request) {
        try {
            // 获取或创建基金
            Fund fund = fundRepository.findByFundCode(request.getFundCode());
            if (fund == null) {
                fund = fundService.fetchFundData(request.getFundCode());
                fundRepository.insert(fund);
            }

            // 创建持仓记录
            FundHolding holding = new FundHolding();
            holding.setUserId(1L); // 临时使用固定用户ID
            holding.setFundId(fund.getId());
            holding.setBuyPrice(request.getBuyPrice());
            holding.setShares(request.getShares());
            holding.setBuyDate(request.getBuyDate());
            holding.setTotalCost(request.getBuyPrice().multiply(request.getShares()));

            holdingRepository.insert(holding);

            Map<String, Long> data = new HashMap<>();
            data.put("holdingId", holding.getId());

            return ApiResponse.success(data);
        } catch (Exception e) {
            return ApiResponse.error("添加持仓失败: " + e.getMessage());
        }
    }

    @GetMapping("/holdings")
    public ApiResponse<List<FundHolding>> getHoldings() {
        try {
            // 临时使用固定用户ID
            Long userId = 1L;

            List<FundHolding> holdings = holdingRepository.findByUserIdWithFundInfo(userId);

            // 计算收益
            holdings = profitService.calculateHoldingsProfit(holdings);

            return ApiResponse.success(holdings);
        } catch (Exception e) {
            return ApiResponse.error("获取持仓失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/holdings/{id}")
    public ApiResponse<Void> deleteHolding(@PathVariable Long id) {
        try {
            holdingRepository.deleteById(id);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error("删除持仓失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/profit")
    public ApiResponse<ProfitStatistics> getProfitStatistics() {
        try {
            // 临时使用固定用户ID
            Long userId = 1L;

            ProfitStatistics statistics = profitService.calculateProfit(userId);
            return ApiResponse.success(statistics);
        } catch (Exception e) {
            return ApiResponse.error("获取收益统计失败: " + e.getMessage());
        }
    }
}