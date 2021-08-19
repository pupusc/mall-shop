package com.wanmi.sbc.goods.api.request.presellsale;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 预售活动支付传参实体
 */
@Data
@ApiModel
public class PresellSalePaymentRequest extends BaseRequest {

    /**
     * 预售活动id
     */
    @ApiModelProperty(value = "预售活动id")
    private String presellSaleId;

    /**
     * 预售对应商品的购买数量
     */
    @ApiModelProperty(value = "预售对应商品的购买数量")
    private Integer purchases;


    /**
     * 预售活动关联商品id
     */
    @ApiModelProperty(value = "预售活动关联商品id")
    private String presellSaleGoodsId;
}
