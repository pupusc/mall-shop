package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-11 17:15 <br>
 * @see com.wanmi.sbc.customer.api.request.store <br>
 * @since V1.0<br>
 */

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerQueryRequest extends BaseRequest {

    private static final long serialVersionUID = 6528417086362296110L;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "客户ID列表")
    private List<String> customerIds;

    public StoreCustomerQueryRequest(Long storeId) {
        this.storeId = storeId;
    }
}
