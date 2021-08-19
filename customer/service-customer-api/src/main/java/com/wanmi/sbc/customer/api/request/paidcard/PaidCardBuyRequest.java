package com.wanmi.sbc.customer.api.request.paidcard;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaidCardBuyRequest {

    @NotBlank
    @ApiModelProperty("付费规则id")
    private String ruleId;
    @ApiModelProperty("会员ID")
    private String customerId;

    private CustomerVO customer;

}
