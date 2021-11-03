package com.fangdeng.server.mapper;

import com.fangdeng.server.dto.GoodsStockSyncDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsStockSyncMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_stock_sync
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_stock_sync
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    int insert(GoodsStockSyncDTO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_stock_sync
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    int insertSelective(GoodsStockSyncDTO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_stock_sync
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    GoodsStockSyncDTO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_stock_sync
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    int updateByPrimaryKeySelective(GoodsStockSyncDTO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_stock_sync
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    int updateByPrimaryKey(GoodsStockSyncDTO record);

    int batchInsert(@Param("list")List<GoodsStockSyncDTO> list);
}