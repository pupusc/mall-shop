package com.wanmi.sbc.marketing.api.request.grouponsetting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;


/**
 * Created by feitingting on 2019/5/24.
 */
@ApiModel
@Data
public class GrouponRuleSaveRequest {

    @ApiModelProperty(value = "拼团规则，字符串")
    private String rule;
}
