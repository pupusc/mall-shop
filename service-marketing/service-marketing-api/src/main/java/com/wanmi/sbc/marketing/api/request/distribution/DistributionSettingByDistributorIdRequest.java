package com.wanmi.sbc.marketing.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: baijz
 * @Date: Created In 下午4:18 2019/2/19
 * @Description:
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionSettingByDistributorIdRequest {

    /**
     * 邀请人id
     */
    @NotNull
    @ApiModelProperty(value = "邀请人id")
    private String inviteeId;

}
