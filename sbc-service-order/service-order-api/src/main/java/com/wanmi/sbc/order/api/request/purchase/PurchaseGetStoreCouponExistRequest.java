package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseGetStoreCouponExistRequest implements Serializable {

    private static final long serialVersionUID = 1767395050900577781L;

    @ApiModelProperty(value = "客户信息")
    private CustomerVO customer;

    @ApiModelProperty(value = "购物车归属id")
    private String inviteeId;
}
