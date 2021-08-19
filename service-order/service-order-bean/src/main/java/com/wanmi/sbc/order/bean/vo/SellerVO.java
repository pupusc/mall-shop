package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家
 * Created by Administrator on 2017/5/1.
 */
@Data
@ApiModel
public class SellerVO implements Serializable{

    private static final long serialVersionUID = 1045093112069149841L;
    /**
     * 卖家ID
     */
    @ApiModelProperty(value = "卖家ID")
    private String adminId;

    /**
     * 代理人Id，用于代客下单
     */
    @ApiModelProperty(value = "代理人Id")
    private String proxyId;

    /**
     * 代理人名称，用于代客下单，相当于OptUserName
     */
    @ApiModelProperty(value = "代理人名称")
    private String proxyName;
}
