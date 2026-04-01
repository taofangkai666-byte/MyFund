package com.fundtracker.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("fund_holding")
public class FundHolding {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("fund_id")
    private Long fundId;

    @TableField("buy_price")
    private BigDecimal buyPrice;

    @TableField("shares")
    private BigDecimal shares;

    @TableField("buy_date")
    private LocalDate buyDate;

    @TableField("total_cost")
    private BigDecimal totalCost;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 关联的基金信息
    @TableField(exist = false)
    private Fund fund;

    // 计算字段
    @TableField(exist = false)
    private BigDecimal currentPrice;

    @TableField(exist = false)
    private BigDecimal currentValue;

    @TableField(exist = false)
    private BigDecimal profit;

    @TableField(exist = false)
    private BigDecimal profitRate;
}