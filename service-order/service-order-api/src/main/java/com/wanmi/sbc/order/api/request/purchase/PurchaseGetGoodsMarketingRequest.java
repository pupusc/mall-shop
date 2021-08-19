package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PurchaseGetGoodsMarketingRequest implements Serializable {

    private static final long serialVersionUID = 412787022492395794L;

    @ApiModelProperty(value = "商品信息")
    private List<GoodsInfoVO> goodsInfos;

    @ApiModelProperty(value = "客户信息")
    private CustomerVO customer;
}
