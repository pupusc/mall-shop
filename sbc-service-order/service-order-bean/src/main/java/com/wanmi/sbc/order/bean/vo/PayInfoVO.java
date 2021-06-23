package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单支付信息
 * @author wumeng[OF2627]
 *         company qianmi.com
 *         Date 2017-04-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PayInfoVO implements Serializable{
    private static final long serialVersionUID = 1859928406081744303L;

    /**
     * 支付类型标识
     */
    @ApiModelProperty(value = "支付类型标识,0：在线支付 1：线下支付")
    private String payTypeId;

    /**
     * 支付类型名称
     */
    @ApiModelProperty(value = "支付类型名称")
    private  String payTypeName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String desc;

    /**
     * 是否合并支付（在线支付场景）
     */
    @ApiModelProperty("是否合并支付（在线支付场景）")
    private boolean isMergePay;
}
