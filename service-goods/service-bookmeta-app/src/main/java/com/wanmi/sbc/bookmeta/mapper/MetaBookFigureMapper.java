package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 书籍人物(MetaBookFigure)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Repository
public interface MetaBookFigureMapper extends Mapper<MetaBookFigure> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaBookFigure queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaBookFigure 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaBookFigure> queryAllByLimit(@Param("metaBookFigure") MetaBookFigure metaBookFigure, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaBookFigure 查询条件
     * @return 总行数
     */
    long count(MetaBookFigure metaBookFigure);

    /**
     * 新增数据
     *
     * @param metaBookFigure 实例对象
     * @return 影响行数
     */
    int insert(MetaBookFigure metaBookFigure);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookFigure> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaBookFigure> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookFigure> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaBookFigure> entities);

    /**
     * 修改数据
     *
     * @param metaBookFigure 实例对象
     * @return 影响行数
     */
    int update(MetaBookFigure metaBookFigure);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    /**
     * 采集 图书作者关联信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @param pageSize
     * @return
     */
    List<MetaBookFigure> collectMetaBookFigureByTime(
            @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("fromId") Integer fromId, @Param("pageSize") int pageSize);


    /**
     * 采集 根据 根据人物信息 获取 图书人物信息信息
     * @param figureIds
     * @return
     */
    List<MetaBookFigure> collectMetaBookFigureByIds(@Param("ids") List<Integer> figureIds);


    /**
     * 根据bookId获取书籍推荐人
     * @return
     */
    List<MetaFigure> getBookFigureByBookId(@Param("bookId") Integer bookId);

    /**
     * 根据figureId获取isbn
     * @return
     */
    List<String> getIsbnByFigure(@Param("figureId") Integer figureId,@Param("bookId") Integer bookId);
}

