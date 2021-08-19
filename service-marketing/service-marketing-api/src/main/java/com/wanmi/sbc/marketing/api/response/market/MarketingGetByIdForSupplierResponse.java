package com.wanmi.sbc.marketing.api.response.market;

import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingGetByIdForSupplierResponse {

    /**
     * 终端营销视图
     */
    @ApiModelProperty(value = "终端营销视图")
    private MarketingForEndVO marketingForEndVO;

}
