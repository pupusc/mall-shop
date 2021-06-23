package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.order.bean.dto.PurchaseCalcAmountDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>购物车价格计算请求参数</p>
 * author: sunkun
 * Date: 2018-11-30
 */
@Data
@ApiModel
public class PurchaseCalcAmountRequest implements Serializable {

    private static final long serialVersionUID = -5594709398789761599L;

    @ApiModelProperty(value = "商品信息")
    private List<String> goodsInfoIds;

    @ApiModelProperty(value = "会员id")
    private String customerId;

    @ApiModelProperty(value = "采购单信息")
    @NotNull
    private PurchaseCalcAmountDTO purchaseCalcAmount;

}
