package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaBookIndustryFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * 行业数据文件(MetaBookIndustryFile)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Repository
public interface MetaBookIndustryFileMapper extends Mapper<MetaBookIndustryFile> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaBookIndustryFile queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaBookIndustryFile 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaBookIndustryFile> queryAllByLimit(@Param("metaBookIndustryFile") MetaBookIndustryFile metaBookIndustryFile, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaBookIndustryFile 查询条件
     * @return 总行数
     */
    long count(MetaBookIndustryFile metaBookIndustryFile);

    /**
     * 新增数据
     *
     * @param metaBookIndustryFile 实例对象
     * @return 影响行数
     */
    int insert(MetaBookIndustryFile metaBookIndustryFile);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookIndustryFile> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaBookIndustryFile> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaBookIndustryFile> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaBookIndustryFile> entities);

    /**
     * 修改数据
     *
     * @param metaBookIndustryFile 实例对象
     * @return 影响行数
     */
    int update(MetaBookIndustryFile metaBookIndustryFile);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

