package com.wanmi.sbc.marketing.api.request.grouponsetting;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by feitingting on 2019/5/24.
 */
@ApiModel
@Data
public class GrouponPosterSaveRequest {

    @ApiModelProperty(value = "拼团广告，json字符串")
    private String poster;
}
