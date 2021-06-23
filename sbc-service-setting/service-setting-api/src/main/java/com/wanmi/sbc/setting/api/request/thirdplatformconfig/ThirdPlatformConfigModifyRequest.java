package com.wanmi.sbc.setting.api.request.thirdplatformconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 第三方平台配置
 * Created by dyt on 2019/11/7.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPlatformConfigModifyRequest {

    /**
     * 配置ID
     */
    @NotBlank
    @ApiModelProperty(value="配置ID")
    private String configType;

    /**
     * 客户业务id
     */
    @ApiModelProperty(value="客户业务id 当configType=‘third_platform_linked_mall’时，才有")
    @Length(min = 17, max = 17)
    private String customerBizId;

    /**
     * 状态 0:未启用1:已启用
     */
    @NotNull
    @ApiModelProperty(value="0:未启用1:已启用")
    private Integer status;
}
