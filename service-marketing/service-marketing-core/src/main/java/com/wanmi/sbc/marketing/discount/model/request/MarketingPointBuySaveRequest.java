package com.wanmi.sbc.marketing.discount.model.request;

import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingPointBuyLevel;
import lombok.Data;

import java.util.List;

@Data
public class MarketingPointBuySaveRequest extends MarketingSaveRequest {

    private List<MarketingPointBuyLevel> marketingPointBuyLevel;
}
