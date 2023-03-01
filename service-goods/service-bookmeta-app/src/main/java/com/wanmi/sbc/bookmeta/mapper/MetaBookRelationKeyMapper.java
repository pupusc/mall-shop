package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.MetaBookRelationKey;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MetaBookRelationKeyMapper {
    /**
     *通过BookId获取主副标题
     * @return
     */
    List<MetaBookRelationKey> getKeyById(@Param("id") Integer id);

}
