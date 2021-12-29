package com.wanmi.sbc.marketing.common.service;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingScopeListInvalidMarketingRequest;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.model.root.MarketingScope;
import com.wanmi.sbc.marketing.common.repository.MarketingScopeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MarketingScopeService {

    @Autowired
    private MarketingScopeRepository marketingScopeRepository;

    @Autowired
    private MarketingService marketingService;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    /**
     * 根据营销编号查询营销等级集合
     *
     * @param marketingId
     * @return
     */
    public List<MarketingScope> findByMarketingId(Long marketingId){
        return marketingScopeRepository.findByMarketingId(marketingId);
    }

    /**
     * 订单营销信息校验，返回失效的营销活动
     *
     */
    public List<Marketing> listInvalidMarketing(MarketingScopeListInvalidMarketingRequest request) {
        List<Long> marketingIds = request.getMarketingIds();
        if (CollectionUtils.isEmpty(marketingIds)) {
            return Collections.emptyList();
        }
        List<Marketing> marketingList = marketingService.queryByIds(marketingIds);
        if (CollectionUtils.isEmpty(marketingList)) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        //获取用户在商户店铺下的等级信息
        Map<Long, Long> levelMap = request.getLevelMap();

        //用于存放无效的营销活动
        List<Marketing> invalidIds = new ArrayList<>();
        Map<Long, List<String>> skuGroup = request.getSkuGroup();
        marketingList.forEach(i -> {
            //校验营销活动
            if (i.getIsPause() == BoolFlag.YES || i.getDelFlag() == DeleteFlag.YES || i.getBeginTime().isAfter(LocalDateTime.now())
                    || i.getEndTime().isBefore(LocalDateTime.now())) {
                invalidIds.add(i);
            } else {
                //校验关联商品是否匹配
                List<String> scopeList = this.findByMarketingId(i.getMarketingId()).stream().map(
                        MarketingScope::getScopeId).collect(Collectors.toList());
                List<String> skuList = skuGroup.get(i.getMarketingId());
                if (skuList.stream().anyMatch(s -> !scopeList.contains(s))) {
                    //营销活动创建后不可更改，如果关联商品与后台设置不匹配，基本是安全问题造成
                    throw new SbcRuntimeException(CommonErrorCode.FAILED);
                }
            }
            //校验用户级别
            Long level = levelMap.get(i.getStoreId());
            switch (i.getMarketingJoinLevel()) {
                case ALL_CUSTOMER:
                    break;
                case ALL_LEVEL:
//                    if (level == null) {
//                        invalidIds.add(i);
//                    }
                    break;
                case LEVEL_LIST:
                    if (!i.getJoinLevelList().contains(level)) {
                        invalidIds.add(i);
                    }
                    break;
                default:
                    break;
            }
        });
        return invalidIds;
    }
}
