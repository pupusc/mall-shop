package com.wanmi.sbc.setting.api.response.baseconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by feitingting on 2019/11/8.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BossLogoResponse implements Serializable {

    /**
     * PC商城logo
     */
    @ApiModelProperty(value = "PC商城logo")
    private String pcLogo;
}
