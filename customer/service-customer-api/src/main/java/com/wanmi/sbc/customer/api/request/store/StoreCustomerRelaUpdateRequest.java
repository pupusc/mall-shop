package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.customer.bean.dto.StoreCustomerRelaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-12 14:35 <br>
 * @see com.wanmi.sbc.customer.api.request.store <br>
 * @since V1.0<br>
 */

@ApiModel
@Data
public class StoreCustomerRelaUpdateRequest extends BaseRequest {

    private static final long serialVersionUID = 6908227868404162376L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String customerId;

    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    /**
     * 店铺-用户关系 {@link StoreCustomerRelaDTO}
     */
    @ApiModelProperty(value = "店铺-用户关系")
    private StoreCustomerRelaDTO storeCustomerRelaDTO;
}
