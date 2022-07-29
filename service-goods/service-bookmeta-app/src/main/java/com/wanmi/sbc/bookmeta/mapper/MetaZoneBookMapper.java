package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaZoneBook;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * 图书分区关联图书(MetaZoneBook)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:37
 */
@Repository
public interface MetaZoneBookMapper extends Mapper<MetaZoneBook> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaZoneBook queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaZoneBook 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaZoneBook> queryAllByLimit(@Param("metaZoneBook") MetaZoneBook metaZoneBook, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaZoneBook 查询条件
     * @return 总行数
     */
    long count(MetaZoneBook metaZoneBook);

    /**
     * 新增数据
     *
     * @param metaZoneBook 实例对象
     * @return 影响行数
     */
    int insert(MetaZoneBook metaZoneBook);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaZoneBook> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaZoneBook> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaZoneBook> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaZoneBook> entities);

    /**
     * 修改数据
     *
     * @param metaZoneBook 实例对象
     * @return 影响行数
     */
    int update(MetaZoneBook metaZoneBook);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

