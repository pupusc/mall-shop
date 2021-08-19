package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
public class ConfigUpdateRequest implements Serializable{

    private static final long serialVersionUID = 7726275470517935663L;

    /**
     * 键
     */
    @ApiModelProperty(value = "键")
    private String configKey;


    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer status;


}
