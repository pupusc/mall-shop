package com.wanmi.sbc.bookmeta.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MetaBookRelationMapper {
    /**
     *通过BookId获取主副标题
     * @return
     */
    List<Map> getTitleByBookId(@Param("bookId") String bookId);

}
