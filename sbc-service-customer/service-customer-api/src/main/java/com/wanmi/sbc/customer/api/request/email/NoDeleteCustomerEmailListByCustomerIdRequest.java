package com.wanmi.sbc.customer.api.request.email;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取邮箱列表请求类
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoDeleteCustomerEmailListByCustomerIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -8057219900342417574L;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "邮箱所属客户Id")
    private String customerId;

}
