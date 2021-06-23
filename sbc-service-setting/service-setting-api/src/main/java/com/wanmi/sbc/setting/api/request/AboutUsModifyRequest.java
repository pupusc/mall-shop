package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: songhanlin
 * @Date: Created In 09:58 2018/11/22
 * @Description: 关于我们修改请求Request
 */
@ApiModel
@Data
public class AboutUsModifyRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -8390891337631462612L;

    /**
     * 关于我们内容
     */
    @ApiModelProperty(value = "关于我们内容")
    @NotNull
    private String context;
}
