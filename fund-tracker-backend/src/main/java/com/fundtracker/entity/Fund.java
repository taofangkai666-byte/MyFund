package com.fundtracker.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("fund")
public class Fund {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("fund_code")
    private String fundCode;

    @TableField("fund_name")
    private String fundName;

    @TableField("fund_type")
    private String fundType;

    @TableField("company")
    private String company;

    @TableField("current_price")
    private BigDecimal currentPrice;

    @TableField("price_date")
    private LocalDate priceDate;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}