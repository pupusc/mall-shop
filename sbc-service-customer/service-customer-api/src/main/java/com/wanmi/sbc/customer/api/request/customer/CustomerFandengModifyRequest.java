package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFandengModifyRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -7196380103083045476L;

    @ApiModelProperty(value = "樊登id")
    private String fanDengId;

    @ApiModelProperty(value = "用户id")
    private String customerId;

    @ApiModelProperty(value = "访问ip")
    private String loginIp;

}
