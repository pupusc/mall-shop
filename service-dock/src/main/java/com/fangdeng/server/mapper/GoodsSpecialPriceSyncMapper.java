package com.fangdeng.server.mapper;

import com.fangdeng.server.entity.GoodsPriceSync;
import com.fangdeng.server.entity.GoodsSpecialPriceSync;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsSpecialPriceSyncMapper {

    int deleteByPrimaryKey(Long id);


    int insert(GoodsSpecialPriceSync record);


    int insertSelective(GoodsSpecialPriceSync record);


    GoodsSpecialPriceSync selectByPrimaryKey(Long id);


    int updateByPrimaryKeySelective(GoodsSpecialPriceSync record);

    int updateByPrimaryKey(GoodsSpecialPriceSync record);

    int batchInsert(@Param("list") List<GoodsSpecialPriceSync> list);
}