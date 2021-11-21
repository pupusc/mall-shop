package com.wanmi.sbc.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求参数基类
 * Created by aqlu on 15/11/26.
 */
@ApiModel
@Data
public class RequestParam implements Serializable {

    @ApiModelProperty(value = "请求参数v")
    private String v;
}
