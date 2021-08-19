package com.wanmi.sbc.marketing.api.request.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 15:09
 */
@ApiModel
@Data
public class MarketingIdsRequest implements Serializable {

    private static final long serialVersionUID = 4987033902437196300L;
    /**
     * 营销id列表
     */
    @ApiModelProperty(value = "营销id列表")
    private List<Long> marketingIds;

}
