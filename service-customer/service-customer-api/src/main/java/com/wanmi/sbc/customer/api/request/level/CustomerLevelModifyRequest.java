package com.wanmi.sbc.customer.api.request.level;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 会员等级参数
 * Created by CHENLI on 2017/4/17.
 */
@ApiModel
@Data
public class CustomerLevelModifyRequest extends CustomerLevelAddRequest {

    private static final long serialVersionUID = 5826596763682497363L;
    /**
     * 客户等级ID
     */
    @ApiModelProperty(value = "客户等级ID")
    @NotNull
    private Long customerLevelId;

}
