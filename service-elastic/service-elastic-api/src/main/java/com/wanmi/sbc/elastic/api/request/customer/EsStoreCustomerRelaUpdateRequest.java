package com.wanmi.sbc.elastic.api.request.customer;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
@Data
public class EsStoreCustomerRelaUpdateRequest extends BaseRequest {

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
     * 商家标识
     */
    @ApiModelProperty(value = "商家标识")
    private Long companyInfoId;

    /**
     * 店铺等级标识
     */
    @ApiModelProperty(value = "店铺等级标识")
    private Long storeLevelId;
}
