package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.marketing.bean.constant.MarketingErrorCode;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p></p>
 * author: weiwenhao
 * Date: 2020-04-13
 */
@ApiModel
@Data
public class MarketingBuyoutPriceSaveDTO extends MarketingSaveDTO {

    private static final long serialVersionUID = 4894133284221615424L;

    /**
     * 满减多级实体对象
     */
    @ApiModelProperty(value = "营销一口价多级优惠列表")
    @NotNull
    @Size(max = 5)
    private List<MarketingBuyoutPriceLevelDTO> buyoutPriceLevelList;

    public List<MarketingBuyoutPriceLevelDTO> generateFullReductionLevelList(Long marketingId) {
        return buyoutPriceLevelList.stream().map((level) -> {
            level.setMarketingId(marketingId);
            return level;
        }).collect(Collectors.toList());
    }

    public void valid() {
        Set set = new HashSet<Long>();
        Set setChoiceCount = new HashSet<Long>();

        // 校验参数，一口价打包活动件数和金额不能为空，不能重复的校验
        buyoutPriceLevelList.stream().forEach((level) -> {
            if (level.getFullAmount() == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            } else if (level.getFullAmount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MIN)) < 0 || level.getFullAmount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MAX)) > 0) {
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLAMOUNT_ERROR);
            }
            if (level.getChoiceCount() == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            } else if (level.getChoiceCount().compareTo(Constants.MARKETING_FULLCOUNT_MIN) < 0 || level.getChoiceCount().compareTo(Constants.MARKETING_FULLCOUNT_MAX) > 0) {
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLCOUNT_ERROR);
            }
            set.add(level.getFullAmount());
            setChoiceCount.add(level.getChoiceCount());
        });

        //判断一口价打包活动规则金额是否相同
        if (set.size() != buyoutPriceLevelList.size()) {
            throw new SbcRuntimeException(MarketingErrorCode.MARKETING_MULTI_LEVEL_AMOUNT_NOT_ALLOWED_SAME);
        }
        //判断一口价打包活动规则件数是否相同
        if (setChoiceCount.size() != buyoutPriceLevelList.size()) {
            throw new SbcRuntimeException(MarketingErrorCode.MARKETING_MULTI_LEVEL_COUNT_NOT_ALLOWED_SAME);
        }

        if (this.getScopeType() == MarketingScopeType.SCOPE_TYPE_CUSTOM) {
            if (this.getSkuIds() == null || this.getSkuIds().stream().allMatch(skuId -> "".equals(skuId.trim()))) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
    }
}
