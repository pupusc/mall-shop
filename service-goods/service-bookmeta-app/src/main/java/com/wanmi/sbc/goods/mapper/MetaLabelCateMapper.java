package com.wanmi.sbc.goods.mapper;

import com.wanmi.sbc.goods.entity.MetaLabelCate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * 标签目录(MetaLabelCate)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Repository
public interface MetaLabelCateMapper extends Mapper<MetaLabelCate> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaLabelCate queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaLabelCate 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaLabelCate> queryAllByLimit(@Param("metaLabelCate") MetaLabelCate metaLabelCate, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaLabelCate 查询条件
     * @return 总行数
     */
    long count(MetaLabelCate metaLabelCate);

    /**
     * 新增数据
     *
     * @param metaLabelCate 实例对象
     * @return 影响行数
     */
    int insert(MetaLabelCate metaLabelCate);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaLabelCate> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaLabelCate> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaLabelCate> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaLabelCate> entities);

    /**
     * 修改数据
     *
     * @param metaLabelCate 实例对象
     * @return 影响行数
     */
    int update(MetaLabelCate metaLabelCate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

