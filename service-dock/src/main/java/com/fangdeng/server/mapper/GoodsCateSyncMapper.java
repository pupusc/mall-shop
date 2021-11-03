package com.fangdeng.server.mapper;

import com.fangdeng.server.dto.GoodsCateSyncDTO;
import com.fangdeng.server.dto.GoodsSyncDTO;

import java.util.List;

public interface GoodsCateSyncMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    int insert(GoodsCateSyncDTO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    int insertSelective(GoodsCateSyncDTO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    GoodsCateSyncDTO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    int updateByPrimaryKeySelective(GoodsCateSyncDTO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    int updateByPrimaryKey(GoodsCateSyncDTO record);

    int batchInsert(List<GoodsCateSyncDTO> list);
}