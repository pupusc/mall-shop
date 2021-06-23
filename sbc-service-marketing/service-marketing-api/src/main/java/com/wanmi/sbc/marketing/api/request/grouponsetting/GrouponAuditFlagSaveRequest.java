package com.wanmi.sbc.marketing.api.request.grouponsetting;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class GrouponAuditFlagSaveRequest {

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启拼团商品审核")
    @NotNull
    private DefaultFlag auditFlag;

}
