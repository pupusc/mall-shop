package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel
@Data
public class SwitchModifyResponse implements Serializable {
    private static final long serialVersionUID = 850444020476649184L;
    /**
     * 修改的记录数
     */
    @ApiModelProperty(value = "总数")
    private Integer count;
}
