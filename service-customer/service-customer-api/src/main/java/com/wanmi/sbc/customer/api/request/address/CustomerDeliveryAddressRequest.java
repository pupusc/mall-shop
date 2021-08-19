package com.wanmi.sbc.customer.api.request.address;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员收货地址-根据用户ID查询Request
 */
@ApiModel
@Data
public class CustomerDeliveryAddressRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -5513227067125147002L;

    /**
     * 收货地址ID
     */
    @ApiModelProperty(value = "收货地址ID")
    private String deliveryAddressId;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "会员标识UUID")
    private String customerId;

    /**
     * 是否是默认地址 0：否 1：是
     */
    @ApiModelProperty(value = "是否是默认地址(0:否,1:是)", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isDefaltAddress;

    /**
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志(0:未删除,1:已删除)", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;
}
