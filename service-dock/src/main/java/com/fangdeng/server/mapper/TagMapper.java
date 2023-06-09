package com.fangdeng.server.mapper;

import com.fangdeng.server.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface TagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tag
     *
     * @mbggenerated Tue Oct 12 18:31:38 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tag
     *
     * @mbggenerated Tue Oct 12 18:31:38 CST 2021
     */
    int insert(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tag
     *
     * @mbggenerated Tue Oct 12 18:31:38 CST 2021
     */
    int insertSelective(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tag
     *
     * @mbggenerated Tue Oct 12 18:31:38 CST 2021
     */
    Tag selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tag
     *
     * @mbggenerated Tue Oct 12 18:31:38 CST 2021
     */
    int updateByPrimaryKeySelective(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tag
     *
     * @mbggenerated Tue Oct 12 18:31:38 CST 2021
     */
    int updateByPrimaryKey(Tag record);

    int batchInsert(@Param("list") List<String> list);

    List<Tag> list();

    List<String> listTagName();
}