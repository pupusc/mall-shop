package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>营销和商品关联关系</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
public class MarketingScopeDTO implements Serializable {

    private static final long serialVersionUID = -3177165143639231339L;

    /**
     * 货品与营销规则表Id
     */
    @ApiModelProperty(value = "货品与营销规则表Id")
    private Long marketingScopeId;

    /**
     * 营销Id
     */
    @ApiModelProperty(value = "营销Id")
    private Long marketingId;

    /**
     * 营销范围Id
     */
    @ApiModelProperty(value = "营销范围Id")
    private String scopeId;
}
