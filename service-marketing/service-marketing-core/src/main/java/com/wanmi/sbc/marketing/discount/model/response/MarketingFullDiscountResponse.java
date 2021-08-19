package com.wanmi.sbc.marketing.discount.model.response;


import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.model.root.MarketingScope;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingFullDiscountLevel;
import lombok.Data;

import java.util.List;

/**
 * 营销满折规则
 */
@Data
public class MarketingFullDiscountResponse {
    /**
     * 新增/编辑营销实体对象
     */
    private Marketing marketing;

    /**
     * 新增/编辑满折多级实体对象
     */
    private List<MarketingFullDiscountLevel> discountLevels;

    /**
     * 新增/编辑满折范围实体对象
     */
    private List<MarketingScope> scopes;
}
