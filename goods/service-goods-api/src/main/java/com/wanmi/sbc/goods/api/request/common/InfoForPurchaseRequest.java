package com.wanmi.sbc.goods.api.request.common;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoForPurchaseRequest {

    /**
     * 购物车单品ids
     */
    @ApiModelProperty(value = "购物车单品ids")
    @NotEmpty
    private List<String> goodsInfoIds;

    /**
     * 当前登录用户
     */
    @ApiModelProperty(value = "当前登录用户")
    private CustomerVO customer;

    /**
     * 区的区域码
     */
    @ApiModelProperty(value = "区的区域码")
    private Long areaId;


}
