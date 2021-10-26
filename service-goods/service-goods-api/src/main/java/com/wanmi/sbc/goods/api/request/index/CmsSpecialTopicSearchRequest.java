package com.wanmi.sbc.goods.api.request.index;

import com.wanmi.sbc.goods.bean.enums.PublishState;
import lombok.Data;

@Data
public class CmsSpecialTopicSearchRequest {

    public int pageNum = 1;

    public int pageSize = 10;

    public Long id;

    public String name;

    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    public PublishState publishState;
}
