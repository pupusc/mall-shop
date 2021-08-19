package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p></p>
 * author: weiwenhao
 * Date: 2020-05-22
 */
@ApiModel
@Data
public class HalfPriceSecondPieceSaveDTO extends MarketingSaveDTO {

    private static final long serialVersionUID = 4894133284221615424L;

    /**
     * 第二件半价实体对象
     */
    @ApiModelProperty(value = "第二件半价活动规则列表")
    @NotNull
    private HalfPriceSecondPieceLevelDTO halfPriceSecondPieceLevel;

    //第二件半价规则参数校验
    public void valid() {
        //判断参数不能为空，是否是0-9.9数据
        if (Objects.isNull(halfPriceSecondPieceLevel.getDiscount()) ||  halfPriceSecondPieceLevel.getDiscount().compareTo(BigDecimal.ZERO) == -1 ||  halfPriceSecondPieceLevel.getDiscount().compareTo(BigDecimal.valueOf(9.9))==1) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //第二件半价件数参数不能为null,件数>1
        if (Objects.isNull(halfPriceSecondPieceLevel.getNumber()) || halfPriceSecondPieceLevel.getNumber()<=1) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if (this.getScopeType() == MarketingScopeType.SCOPE_TYPE_CUSTOM) {
            if (this.getSkuIds() == null || this.getSkuIds().stream().allMatch(skuId -> "".equals(skuId.trim()))) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
    }
}
