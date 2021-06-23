package com.wanmi.sbc.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @Author: yinxianzhi
 * @Date: Created In 下午3:28 2019/5/20
 */
@ApiModel
@Data
public class ImmediateExchangeRequest {

    /**
     * 积分商品id
     */
    @ApiModelProperty("积分商品id")
    @NotNull
    private String pointsGoodsId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    @Range(min = 1)
    private long num;

}
