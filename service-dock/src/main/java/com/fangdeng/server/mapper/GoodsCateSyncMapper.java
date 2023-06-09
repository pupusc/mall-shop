package com.fangdeng.server.mapper;

import com.fangdeng.server.entity.GoodsCateSync;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
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
    int insert(GoodsCateSync record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    int insertSelective(GoodsCateSync record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    GoodsCateSync selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    int updateByPrimaryKeySelective(GoodsCateSync record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_cate_sync
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    int updateByPrimaryKey(GoodsCateSync record);

    int batchInsert(List<GoodsCateSync> list);
}