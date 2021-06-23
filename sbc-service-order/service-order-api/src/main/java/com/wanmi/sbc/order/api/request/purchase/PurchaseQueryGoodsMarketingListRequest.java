package com.wanmi.sbc.order.api.request.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseQueryGoodsMarketingListRequest implements Serializable {

    private static final long serialVersionUID = -6467021279956692513L;

    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;
}
