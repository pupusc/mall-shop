package com.wanmi.sbc.marketing.discount.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingFullDiscountLevel;
import com.wanmi.sbc.marketing.discount.model.request.MarketingFullDiscountSaveRequest;
import com.wanmi.sbc.marketing.discount.repository.MarketingFullDiscountLevelRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 营销满折业务
 */
@Service
public class MarketingFullDiscountService {

    @Autowired
    private MarketingFullDiscountLevelRepository marketingFullDiscountLevelRepository;

    @Autowired
    private MarketingService marketingService;

    /**
     * 新增满折
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMarketingFullDiscount(MarketingFullDiscountSaveRequest request) throws SbcRuntimeException {

        Marketing marketing = marketingService.addMarketing(request);

        // 保存多级优惠信息
        this.saveLevelList(request.generateFullDiscountLevelList(marketing.getMarketingId()));
    }

    /**
     * 修改满折
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyMarketingFullDiscount(MarketingFullDiscountSaveRequest request) throws SbcRuntimeException {
        marketingService.modifyMarketing(request);

        // 先删除已有的多级优惠信息，然后再保存
        marketingFullDiscountLevelRepository.deleteByMarketingId(request.getMarketingId());
        this.saveLevelList(request.generateFullDiscountLevelList(request.getMarketingId()));
    }

    /**
     * 根据营销编号查询营销等级集合
     *
     * @param marketingId
     * @return
     */
    public List<MarketingFullDiscountLevel> findByMarketingId(Long marketingId){
       return marketingFullDiscountLevelRepository.findByMarketingId(marketingId);
    }

    /**
     * 保存多级优惠信息
     */
    private void saveLevelList(List<MarketingFullDiscountLevel> discountLevelList) {
        if(CollectionUtils.isNotEmpty(discountLevelList)) {
            marketingFullDiscountLevelRepository.saveAll(discountLevelList);
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }


}
