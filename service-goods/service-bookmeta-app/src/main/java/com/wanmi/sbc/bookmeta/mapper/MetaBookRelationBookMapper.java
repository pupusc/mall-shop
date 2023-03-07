package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaBookRelationBook;
import org.springframework.stereotype.Repository;

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
    int deleteMetaBookRelationBook(int id);
}
