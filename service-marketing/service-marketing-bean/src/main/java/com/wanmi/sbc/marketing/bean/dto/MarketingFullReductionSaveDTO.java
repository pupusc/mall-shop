package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.marketing.bean.constant.MarketingErrorCode;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
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
 * author: sunkun
 * Date: 2018-11-21
 */
@ApiModel
@Data
public class MarketingFullReductionSaveDTO extends MarketingSaveDTO {

    private static final long serialVersionUID = 6192215362679463311L;

    /**
     * 满减多级实体对象
     */
    @ApiModelProperty(value = "营销满减多级优惠列表")
    @NotNull
    @Size(max=5)
    private List<MarketingFullReductionLevelDTO> fullReductionLevelList;

    public List<MarketingFullReductionLevelDTO> generateFullReductionLevelList(Long marketingId) {
        return fullReductionLevelList.stream().map((level) -> {
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
        fullReductionLevelList.stream().forEach((level) -> {
            if (this.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT) {
                if (level.getFullAmount() == null) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                } else if (level.getFullAmount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MIN)) < 0 ||  level.getFullAmount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MAX)) > 0) {
                    throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLAMOUNT_ERROR);
                }

                set.add(level.getFullAmount());
            } else if (this.getSubType() == MarketingSubType.REDUCTION_FULL_COUNT) {
                if (level.getFullCount() == null) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                } else if (level.getFullCount().compareTo(Constants.MARKETING_FULLCOUNT_MIN) < 0 || level.getFullCount().compareTo(Constants.MARKETING_FULLCOUNT_MAX) > 0) {
                    throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLCOUNT_ERROR);
                }

                set.add(level.getFullCount());
            } else {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (level.getReduction() == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            } else if (level.getReduction().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MIN)) < 0 ||  level.getReduction().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MAX)) > 0) {
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_FULLAMOUNT_ERROR);
            } else if (this.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT && level.getFullAmount().compareTo(level.getReduction()) <= 0) {
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_REDUCTION_AMOUNT_ERROR);
            }
        });

        if (set.size() != fullReductionLevelList.size()) {
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
