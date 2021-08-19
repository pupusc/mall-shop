package com.wanmi.sbc.marketing.api.response.market;

import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Description:
* @Author: ZhangLingKe
* @CreateDate: 2018/11/22 9:56
*/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingGetByIdForCustomerResponse {

    /**
     * 终端营销视图
     */
    @ApiModelProperty(value = "终端营销视图")
    private MarketingForEndVO marketingForEndVO;

}
