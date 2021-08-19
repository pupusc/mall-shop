package com.wanmi.sbc.goods.api.request.adjustprice;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SupplyPriceAdjustDetailModifyRequest extends GoodsBaseRequest {

    private static final long serialVersionUID = -2987733268661928410L;
    /**
     * 调价单号
     */
    @ApiModelProperty(value = "调价单号")
    @NotBlank
    private String adjustNo;

    /**
     * 调价明细ID
     */
    @ApiModelProperty(value = "调价明细ID")
    @NonNull
    private Long adjustDetailId;

    /**
     * 更新后的供货价
     */
    @ApiModelProperty(value = "更新后的供货价")
    private BigDecimal supplyPrice;

    @Override
    public void checkParam() {
        if (Objects.isNull(this.supplyPrice)) {
            throw new SbcRuntimeException(CommonErrorCode.INPUT_HINT, new Object[]{"供货价！"});
        }
        if (!ValidateUtil.isNum(supplyPrice+"") && !ValidateUtil.isFloatNum(supplyPrice+"")) {
            throw new SbcRuntimeException(CommonErrorCode.CONSTRAINT_HINT, new Object[]{"供货价", "0和正数，允许两位小数，不超过9999999.99！"});
        }
        if (supplyPrice.compareTo(new BigDecimal("9999999.99")) > 0) {
            throw new SbcRuntimeException(CommonErrorCode.CONSTRAINT_HINT, new Object[]{"供货价", "0和正数，允许两位小数，不超过9999999.99！"});
        }
    }
}
