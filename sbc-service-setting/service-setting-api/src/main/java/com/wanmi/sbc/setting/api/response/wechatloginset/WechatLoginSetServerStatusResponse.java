package com.wanmi.sbc.setting.api.response.wechatloginset;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>获取授信开关状态</p>
 *
 * @date 2019-11-11 16:15:25
 */

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatLoginSetServerStatusResponse implements Serializable {

    private static final long serialVersionUID = 6511135264567462223L;

    /**
     * h5-微信授权登录是否启用 0 不启用， 1 启用
     */
    @ApiModelProperty(value = "h5-微信授权登录是否启用 0 不启用， 1 启用")
    private DefaultFlag mobileStatus;

    /**
     * pc-微信授权登录是否启用 0 不启用， 1 启用
     */
    @ApiModelProperty(value = "pc-微信授权登录是否启用 0 不启用， 1 启用")
    private DefaultFlag pcStatus;

    /**
     * app-微信授权登录是否启用 0 不启用， 1 启用
     */
    @ApiModelProperty(value = "app-微信授权登录是否启用 0 不启用， 1 启用")
    private DefaultFlag appStatus;
}
