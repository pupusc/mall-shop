package com.soybean.mall.cart.vo;

import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import lombok.Data;

/**
 * @author Liang Jun
 * @date 2022-06-16 19:13:00
 */
@Data
public class PromoteInfoResultVO$Marketing {
    /**
     * 营销Id
     */
    private Long marketingId;
    /**
     * 营销名称
     */
    private String marketingName;

    /**
     * 营销类型 0：满减 1:满折 2:满赠
     */
    private MarketingType marketingType;

    /**
     * 营销类型文案
     */
    private String marketingTypeText;

    /**
     * 营销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠
     */
    private MarketingSubType subType;
}
