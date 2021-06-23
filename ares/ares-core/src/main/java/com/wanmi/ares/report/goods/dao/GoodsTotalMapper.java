package com.wanmi.ares.report.goods.dao;


import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.reponse.GoodsTotalResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 商品模块Mapper类
 * Created by dyt on 2017/10/12.
 */
@Mapper
public interface GoodsTotalMapper {

    /**
     * 批量保存Sku概览表
     * @param goodsTotals goodsTotals对象
     * @return 影响行数
     */
    int saveGoodsTotal(List<GoodsTotalResponse> goodsTotals);

    /**
     * 批量保存sku概览表
     * @param statDate
     * @return
     */
    int saveGoodsTotalForSelect( @Param("statDate") LocalDate statDate);

    /**
     * 根据ID查询昨日报表
     * @return 概览表
     */
    List<GoodsTotalResponse> findGoodsTotal(GoodsQueryCriteria criteria);

    Long queryGoodsCompanyInfoId(@Param("goodsInfoId") String goodsInfoId);

    void deleteGoodsTotalForSelect(@Param("statDate") LocalDate statDate);
}
