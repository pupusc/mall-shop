package com.wanmi.sbc.customer.api.request.detail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 根据会员ID集合查询会员详情集合
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailListByCustomerIdsRequest implements Serializable {

    private static final long serialVersionUID = -7395969401233896552L;

    /**
     * 批量多个会员ID
     */
    @ApiModelProperty(value = "批量多个会员ID")
    @NotEmpty
    private List<String> customerIds;


}
