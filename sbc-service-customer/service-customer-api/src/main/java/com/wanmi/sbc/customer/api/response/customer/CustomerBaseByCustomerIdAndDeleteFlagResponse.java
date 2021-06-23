package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerBaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBaseByCustomerIdAndDeleteFlagResponse extends CustomerBaseRequest {
    private static final long serialVersionUID = -3854269857592932191L;

    /**
     * 会员基础信息
     */
    @ApiModelProperty(value = "会员基础信息")
    private CustomerBaseVO customerBaseVO;
}
