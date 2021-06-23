package com.wanmi.sbc.customer.api.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCustomerRelaListCustomerIdByStoreIdResponse implements Serializable {

    private static final long serialVersionUID = 5826272274195467351L;

    /**
     * 会员ID集合
     */
    @ApiModelProperty(value = "会员ID集合")
    private List<String> customerIds;
}
