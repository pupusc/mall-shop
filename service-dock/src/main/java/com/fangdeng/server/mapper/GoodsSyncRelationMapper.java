package com.fangdeng.server.mapper;

import com.fangdeng.server.entity.GoodsSyncRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;



@Mapper
public interface GoodsSyncRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_sync_relation
     *
     * @mbggenerated Wed Oct 13 17:17:18 CST 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_sync_relation
     *
     * @mbggenerated Wed Oct 13 17:17:18 CST 2021
     */
    int insert(GoodsSyncRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_sync_relation
     *
     * @mbggenerated Wed Oct 13 17:17:18 CST 2021
     */
    int insertSelective(GoodsSyncRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_sync_relation
     *
     * @mbggenerated Wed Oct 13 17:17:18 CST 2021
     */
    GoodsSyncRelation selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_sync_relation
     *
     * @mbggenerated Wed Oct 13 17:17:18 CST 2021
     */
    int updateByPrimaryKeySelective(GoodsSyncRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_sync_relation
     *
     * @mbggenerated Wed Oct 13 17:17:18 CST 2021
     */
    int updateByPrimaryKey(GoodsSyncRelation record);

    List<GoodsSyncRelation> list(@Param("id") Long id);

    List<String> listByGoodsNos(@Param("goodsNos") List<String> goodsNos);
}