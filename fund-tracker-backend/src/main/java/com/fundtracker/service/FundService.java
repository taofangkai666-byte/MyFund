package com.fundtracker.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fundtracker.dto.FundDTO;
import com.fundtracker.entity.Fund;
import com.fundtracker.repository.FundRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FundService implements FundDataService {

    private final FundRepository fundRepository;

    public FundService(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    public FundDTO getFundByCode(String fundCode) {
        Fund fund = fundRepository.findByFundCode(fundCode);
        if (fund == null) {
            // 从外部API获取基金数据（模拟）
            Fund fundData = fetchFundData(fundCode);
            fundRepository.insert(fundData);
            return convertToDTO(fundData);
        }
        return convertToDTO(fund);
    }

    public List<FundDTO> searchFunds(String keyword, Page<Fund> page) {
        List<Fund> funds;
        if (keyword == null || keyword.trim().isEmpty()) {
            funds = fundRepository.selectPage(page, null).getRecords();
        } else {
            funds = fundRepository.findByKeyword(page, keyword).getRecords();
        }

        return funds.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<Fund> getFundsPage(int page, int size, String keyword) {
        Page<Fund> fundPage = new Page<>(page, size);
        if (keyword == null || keyword.trim().isEmpty()) {
            return fundRepository.selectPage(fundPage, null);
        } else {
            return fundRepository.findByKeyword(fundPage, keyword);
        }
    }

    @Scheduled(fixedRate = 300000) // 每5分钟更新一次
    public void updateAllFundPrices() {
        List<Fund> funds = fundRepository.selectList(null);
        for (Fund fund : funds) {
            try {
                Fund updatedFund = fetchFundData(fund.getFundCode());
                fund.setCurrentPrice(updatedFund.getCurrentPrice());
                fund.setPriceDate(updatedFund.getPriceDate());
                fundRepository.updateById(fund);
            } catch (Exception e) {
                log.error("更新基金价格失败: " + fund.getFundCode(), e);
            }
        }
    }

    private FundDTO convertToDTO(Fund fund) {
        FundDTO dto = new FundDTO();
        BeanUtils.copyProperties(fund, dto);
        return dto;
    }
}