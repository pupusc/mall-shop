package com.wanmi.sbc.customer.api.request.growthvalue;

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
public class CustomerByGrowthValueRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -7621730806420281178L;

    @ApiModelProperty(value = "会员成长值")
    @NotNull
    private Long growthValue;
}
