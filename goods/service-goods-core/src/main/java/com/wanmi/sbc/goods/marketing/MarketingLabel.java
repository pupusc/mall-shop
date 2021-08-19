package com.wanmi.sbc.goods.marketing;

import lombok.Data;

/**
 * 营销标签
 * Created by dyt on 2018/2/28.
 */
@Data
public class MarketingLabel {

    /**
     * 营销编号
     */
    private Long marketingId;

    /**
     * 促销类型 0：满减 1:满折 2:满赠
     * 与Marketing.marketingType保持一致
     */
    private Integer marketingType;

    /**
     * 促销描述
     */
    private String marketingDesc;

}
