package com.wanmi.sbc.goods.api.request.blacklist;

import lombok.Data;

@Data
public class GoodsBlackListCreateOrUpdateRequest {

    private Integer id;

    /**
     * 0-启用 1-停用
     */
    private Integer delFlag;
}
