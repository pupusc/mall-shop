package com.wanmi.sbc.marketing.gift.response;


import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 满赠信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingFullGiftLevelResponse{

    /**
     * 活动规则列表
     */
    private List<MarketingFullGiftLevel> levelList = new ArrayList<>();

    /**
     * 赠品列表
     */
    private List<GoodsInfoVO> giftList = new ArrayList<>();
}
