package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdFigureBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * 书籍推荐(MetaBookRcmmd)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-25 16:54:55
 */
@Repository
public interface MetaBookRcmmdMapper extends Mapper<MetaBookRcmmd> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaBookRcmmd queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaBookRcmmd 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaBookRcmmd> queryAllByLimit(@Param("metaBookRcmmd") MetaBookRcmmd metaBookRcmmd, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaBookRcmmd 查询条件
     * @return 总行数
     */
    long count(MetaBookRcmmd metaBookRcmmd);

    /**
     * 新增数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 影响行数
     */
    int insert(MetaBookRcmmd metaBookRcmmd);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookRcmmd> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaBookRcmmd> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookRcmmd> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaBookRcmmd> entities);

    /**
     * 修改数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 影响行数
     */
    int update(MetaBookRcmmd metaBookRcmmd);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据业务类型获取 推荐信息
     * @param bizIds
     * @param bizTypes
     * @return
     */
    List<MetaBookRcmmd> collectMetaBookRcmmd(@Param("bizIds") List<Integer> bizIds, @Param("bizTypes") List<Integer> bizTypes);

    /**
     * 根据书籍id获取 推荐信息
     * @return
     */
    List<MetaBookRcmmdFigureBO> RcommdFigureByBookId(@Param("bookId") Integer bookId);

    /**
     * 根据推荐人id获取 推荐列表
     * @return
     */
    List<String> RcommdBookByFigureId(@Param("bizId") Integer bizId,@Param("bookId") Integer bookId);
}

