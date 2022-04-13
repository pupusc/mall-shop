package com.wanmi.sbc.goods.api.request.blacklist;

import lombok.Data;

@Data
public class GoodsBlackListSearchRequest {

    /**
     * 黑名单类型
     */
    private Integer businessCategory;

    /**
     * 业务主键
     */
    private String businessId;
}
