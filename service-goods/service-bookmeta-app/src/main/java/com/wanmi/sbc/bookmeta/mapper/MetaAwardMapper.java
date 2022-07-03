package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.entity.MetaBookClump;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 奖项(MetaAward)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Repository
public interface MetaAwardMapper extends Mapper<MetaAward> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaAward queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaAward 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaAward> queryAllByLimit(@Param("metaAward") MetaAward metaAward, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaAward 查询条件
     * @return 总行数
     */
    long count(MetaAward metaAward);

    /**
     * 新增数据
     *
     * @param metaAward 实例对象
     * @return 影响行数
     */
    int insert(MetaAward metaAward);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaAward> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaAward> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaAward> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaAward> entities);

    /**
     * 修改数据
     *
     * @param metaAward 实例对象
     * @return 影响行数
     */
    int update(MetaAward metaAward);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    /**
     * 采集奖项信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @param pageSize
     * @return
     */
    List<MetaAward> collectMetaAwardByTime(
            @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("fromId") Integer fromId, @Param("pageSize") int pageSize);

}

