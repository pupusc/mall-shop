package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员角色-根据公司ID查询Request
 */
@ApiModel
@Data
public class RoleInfoListRequest extends CustomerBaseRequest {

    /**
     * 公司编号
     */
    @ApiModelProperty(value = "公司编号")
    private Long companyInfoId;
}
