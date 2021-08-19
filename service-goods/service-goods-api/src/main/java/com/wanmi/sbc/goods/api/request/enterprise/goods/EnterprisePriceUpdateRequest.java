package com.wanmi.sbc.goods.api.request.enterprise.goods;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 企业价格修改——单个接口
 * @author by 柏建忠 on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterprisePriceUpdateRequest implements Serializable {

    private static final long serialVersionUID = -8312305040718761890L;
    /**
     * skuId
     */
    @ApiModelProperty(value = "模拟goodsId")
    @NotEmpty
    private String goodsInfoId;

    /**
     * 企业价格
     */
    @ApiModelProperty(value = "企业价")
    @NotNull
    private BigDecimal enterPrisePrice;


    /**
     *  企业商品审核 0: 不需要审核 1: 需要审核
     */
    @ApiModelProperty(value = " 企业商品审核 0: 不需要审核 1: 需要审核 ")
    private DefaultFlag enterpriseGoodsAuditFlag;

}
