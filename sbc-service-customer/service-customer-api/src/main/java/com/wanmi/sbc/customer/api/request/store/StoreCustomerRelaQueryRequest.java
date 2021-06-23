package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-13 13:58 <br>
 * @see com.wanmi.sbc.customer.api.request.store <br>
 * @since V1.0<br>
 */

@ApiModel
@Data
public class StoreCustomerRelaQueryRequest extends BaseRequest {

    private static final long serialVersionUID = 5826272274195467351L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String customerId;

    /**
     * 商家ID
     */
    @ApiModelProperty(value = "商家ID")
    private Long companyInfoId;

    /**
     * 是否查平台
     */
    @ApiModelProperty(value = "是否查平台")
    private Boolean queryPlatform;
}
