package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseModifyGoodsMarketingRequest implements Serializable {

    private static final long serialVersionUID = -7127929523767105974L;

    @ApiModelProperty(value = "商品id")
    @NotBlank
    private String goodsInfoId;

    @ApiModelProperty(value = "营销id")
    @NotNull
    private Long marketingId;

    @ApiModelProperty(value = "客户信息")
    private CustomerDTO customer;
}
