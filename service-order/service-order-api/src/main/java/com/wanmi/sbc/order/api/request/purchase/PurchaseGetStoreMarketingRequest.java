package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsMarketingDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseGetStoreMarketingRequest implements Serializable {

    private static final long serialVersionUID = -6708015629193525235L;

    @ApiModelProperty(value = "商品营销信息")
    @NotNull
    @Size(min = 1)
    private List<GoodsMarketingDTO> goodsMarketings;

    @ApiModelProperty(value = "客户信息")
    private CustomerDTO customer;

    @ApiModelProperty(value = "商品ids")
    private List<String> goodsInfoIdList;

    @ApiModelProperty(value = "采购单信息")
    private PurchaseFrontRequest frontReq;
}
