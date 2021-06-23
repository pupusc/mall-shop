package com.wanmi.sbc.marketing.api.request.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingScopeListInvalidMarketingRequest implements Serializable {

    private static final long serialVersionUID = 1506933929495607020L;

    /**
     * 客户ID
     */
    @NotBlank
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 营销id列表
     */
    @ApiModelProperty(value = "营销id列表")
    private List<Long> marketingIds;

    /**
     * 营销分组sku
     */
    @ApiModelProperty(value = "营销分组sku")
    private Map<Long, List<String>> skuGroup;

    /**
     * 店铺等级
     */
    @ApiModelProperty(value = "营销分组sku")
    private Map<Long, Long> levelMap;



}
