package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookExt;
import com.wanmi.sbc.bookmeta.entity.MetaBookV2;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 书籍(MetaBook)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-17 16:03:45
 */
@Repository
public interface MetaBookMapper extends Mapper<MetaBook> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaBook queryById(Integer id);

    MetaBookV2 queryByIdV2(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaBook 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaBook> queryAllByLimit(@Param("metaBook") MetaBook metaBook, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaBook 查询条件
     * @return 总行数
     */
    long count(@Param("metaBook") MetaBook metaBook);

    /**
     * 新增数据
     *
     * @param metaBook 实例对象
     * @return 影响行数
     */
    int insert(MetaBook metaBook);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBook> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaBook> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBook> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaBook> entities);

    /**
     * 修改数据
     *
     * @param metaBook 实例对象
     * @return 影响行数
     */
    int update(MetaBook metaBook);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int countExt(@Param("metaBook") MetaBookExt metaBook);

    List<MetaBook> queryAllByLimitExt(@Param("metaBook") MetaBookExt metaBook, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    int removeBookClumpId(Integer id);

    int removeBookGroupId(Integer id);

    int removeProducerId(Integer id);

    int removePublisherId(Integer id);

    /**
     * 采集book信息
     * @return
     */
    List<MetaBook> collectMetaBookByTime(
            @Param("beginTime")LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("fromId") Integer fromId, @Param("pageSize") int pageSize);

    /**
     * 采集书单信息
     * @param bookIds
     * @return
     */
    List<MetaBook> collectMetaBookByIds(@Param("bookIds") List<Integer> bookIds);

    /**
     * 采集书单信息
     * @param publisherIds
     * @return
     */
    List<MetaBook> collectMetaBookByPublisherIds(@Param("publisherIds") List<Integer> publisherIds);

    /**
     * 采集书单信息
     * @param bookGroupIds
     * @return
     */
    List<MetaBook> collectMetaBookByBookGroupIds(@Param("bookGroupIds") List<Integer> bookGroupIds);

    /**
     * 采集书单信息
     * @param bookProducerIds
     * @return
     */
    List<MetaBook> collectMetaBookByBookProducerIds(@Param("bookProducerIds") List<Integer> bookProducerIds);


    /**
     * 获取书单信息
     * @param isbns
     * @return
     */
    List<MetaBook> collectMetaBookByCondition(@Param("isbns") List<String> isbns);

    /**
     * 更新删除标志
     * @return
     */
    Boolean updateDelflag(@Param("id") Integer id,@Param("date") Date date);

    /**
     *
     * @return
     */
    List<Map> getAllBook();


    /**
     *通过spu获得图书信息
     * @return
     */
    List<Map> getBookInfoBySpu(@Param("spuId") String spuId);
}

