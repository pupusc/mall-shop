package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoDeleteCustomerGetByFanDengRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 4490114343049955112L;

    @ApiModelProperty(value = "樊登用户id")
    @NotNull
    private String fanDengId;
}
