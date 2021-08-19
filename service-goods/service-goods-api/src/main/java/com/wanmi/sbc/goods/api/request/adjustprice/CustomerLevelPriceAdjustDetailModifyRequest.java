package com.wanmi.sbc.goods.api.request.adjustprice;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsLevelPriceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.wanmi.sbc.common.util.ValidateUtil.NULL_EX_MESSAGE;

/**
 * <p>客户等级价批量调价详情编辑请求参数</p>
 * Created by of628-wenzhi on 2020-12-21-8:46 下午.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerLevelPriceAdjustDetailModifyRequest extends GoodsBaseRequest {
    private static final long serialVersionUID = 8907180245177802986L;

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

    /**
     * 客户级别价列表
     */
    @ApiModelProperty(value = "客户级别价列表")
    private List<GoodsLevelPriceDTO> levelPriceList;

    @Override
    public void checkParam() {
        BigDecimal marketingPrice = this.marketingPrice;
        Validate.isTrue(Objects.nonNull(marketingPrice) || CollectionUtils.isNotEmpty(levelPriceList), NULL_EX_MESSAGE,
                "marketPrice & levelPriceList");
        checkPrice(marketingPrice, "市场价");
        levelPriceList.forEach(i -> {
            if (Objects.nonNull(i.getPrice())) {
                checkPrice(i.getPrice(), "等级价");
            }
        });
    }

    private void checkPrice(BigDecimal price, String priceText) {
        if (Objects.nonNull(price)) {
            if (!ValidateUtil.isNum(price + "") && !ValidateUtil.isFloatNum(price + "")) {
                throw new SbcRuntimeException(CommonErrorCode.CONSTRAINT_HINT, new Object[]{priceText, "0和正数，允许两位小数，不超过9999999.99！"});
            }
            if (price.compareTo(new BigDecimal("9999999.99")) > 0) {
                throw new SbcRuntimeException(CommonErrorCode.CONSTRAINT_HINT, new Object[]{priceText, "0和正数，允许两位小数，不超过9999999.99！"});
            }
        }
    }

}
