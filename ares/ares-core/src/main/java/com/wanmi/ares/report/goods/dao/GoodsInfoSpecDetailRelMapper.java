package com.wanmi.ares.report.goods.dao;


import com.wanmi.ares.source.model.root.GoodsInfoSpecDetailRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Sku报表Mapper类
 * Created by dyt on 2017/11/12.
 */
@Mapper
public interface GoodsInfoSpecDetailRelMapper {

    List<GoodsInfoSpecDetailRel> queryByGoodsId(@Param("ids")List<String> ids,@Param("delFlag") Integer delFlag);

    List<GoodsInfoSpecDetailRel> queryDetailNameGroupByGoodsId(@Param("ids")List<String> ids,@Param("delFlag") Integer delFlag);
}
