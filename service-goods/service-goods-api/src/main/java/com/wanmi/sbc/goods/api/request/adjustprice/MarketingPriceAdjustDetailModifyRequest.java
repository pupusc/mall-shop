package com.wanmi.sbc.goods.api.request.adjustprice;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>市场价调价详情编辑请求参数</p>
 * Created by of628-wenzhi on 2020-12-15-3:09 下午.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class MarketingPriceAdjustDetailModifyRequest extends GoodsBaseRequest {
    private static final long serialVersionUID = 868467459578972778L;

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
    @NotNull
    private Long adjustDetailId;

    /**
     * 更新后的市场价
     */
    @ApiModelProperty(value = "更新后的市场价")
    private BigDecimal marketingPrice;

    /**
     * Sku ID
     */
    @ApiModelProperty(value = "Sku ID")
    @NotBlank
    private String goodsInfoId;

    @Override
    public void checkParam() {
        BigDecimal marketingPrice = this.marketingPrice;
        if (Objects.isNull(marketingPrice)) {
            throw new SbcRuntimeException(CommonErrorCode.INPUT_HINT, new Object[]{"市场价！"});
        }
        if (!ValidateUtil.isNum(marketingPrice + "") && !ValidateUtil.isFloatNum(marketingPrice + "")) {
            throw new SbcRuntimeException(CommonErrorCode.CONSTRAINT_HINT, new Object[]{"市场价", "0和正数，允许两位小数，不超过9999999.99！"});
        }
        if (marketingPrice.compareTo(new BigDecimal("9999999.99")) > 0) {
            throw new SbcRuntimeException(CommonErrorCode.CONSTRAINT_HINT, new Object[]{"市场价", "0和正数，允许两位小数，不超过9999999.99！"});
        }
    }
}
