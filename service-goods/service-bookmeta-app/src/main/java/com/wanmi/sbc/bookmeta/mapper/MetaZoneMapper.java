package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaZone;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 图书分区(MetaZone)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@Repository
public interface MetaZoneMapper extends Mapper<MetaZone> {

    /**
     * 查询指定行数据
     *
     * @param metaZone 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaZone> queryList(@Param("metaZone") MetaZone metaZone, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaZone 查询条件
     * @return 总行数
     */
    int queryCount(MetaZone metaZone);
}

