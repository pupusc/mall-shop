package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 出版社(MetaPublisher)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:03
 */
@Repository
public interface MetaPublisherMapper extends Mapper<MetaPublisher> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaPublisher queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaPublisher 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaPublisher> queryAllByLimit(@Param("metaPublisher") MetaPublisher metaPublisher, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaPublisher 查询条件
     * @return 总行数
     */
    long count(MetaPublisher metaPublisher);

    /**
     * 新增数据
     *
     * @param metaPublisher 实例对象
     * @return 影响行数
     */
    int insert(MetaPublisher metaPublisher);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaPublisher> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaPublisher> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaPublisher> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaPublisher> entities);

    /**
     * 修改数据
     *
     * @param metaPublisher 实例对象
     * @return 影响行数
     */
    int update(MetaPublisher metaPublisher);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    /**
     * 采集出版社信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @param pageSize
     * @return
     */
    List<MetaPublisher> collectMetaPublisherByTime(
            @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("fromId") Integer fromId, @Param("pageSize") int pageSize);

//    /**
//     * 采集 根据id获取 出版社信息
//     * @param ids
//     * @return
//     */
//    List<MetaPublisher> collectMetaPublisherByIds(@Param("ids") List<Integer> ids);
}

