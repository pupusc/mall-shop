package com.wanmi.sbc.marketing.api.response.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 16:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingQueryStartingByIdsResponse {

    /**
     * 营销id字符形式列表
     */
    @ApiModelProperty(value = "营销id列表")
    private List<String> marketingIdList;

}
