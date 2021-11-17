package com.wanmi.sbc.marketing.discount.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingPointBuyLevel;
import com.wanmi.sbc.marketing.discount.model.request.MarketingPointBuySaveRequest;
import com.wanmi.sbc.marketing.discount.repository.MarketingPointBuyLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketingPointBuyService {

    @Autowired
    private MarketingPointBuyLevelRepository marketingPointBuyLevelRepository;
    @Autowired
    private MarketingService marketingService;

    /**
     * 新增满折
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPointBuyDiscount(MarketingPointBuySaveRequest request) throws SbcRuntimeException {
        if(request.getMarketingPointBuyLevel().getMoney() == null || request.getMarketingPointBuyLevel().getPointNeed() == null){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "参数错误");
        }
        Marketing marketing = marketingService.addMarketing(request);
        MarketingPointBuyLevel marketingPointBuyLevel = request.getMarketingPointBuyLevel();
        MarketingPointBuyLevel level = new MarketingPointBuyLevel();
        level.setMarketingId(marketing.getMarketingId());
        level.setMoney(marketingPointBuyLevel.getMoney());
        level.setPointNeed(marketingPointBuyLevel.getPointNeed());
        marketingPointBuyLevelRepository.save(level);
    }

    /**
     * 更新满折
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePointBuyDiscount(MarketingPointBuySaveRequest request) throws SbcRuntimeException {
        if(request.getMarketingId() == null || request.getMarketingPointBuyLevel().getMoney() == null || request.getMarketingPointBuyLevel().getPointNeed() == null){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "参数错误");
        }
        marketingService.modifyMarketing(request);
        marketingPointBuyLevelRepository.deleteById(request.getMarketingPointBuyLevel().getId());
        MarketingPointBuyLevel marketingPointBuyLevel = request.getMarketingPointBuyLevel();
        MarketingPointBuyLevel level = new MarketingPointBuyLevel();
        level.setMarketingId(request.getMarketingId());
        level.setMoney(marketingPointBuyLevel.getMoney());
        level.setPointNeed(marketingPointBuyLevel.getPointNeed());
        marketingPointBuyLevelRepository.save(level);
    }
}
