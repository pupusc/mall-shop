package com.wanmi.sbc.marketing.api.request.grouponactivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 驳回拼团活动请求对象
 */
@ApiModel
@Data
public class GrouponActivityRefuseRequest implements Serializable {

    /**
     * 活动ID
     */
    @ApiModelProperty(value = "审核拼团活动，grouponActivityId")
    @NotNull
    private String grouponActivityId;

    /**
     * 分销拼团活动不通过原因
     */
    @NotBlank
    @ApiModelProperty(value = "拼团活动不通过原因")
    private String auditReason;
}
