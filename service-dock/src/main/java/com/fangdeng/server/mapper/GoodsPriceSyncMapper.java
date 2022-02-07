package com.fangdeng.server.mapper;

import com.fangdeng.server.entity.GoodsPriceSync;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface  GoodsPriceSyncMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_price_sync
     *
     * @mbggenerated Fri Sep 17 11:11:09 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_price_sync
     *
     * @mbggenerated Fri Sep 17 11:11:09 CST 2021
     */
    int insert(GoodsPriceSync record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_price_sync
     *
     * @mbggenerated Fri Sep 17 11:11:09 CST 2021
     */
    int insertSelective(GoodsPriceSync record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_price_sync
     *
     * @mbggenerated Fri Sep 17 11:11:09 CST 2021
     */
    GoodsPriceSync selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_price_sync
     *
     * @mbggenerated Fri Sep 17 11:11:09 CST 2021
     */
    int updateByPrimaryKeySelective(GoodsPriceSync record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_price_sync
     *
     * @mbggenerated Fri Sep 17 11:11:09 CST 2021
     */
    int updateByPrimaryKey(GoodsPriceSync record);

    int batchInsert(@Param("list") List<GoodsPriceSync> list);
}