package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SettingBaseRequest implements Serializable {
    private static final long serialVersionUID = -5053399826249919351L;

    /**
     * 统一参数校验入口
     */
    public void checkParam(){}
}
