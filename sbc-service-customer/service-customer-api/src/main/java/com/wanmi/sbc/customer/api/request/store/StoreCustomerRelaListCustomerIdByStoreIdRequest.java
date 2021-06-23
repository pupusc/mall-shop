package com.wanmi.sbc.customer.api.request.store;

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
public class StoreCustomerRelaListCustomerIdByStoreIdRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 5826272274195467351L;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 店铺等级ID
     */
    @ApiModelProperty(value = "店铺等级ID")
    private List<Long> storeLevelIds;
}
