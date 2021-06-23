package com.wanmi.sbc.customer.api.request.company;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
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
 * @createTime 2018-09-13 14:36 <br>
 * @see com.wanmi.sbc.customer.api.request.company <br>
 * @since V1.0<br>
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyInfoQueryRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -4124584228791500329L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String customerId;
}
