package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListCustomerIdByPageableRequest extends BaseQueryRequest {
    private static final long serialVersionUID = -3854269857592932191L;

    /**
     * 会员等级ID集合
     */
    @ApiModelProperty(value = "会员等级ID集合")
    private List<Long> customerLevelIds;
}
