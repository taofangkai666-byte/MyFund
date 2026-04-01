package com.fundtracker.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fundtracker.entity.Fund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FundRepository extends BaseMapper<Fund> {

    @Select("SELECT * FROM fund WHERE fund_code = #{fundCode}")
    Fund findByFundCode(@Param("fundCode") String fundCode);

    @Select("SELECT * FROM fund WHERE fund_name LIKE CONCAT('%', #{keyword}, '%') OR fund_code LIKE CONCAT('%', #{keyword}, '%')")
    List<Fund> findByKeyword(@Param("keyword") String keyword);

    @Select("SELECT * FROM fund WHERE fund_name LIKE CONCAT('%', #{keyword}, '%') OR fund_code LIKE CONCAT('%', #{keyword}, '%')")
    Page<Fund> findByKeyword(Page<Fund> page, @Param("keyword") String keyword);
}