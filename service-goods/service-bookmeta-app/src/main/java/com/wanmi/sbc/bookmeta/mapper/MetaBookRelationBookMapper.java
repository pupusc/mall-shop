package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaBookRelationBook;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/16:57
 * @Description:
 */
@Repository
public interface MetaBookRelationBookMapper {
    int insertMetaBookRelationBook(MetaBookRelationBook metaBookRelationBook);
    int updateMetaBookRelationBook(MetaBookRelationBook metaBookRelationBook);
    int deleteMetaBookRelationBook(int id);
    List<MetaBookRelationBook> getMetaBookRelationBook(@Param("relationId") int relationId);
}
