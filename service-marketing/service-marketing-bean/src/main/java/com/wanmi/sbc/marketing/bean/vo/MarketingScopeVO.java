package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 14:16
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingScopeVO implements Serializable {

    private static final long serialVersionUID = -7950579506250193744L;
    /**
     * 货品与促销规则表Id
     */
    @ApiModelProperty(value = "营销和商品关联表Id")
    private Long marketingScopeId;

    /**
     * 促销Id
     */
    @ApiModelProperty(value = "营销Id")
    private Long marketingId;

    /**
     * 促销范围Id
     */
    @ApiModelProperty(value = "营销范围Id")
    private String scopeId;

}
