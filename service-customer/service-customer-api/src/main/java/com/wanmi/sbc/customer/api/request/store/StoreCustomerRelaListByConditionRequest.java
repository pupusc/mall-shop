package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class StoreCustomerRelaListByConditionRequest extends BaseRequest {
    private static final long serialVersionUID = 2493780613748670846L;

    /**
     * 客户Id
     */
    @ApiModelProperty(value = "客户Id")
    private String customerId;

    /**
     * 商家Id
     */
    @ApiModelProperty(value = "商家Id")
    private Long companyInfoId;

    /**
     * 商铺Id
     */
    @ApiModelProperty(value = "商铺Id")
    private Long storeId;

    /**
     * 店铺等级标识
     */
    @ApiModelProperty(value = "店铺等级标识")
    private Long storeLevelId;

    /**
     * 批量商铺Id
     */
    @ApiModelProperty(value = "批量商铺Id")
    private List<Long> storeIds;

    /**
     * 客户类型
     */
    @ApiModelProperty(value = "客户类型")
    private CustomerType customerType;

}
