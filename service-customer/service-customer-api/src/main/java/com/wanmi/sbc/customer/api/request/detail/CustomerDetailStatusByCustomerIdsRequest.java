package com.wanmi.sbc.customer.api.request.detail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 根据customerid查询会员是否被禁用以及禁用原因
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailStatusByCustomerIdsRequest implements Serializable {

    private static final long serialVersionUID = -7395969401233896552L;

    /**
     * 批量多个会员ID
     */
    @ApiModelProperty(value = "批量多个会员ID")
    @NotEmpty
    private List<String> customerIds;


}
