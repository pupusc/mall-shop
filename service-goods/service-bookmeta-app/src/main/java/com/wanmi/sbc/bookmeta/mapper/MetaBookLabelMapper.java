package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.bo.MetaBookLabelQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryByPageBo;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 标签(MetaBookLabel)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Repository
public interface MetaBookLabelMapper extends Mapper<MetaBookLabel> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaBookLabel queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaBookLabel 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaBookLabel> queryAllByLimit(@Param("metaBookLabel") MetaBookLabel metaBookLabel, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaBookLabel 查询条件
     * @return 总行数
     */
    long count(MetaBookLabel metaBookLabel);

    /**
     * 新增数据
     *
     * @param metaBookLabel 实例对象
     * @return 影响行数
     */
    int insert(MetaBookLabel metaBookLabel);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookLabel> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaBookLabel> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookLabel> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaBookLabel> entities);

    /**
     * 修改数据
     *
     * @param metaBookLabel 实例对象
     * @return 影响行数
     */
    int update(MetaBookLabel metaBookLabel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 采集图书标签信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @param pageSize
     * @return
     */
    List<MetaBookLabel> collectMetaBookLabelByTime(
            @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("fromId") Integer fromId, @Param("pageSize") int pageSize);

    /**
     * 采集图书标签信息
     * @param labelIds
     * @return
     */
    List<MetaBookLabel> collectMetaBookLabel(@Param("labelIds") List<Integer> labelIds);

    /**
     * 采集图书标签信息
     * @return
     */
    List<Map> queryBookLable();


    List<MetaBookLabelQueryByPageReqBO> queryBookLableByPage(@Param("nameQuery") MetaBookQueryByPageBo nameQuery, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);
    int countBookLable(@Param("nameQuery") MetaBookQueryByPageBo nameQuery);

    /**
     * 查询图书标签关系是否存在
     * @return
     */
    List<MetaBookLabel> queryExitByBookAndLabelId(@Param("book_id") Integer bookId,@Param("label_id") Integer labelId);

    List<MetaBookLabelQueryByPageReqBO> getAllBook(@Param("name") String name,@Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    int getAllBookCount(@Param("name") String name);

    List<MetaBookLabelQueryByPageReqBO> getAllBookLabel(@Param("bo")MetaBookLabelQueryByPageReqBO pageReqBO,@Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    int getAllBookLabelCount(MetaBookLabelQueryByPageReqBO pageReqBO);

    int insertSelective(MetaBookLabel metaBookLabel);
}

