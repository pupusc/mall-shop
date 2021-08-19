package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员角色-根据角色ID查询Request
 */
@ApiModel
@Data
public class RoleInfoQueryRequest extends CustomerBaseRequest {

    /**
     * 角色Id
     */
    @ApiModelProperty(value = "角色Id")
    private Long roleInfoId;
}
