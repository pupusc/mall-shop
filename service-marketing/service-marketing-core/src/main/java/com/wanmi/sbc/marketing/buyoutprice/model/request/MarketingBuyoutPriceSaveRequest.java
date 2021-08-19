package com.wanmi.sbc.marketing.buyoutprice.model.request;


import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.buyoutprice.model.entry.MarketingBuyoutPriceLevel;
import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
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
 * 一口价营销规则
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingBuyoutPriceSaveRequest extends MarketingSaveRequest {
    /**
     * 一口价多级实体对象
     */
    @NotNull
    @Size(max=5)
    private List<MarketingBuyoutPriceLevel> buyoutPriceLevelList;

    public List<MarketingBuyoutPriceLevel> generateBuyoutPriceLevelList(Long marketingId) {
        return buyoutPriceLevelList.stream().map((level) -> {
            level.setMarketingId(marketingId);
            return level;
        }).collect(Collectors.toList());
    }

    public void valid() {
        Set set;

        if (this.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT) {
            set = new HashSet<BigDecimal>();
        } else {
            set = new HashSet<Long>();
        }

        // 校验参数，满金额时金额不能为空，满数量时数量不能为空
        buyoutPriceLevelList.stream().forEach((level) -> {
            if (this.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT) {
                if (level.getFullAmount() == null) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                } else if (level.getFullAmount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MIN)) < 0 ||  level.getFullAmount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MAX)) > 0) {
                    throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLAMOUNT_ERROR);
                }

                set.add(level.getFullAmount());
            } else if (this.getSubType() == MarketingSubType.REDUCTION_FULL_COUNT) {
                if (level.getChoiceCount() == null) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                } else if (level.getChoiceCount().compareTo(Constants.MARKETING_FULLCOUNT_MIN) < 0 || level.getChoiceCount().compareTo(Constants.MARKETING_FULLCOUNT_MAX) > 0) {
                    throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLCOUNT_ERROR);
                }

                set.add(level.getChoiceCount());
            } else {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        });

        if (set.size() != buyoutPriceLevelList.size()) {
            if (this.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT) {
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
