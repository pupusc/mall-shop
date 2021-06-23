package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-12 10:26 <br>
 * @see com.wanmi.sbc.customer.api.request.store <br>
 * @since V1.0<br>
 */

@ApiModel
@Data
public class StoreCustomerQueryByEmployeeRequest extends BaseRequest {

    private static final long serialVersionUID = -8103093410245577747L;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String customerAccount;

    /**
     * 要查询的条数
     */
    @ApiModelProperty(value = "要查询的条数")
    private Integer pageSize;

    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    /**
     * 员工ID集合
     */
    @ApiModelProperty(value = "员工ID集合")
    private List<String> employeeIds;
}
