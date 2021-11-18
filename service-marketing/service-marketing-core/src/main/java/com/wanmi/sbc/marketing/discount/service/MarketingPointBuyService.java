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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<MarketingPointBuyLevel> marketingPointBuyLevel1 = request.getMarketingPointBuyLevel();
        for (MarketingPointBuyLevel marketingPointBuyLevel : marketingPointBuyLevel1) {
            if(marketingPointBuyLevel.getMoney() == null || marketingPointBuyLevel.getPointNeed() == null || marketingPointBuyLevel.getSkuId() == null
                    || marketingPointBuyLevel.getPrice() == null){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "参数错误");
            }
        }
        Marketing marketing = marketingService.addMarketing(request);
        List<MarketingPointBuyLevel> marketingPointBuyLevel = request.getMarketingPointBuyLevel();
        List<MarketingPointBuyLevel> toSave = new ArrayList<>();
        for (MarketingPointBuyLevel pointBuyLevel : marketingPointBuyLevel) {
            MarketingPointBuyLevel level = new MarketingPointBuyLevel();
            level.setMarketingId(marketing.getMarketingId());
            level.setMoney(pointBuyLevel.getMoney());
            level.setPointNeed(pointBuyLevel.getPointNeed());
            level.setPrice(pointBuyLevel.getPrice());
            level.setSkuId(pointBuyLevel.getSkuId());
            toSave.add(level);
        }
        marketingPointBuyLevelRepository.saveAll(toSave);
    }

    /**
     * 更新满折
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePointBuyDiscount(MarketingPointBuySaveRequest request) throws SbcRuntimeException {
        if(request.getMarketingId() == null){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "参数错误");
        }
        List<MarketingPointBuyLevel> marketingPointBuyLevel1 = request.getMarketingPointBuyLevel();
        for (MarketingPointBuyLevel marketingPointBuyLevel : marketingPointBuyLevel1) {
            if(marketingPointBuyLevel.getMoney() == null || marketingPointBuyLevel.getPointNeed() == null || marketingPointBuyLevel.getSkuId() == null
                || marketingPointBuyLevel.getPrice() == null){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "参数错误");
            }
        }
        marketingService.modifyMarketing(request);
        marketingPointBuyLevelRepository.deleteByMarketingId(request.getMarketingId());
        List<MarketingPointBuyLevel> marketingPointBuyLevel = request.getMarketingPointBuyLevel();
        List<MarketingPointBuyLevel> toSave = new ArrayList<>();
        for (MarketingPointBuyLevel pointBuyLevel : marketingPointBuyLevel) {
            MarketingPointBuyLevel level = new MarketingPointBuyLevel();
            level.setMarketingId(request.getMarketingId());
            level.setMoney(pointBuyLevel.getMoney());
            level.setPointNeed(pointBuyLevel.getPointNeed());
            level.setPrice(pointBuyLevel.getPrice());
            level.setSkuId(pointBuyLevel.getSkuId());
            toSave.add(level);
        }
        marketingPointBuyLevelRepository.saveAll(toSave);
    }

    public MarketingPointBuyLevel findPointBuyById(Long id){
        return marketingPointBuyLevelRepository.findById(id).orElse(null);
    }

}
