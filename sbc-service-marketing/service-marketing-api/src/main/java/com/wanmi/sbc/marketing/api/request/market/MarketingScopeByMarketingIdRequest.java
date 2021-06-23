package com.wanmi.sbc.marketing.api.request.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingScopeByMarketingIdRequest implements Serializable {

    private static final long serialVersionUID = 1506933929495607020L;
    /**
     * 促销Id
     */
    @ApiModelProperty(value = "营销id")
    @NotNull
    private Long marketingId;

}
