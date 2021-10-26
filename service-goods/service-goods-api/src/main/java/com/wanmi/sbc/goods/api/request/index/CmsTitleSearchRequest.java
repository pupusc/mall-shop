package com.wanmi.sbc.goods.api.request.index;

import lombok.Data;

@Data
public class CmsTitleSearchRequest {

    public int pageNum = 1;

    public int pageSize = 10;

    public Long id;

    public String code;

    /**
     * 0-未启用 1-启用
     */
    public Integer publishState;
}
