package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBaseByCustomerIdAndDeleteFlagRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -3854269857592932191L;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记")
    private DeleteFlag deleteFlag;
}
