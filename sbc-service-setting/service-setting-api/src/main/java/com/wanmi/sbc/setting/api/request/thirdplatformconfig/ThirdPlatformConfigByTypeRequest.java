package com.wanmi.sbc.setting.api.request.thirdplatformconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方平台配置
 * Created by dyt on 2019/11/7.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPlatformConfigByTypeRequest {

    /**
     * 配置ID
     */
    @ApiModelProperty(value="配置ID")
    private String configType;
}
