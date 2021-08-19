package com.wanmi.sbc.order.api.request.distribution;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class PageByCustomerIdRequest extends BaseQueryRequest {

    @ApiModelProperty("分销员的customerId")
    @NotBlank
    private String customerId;

}
