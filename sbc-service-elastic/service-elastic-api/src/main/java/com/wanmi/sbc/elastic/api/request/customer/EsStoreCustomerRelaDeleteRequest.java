package com.wanmi.sbc.elastic.api.request.customer;

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
 * @createTime 2018-09-12 14:55 <br>
 * @see com.wanmi.sbc.customer.api.request.store <br>
 * @since V1.0<br>
 */
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class EsStoreCustomerRelaDeleteRequest extends BaseRequest {

    private static final long serialVersionUID = 7161467477562613625L;

    /**
     * 用户标识
     */
    @ApiModelProperty(value = "用户标识")
    private String customerId;

    /**
     * 商家标识
     */
    @ApiModelProperty(value = "商家标识")
    private Long companyInfoId;
}
