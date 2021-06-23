package com.wanmi.sbc.customer.api.request.detail;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * <p>根据会员id查询单个会员明细request</p>
 * Created by daiyitian on 2018-08-13-下午3:47.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetailByCustomerIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -9155234181833842753L;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    @NotBlank
    private String customerId;

}
