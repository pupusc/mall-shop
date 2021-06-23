package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class StoreCustomerQueryByCustomerNameRequest extends BaseRequest {

    private static final long serialVersionUID = 7387378366905279798L;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;
}
