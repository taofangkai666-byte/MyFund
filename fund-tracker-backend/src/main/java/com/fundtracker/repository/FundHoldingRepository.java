package com.fundtracker.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fundtracker.entity.FundHolding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FundHoldingRepository extends BaseMapper<FundHolding> {

    @Select("SELECT h.*, f.fund_code, f.fund_name, f.fund_type, f.company, f.current_price, f.price_date " +
            "FROM fund_holding h " +
            "LEFT JOIN fund f ON h.fund_id = f.id " +
            "WHERE h.user_id = #{userId}")
    List<FundHolding> findByUserIdWithFundInfo(@Param("userId") Long userId);

    @Select("SELECT * FROM fund_holding WHERE user_id = #{userId}")
    List<FundHolding> findByUserId(@Param("userId") Long userId);
}