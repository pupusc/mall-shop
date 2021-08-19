package com.wanmi.sbc.crm.api.request.rfmstatistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>根据customerId查询最近一天的rfm会员统计明细request</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmCustomerDetailByCustomerIdRequest implements Serializable {

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id", required = true)
    @NotBlank
    private String customerId;
}
