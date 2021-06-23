package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGetForSupplierRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -1709264705378683734L;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "供应商下的客户")
    private Long companyInfoId;

    @ApiModelProperty(value = "商铺Id")
    private Long storeId;
}
