package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class PaidCardSimpleVO implements Serializable {


    private static final long serialVersionUID = -6817159125180539959L;
    /**
     * 付费会员卡id
     */
    @ApiModelProperty(value = "付费会员卡id")
    private String paidCardId;

    /**
     * 付费会员卡名称
     */
    @ApiModelProperty(value = "付费会员卡名称")
    private String paidCardName;
}
