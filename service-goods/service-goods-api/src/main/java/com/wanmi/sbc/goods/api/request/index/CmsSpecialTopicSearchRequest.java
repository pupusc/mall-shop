package com.wanmi.sbc.goods.api.request.index;

import com.wanmi.sbc.goods.bean.enums.PublishState;
import lombok.Data;

import java.util.List;

@Data
public class CmsSpecialTopicSearchRequest {

    public int pageNum = 0;

    public int pageSize = 10;

    public Long id;

    public String name;

    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    /**
     * 0-未启用 1-启用
     */
    public Integer publishState;

    /**
     * 特色栏目id
     */
    public List<Integer> idColl;
}
