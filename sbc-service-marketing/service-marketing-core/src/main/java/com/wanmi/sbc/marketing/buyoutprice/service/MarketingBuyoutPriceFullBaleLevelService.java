package com.wanmi.sbc.marketing.buyoutprice.service;

import com.wanmi.sbc.marketing.bean.vo.MarketingPageVO;
import com.wanmi.sbc.marketing.buyoutprice.model.entry.MarketingBuyoutPriceLevel;
import com.wanmi.sbc.marketing.buyoutprice.repository.MarketingBuyoutPriceLevelRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 一口价营销规则业务
 */
@Service
public class MarketingBuyoutPriceFullBaleLevelService {

    @Autowired
    private MarketingBuyoutPriceLevelRepository marketingBuyoutPriceLevelRepository;

    /**
     * 凭借营销规则信息
     */
    public void listFullBaleLevel(List<MarketingPageVO> marketingList) {
        List<Long> marketingIds = marketingList.stream().map(MarketingPageVO::getMarketingId).collect(Collectors.toList());
        List<MarketingBuyoutPriceLevel> priceLevelList = marketingBuyoutPriceLevelRepository.findByMarketingIdIn(marketingIds);
        Map<Long, List<MarketingBuyoutPriceLevel>> levelMap = priceLevelList.stream().collect(Collectors.groupingBy(MarketingBuyoutPriceLevel::getMarketingId));
        for (MarketingPageVO marketingId : marketingList) {
            List<MarketingBuyoutPriceLevel> levels = levelMap.get(marketingId.getMarketingId());
            if (CollectionUtils.isEmpty(levels)){
                continue;
            }
            List<String> levelsList= levels.stream().map(marketing->{
                StringBuilder builder = new StringBuilder();
                builder.append(marketing.getFullAmount()).append("元任选").append(marketing.getChoiceCount()).append("件");
                return builder.toString();
            }).collect(Collectors.toList());
            marketingId.setRulesList(levelsList);
        }
    }
}
