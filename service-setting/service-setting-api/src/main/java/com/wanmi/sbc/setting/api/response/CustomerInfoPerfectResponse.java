package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel
@Data
public class CustomerInfoPerfectResponse implements Serializable {
    private static final long serialVersionUID = 2160040680981518770L;
    /**
     * 0 true:需要完善信息 1 false:不需要完善信息
     */
    @ApiModelProperty(value = "已经完善信息-0 true:需要完善信息 1 false:不需要完善信息")
    private boolean perfect;
}
