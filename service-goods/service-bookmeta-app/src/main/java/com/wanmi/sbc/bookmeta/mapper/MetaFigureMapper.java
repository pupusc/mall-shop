package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 人物(MetaFigure)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-16 11:26:58
 */
@Repository
public interface MetaFigureMapper extends Mapper<MetaFigure> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaFigure queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaFigure 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaFigure> queryAllByLimit(@Param("metaFigure") MetaFigure metaFigure, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaFigure 查询条件
     * @return 总行数
     */
    long count(MetaFigure metaFigure);

    /**
     * 新增数据
     *
     * @param metaFigure 实例对象
     * @return 影响行数
     */
    int insert(MetaFigure metaFigure);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaFigure> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaFigure> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaFigure> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaFigure> entities);

    /**
     * 修改数据
     *
     * @param metaFigure 实例对象
     * @return 影响行数
     */
    int update(MetaFigure metaFigure);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 采集作者信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @param pageSize
     * @return
     */
    List<MetaFigure> collectMetaFigureByTime(
            @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("fromId") Integer fromId, @Param("pageSize") int pageSize);
}

