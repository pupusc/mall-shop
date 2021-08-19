package com.wanmi.sbc.marketing.api.request.gift;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-20 16:44
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullGiftLevelListByMarketingIdAndCustomerRequest extends MarketingIdRequest {

    private static final long serialVersionUID = -301915860701916007L;
    /**
     * 客户信息
     */
    @ApiModelProperty(value = "客户信息")
    private CustomerDTO customer;

    @ApiModelProperty(value = "客户id")
    private String customerId;
}
