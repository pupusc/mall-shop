package com.wanmi.sbc.marketing.api.request.distribution;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午6:00 2019/3/13
 * @Description:
 */
@ApiModel
@Data
public class DistributionOpenFlagSaveRequest {

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启社交分销")
    @NotNull
    private DefaultFlag openFlag;

}
