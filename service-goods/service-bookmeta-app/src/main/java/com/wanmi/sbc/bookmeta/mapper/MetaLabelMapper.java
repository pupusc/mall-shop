package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.entity.MetaLabelExt;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 标签(MetaLabel)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-17 11:38:03
 */
@Repository
public interface MetaLabelMapper extends Mapper<MetaLabel> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaLabel queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaLabel 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaLabel> queryAllByLimit(@Param("metaLabel") MetaLabel metaLabel, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaLabel 查询条件
     * @return 总行数
     */
    long count(@Param("metaLabel") MetaLabel metaLabel);

    /**
     * 新增数据
     *
     * @param metaLabel 实例对象
     * @return 影响行数
     */
    int insert(MetaLabel metaLabel);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaLabel> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaLabel> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaLabel> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaLabel> entities);

    /**
     * 修改数据
     *
     * @param metaLabel 实例对象
     * @return 影响行数
     */
    int update(MetaLabel metaLabel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int countExt(@Param("metaLabel") MetaLabel metaLabel);

    List<MetaLabelExt> queryAllByLimitExt(@Param("metaLabel") MetaLabel metaLabel, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);
}
