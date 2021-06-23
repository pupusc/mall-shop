package com.wanmi.sbc.marketing.gift.request;


import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftDetail;
import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftLevel;
import com.wanmi.sbc.marketing.util.error.MarketingErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 营销满赠规则
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingFullGiftSaveRequest extends MarketingSaveRequest {
    /**
     * 满赠多级实体对象
     */
    @NotNull
    @Size(max=5)
    private List<MarketingFullGiftLevel> fullGiftLevelList;

    public List<MarketingFullGiftLevel> generateFullGiftLevelList(Long marketingId) {
        return fullGiftLevelList.stream().map((level) -> {
            level.setGiftLevelId(null);
            level.setMarketingId(marketingId);
            return level;
        }).collect(Collectors.toList());
    }

    public List<MarketingFullGiftDetail> generateFullGiftDetailList(List<MarketingFullGiftLevel> fullGiftLevelList) {
        return fullGiftLevelList.stream().map((level) ->
                level.getFullGiftDetailList().stream().map((detail) -> {
                    detail.setGiftLevelId(level.getGiftLevelId());
                    detail.setMarketingId(level.getMarketingId());
                    return detail;
        })).flatMap((detail) -> detail).collect(Collectors.toList());
    }

    public void valid() {
        Set set;

        if (this.getSubType() == MarketingSubType.GIFT_FULL_AMOUNT) {
            set = new HashSet<BigDecimal>();
        } else {
            set = new HashSet<Long>();
        }

        // 校验参数，满金额时金额不能为空，满数量时数量不能为空
        fullGiftLevelList.stream().forEach((level) -> {
            if (this.getSubType() == MarketingSubType.GIFT_FULL_AMOUNT) {
                if (level.getFullAmount() == null) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                } else if (level.getFullAmount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MIN)) < 0 ||  level.getFullAmount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MAX)) > 0) {
                    throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLAMOUNT_ERROR);
                }

                set.add(level.getFullAmount());
            } else if (this.getSubType() == MarketingSubType.GIFT_FULL_COUNT) {
                if (level.getFullCount() == null) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                } else if (level.getFullCount().compareTo(Constants.MARKETING_FULLCOUNT_MIN) < 0 || level.getFullCount().compareTo(Constants.MARKETING_FULLCOUNT_MAX) > 0) {
                    throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLCOUNT_ERROR);
                }

                set.add(level.getFullCount());
            } else {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (level.getFullGiftDetailList() == null || level.getFullGiftDetailList().isEmpty()) {
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_GIFT_TYPE_MIN_1);
            } else if (level.getFullGiftDetailList().size() > Constants.MARKETING_Gift_TYPE_MAX) {
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_GIFT_TYPE_MAX_20);
            } else {
                level.getFullGiftDetailList().stream().forEach(detail -> {
                    if (detail.getProductNum() == null || detail.getProductNum() < Constants.MARKETING_Gift_MIN || detail.getProductNum() > Constants.MARKETING_Gift_MAX) {
                        throw new SbcRuntimeException(MarketingErrorCode.MARKETING_GIFT_COUNT_BETWEEN_1_AND_999);
                    }
                });
            }
        });

        if (set.size() != fullGiftLevelList.size()) {
            if (this.getSubType() == MarketingSubType.GIFT_FULL_AMOUNT) {
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_MULTI_LEVEL_AMOUNT_NOT_ALLOWED_SAME);
            } else {
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_MULTI_LEVEL_COUNT_NOT_ALLOWED_SAME);
            }
        }

        if (this.getScopeType() == MarketingScopeType.SCOPE_TYPE_CUSTOM) {
            if (this.getSkuIds() == null || this.getSkuIds().stream().allMatch(skuId -> "".equals(skuId.trim()))) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
    }
}
