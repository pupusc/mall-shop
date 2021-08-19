package com.wanmi.sbc.customer.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>客户等级</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
public class MarketingCustomerLevelDTO implements Serializable {

    private static final long serialVersionUID = 5079626444171351912L;

    /**
     * 预约Id
     */
    @ApiModelProperty(value = "预约Id")
    private Long id;

    /**
     * 营销Id
     */
    @ApiModelProperty(value = "营销Id")
    private Long marketingId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 目标客户
     */
    @ApiModelProperty(value = "参加会员")
    private String joinLevel;
}
