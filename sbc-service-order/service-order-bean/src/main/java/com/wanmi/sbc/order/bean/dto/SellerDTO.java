package com.wanmi.sbc.order.bean.dto;

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
public class SellerDTO implements Serializable{

    /**
     * 卖家ID
     */
    @ApiModelProperty(value = "卖家ID")
    private String adminId;

    /**
     * 代理人Id，用于代客下单
     */
    @ApiModelProperty(value = "代理人Id，用于代客下单")
    private String proxyId;

    /**
     * 代理人名称，用于代客下单，相当于OptUserName
     */
    @ApiModelProperty(value = "代理人名称，用于代客下单，相当于OptUserName")
    private String proxyName;

}
