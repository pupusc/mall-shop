package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 出品方(MetaProducer)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Repository
public interface MetaProducerMapper extends Mapper<MetaProducer> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaProducer queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaProducer 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaProducer> queryAllByLimit(@Param("metaProducer") MetaProducer metaProducer, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaProducer 查询条件
     * @return 总行数
     */
    long count(MetaProducer metaProducer);

    /**
     * 新增数据
     *
     * @param metaProducer 实例对象
     * @return 影响行数
     */
    int insert(MetaProducer metaProducer);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaProducer> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaProducer> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaProducer> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaProducer> entities);

    /**
     * 修改数据
     *
     * @param metaProducer 实例对象
     * @return 影响行数
     */
    int update(MetaProducer metaProducer);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    /**
     * 采集出品方信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @param pageSize
     * @return
     */
    List<MetaProducer> collectMetaProducerByTime(
            @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("fromId") Integer fromId, @Param("pageSize") int pageSize);
}

