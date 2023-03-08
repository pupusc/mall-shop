package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaBookRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MetaBookRelationMapper {
    /**
     *通过BookId获取主副标题
     * @return
     */
    List<Map> getTitleByBookId(@Param("bookId") String bookId);
    int insertSelective(MetaBookRelation metaBookRelation);
    int deleteSelective(int id);
    List<MetaBookRelation> getMetaBookRelation(@Param("bookId") int bookId);
    int updateMetaBookRelation(MetaBookRelation metaBookRelation);




}
