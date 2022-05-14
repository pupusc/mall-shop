package com.wanmi.sbc.goods.mapper;

import com.wanmi.sbc.goods.entity.MetaDataDict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * 数据字典(MetaDataDict)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Repository
public interface MetaDataDictMapper extends Mapper<MetaDataDict> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaDataDict queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaDataDict 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaDataDict> queryAllByLimit(@Param("metaDataDict") MetaDataDict metaDataDict, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaDataDict 查询条件
     * @return 总行数
     */
    long count(MetaDataDict metaDataDict);

    /**
     * 新增数据
     *
     * @param metaDataDict 实例对象
     * @return 影响行数
     */
    int insert(MetaDataDict metaDataDict);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaDataDict> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaDataDict> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaDataDict> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaDataDict> entities);

    /**
     * 修改数据
     *
     * @param metaDataDict 实例对象
     * @return 影响行数
     */
    int update(MetaDataDict metaDataDict);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

