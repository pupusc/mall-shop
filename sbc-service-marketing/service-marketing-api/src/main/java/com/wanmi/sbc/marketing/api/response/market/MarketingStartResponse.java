package com.wanmi.sbc.marketing.api.response.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 14:45
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingStartResponse {

    /**
     * 数据操作结果
     */
    @ApiModelProperty(value = "数据操作结果")
    private Integer resultNum;

}
