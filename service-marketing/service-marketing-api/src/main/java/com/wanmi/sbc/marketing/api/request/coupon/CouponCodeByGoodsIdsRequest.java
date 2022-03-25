package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.common.enums.ChannelType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CouponCodeByGoodsIdsRequest implements Serializable {
    private static final long serialVersionUID = 4413953748731762194L;
    /**
     * 商品Id
     */
    private List<String> couponIds;

    private String customerId;
}
