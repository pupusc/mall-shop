package com.wanmi.sbc.marketing.halfpricesecondpiece.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import com.wanmi.sbc.marketing.halfpricesecondpiece.model.entry.MarketingHalfPriceSecondPieceLevel;
import com.wanmi.sbc.marketing.halfpricesecondpiece.model.request.HalfPriceSecondPieceSaveRequest;
import com.wanmi.sbc.marketing.halfpricesecondpiece.repository.HalfPriceSecondPieceLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 营销一口价业务
 */
@Service
public class HalfPriceSecondPieceService {

    @Autowired
    private HalfPriceSecondPieceLevelRepository halfPriceSecondPieceLevelRepository;

    @Autowired
    private MarketingService marketingService;


    /**
     * 新增第二件半价
     */
    @Transactional(rollbackFor = Exception.class)
    public Marketing addMarketingHalfPriceSecondPiece(HalfPriceSecondPieceSaveRequest request) throws SbcRuntimeException {
        Marketing marketing = marketingService.addMarketing(request);

        // 保存多级优惠信息
        this.saveLevel(request.generateHalfPriceSecondPieceList(marketing.getMarketingId()));

        return marketing;
    }

    /**
     * 修改第二件半价
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyHalfPriceSecondPiece(HalfPriceSecondPieceSaveRequest request) throws SbcRuntimeException {
        //修改营销信息
        marketingService.modifyMarketing(request);
        // 先删除已有的多级优惠信息，然后再保存
        halfPriceSecondPieceLevelRepository.deleteByMarketingId(request.getMarketingId());
        //保存多节营销活动
        this.saveLevel(request.generateHalfPriceSecondPieceList(request.getMarketingId()));
    }


    /**
     * 保存多级优惠信息
     */
    private void saveLevel(MarketingHalfPriceSecondPieceLevel halfPriceSecondPieceLevel) {
        if (Objects.nonNull(halfPriceSecondPieceLevel)) {
            halfPriceSecondPieceLevelRepository.save(halfPriceSecondPieceLevel);
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }
}
