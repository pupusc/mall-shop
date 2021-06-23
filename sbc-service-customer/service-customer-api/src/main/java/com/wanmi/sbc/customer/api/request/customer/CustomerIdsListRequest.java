package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class CustomerIdsListRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -1585004539318321019L;

    @ApiModelProperty(value = "会员ids")
    @NotNull
    private List<String> customerIds;
}
