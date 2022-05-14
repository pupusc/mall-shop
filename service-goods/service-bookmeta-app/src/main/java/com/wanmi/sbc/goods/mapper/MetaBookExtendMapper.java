package com.wanmi.sbc.goods.mapper;

import com.wanmi.sbc.goods.entity.MetaBookExtend;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * 书籍扩展属性(MetaBookExtend)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Repository
public interface MetaBookExtendMapper extends Mapper<MetaBookExtend> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaBookExtend queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaBookExtend 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaBookExtend> queryAllByLimit(@Param("metaBookExtend") MetaBookExtend metaBookExtend, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaBookExtend 查询条件
     * @return 总行数
     */
    long count(MetaBookExtend metaBookExtend);

    /**
     * 新增数据
     *
     * @param metaBookExtend 实例对象
     * @return 影响行数
     */
    int insert(MetaBookExtend metaBookExtend);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookExtend> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaBookExtend> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookExtend> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaBookExtend> entities);

    /**
     * 修改数据
     *
     * @param metaBookExtend 实例对象
     * @return 影响行数
     */
    int update(MetaBookExtend metaBookExtend);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

