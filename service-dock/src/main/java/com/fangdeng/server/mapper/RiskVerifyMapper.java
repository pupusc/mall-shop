package com.fangdeng.server.mapper;

import com.fangdeng.server.entity.RiskVerify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RiskVerifyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_verify
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */

    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_verify
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    int insert(RiskVerify record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_verify
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    int insertSelective(RiskVerify record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_verify
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    RiskVerify selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_verify
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    int updateByPrimaryKeySelective(RiskVerify record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_verify
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    int updateByPrimaryKey(RiskVerify record);

    int batchInsert(List<RiskVerify> list);

    int updateStatus(@Param("goodsNo")String goodsNo);
}