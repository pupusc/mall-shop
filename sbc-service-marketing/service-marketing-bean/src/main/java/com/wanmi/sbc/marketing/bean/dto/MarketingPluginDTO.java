package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
public class MarketingPluginDTO implements Serializable {

    private static final long serialVersionUID = -4370964691100843938L;

    /**
     * 当前客户
     */
    @ApiModelProperty(value = "当前客户信息")
    private CustomerDTO customerDTO;
}
