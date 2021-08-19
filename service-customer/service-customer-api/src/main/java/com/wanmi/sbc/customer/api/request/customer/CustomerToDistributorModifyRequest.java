package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
@Builder
public class CustomerToDistributorModifyRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -8427145177039519006L;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @NotNull
    private String customerId;

    /**
     * 是否为分销员
     */
    @ApiModelProperty(value = "是否为分销员")
    @NotNull
    private DefaultFlag isDistributor;
}
