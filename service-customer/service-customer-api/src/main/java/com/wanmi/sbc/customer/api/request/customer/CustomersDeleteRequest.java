package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomersDeleteRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 5545555160440882586L;

    @ApiModelProperty(value = "会员id列表")
    @NotNull
    private List<String> customerIds;
}
