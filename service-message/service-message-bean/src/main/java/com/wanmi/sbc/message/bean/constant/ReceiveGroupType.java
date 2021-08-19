package com.wanmi.sbc.message.bean.constant;

import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName ReceiveGroupType
 * @Description TODO
 * @Author daiyitian
 * @Date 2019/12/6 16:37
 **/
public class ReceiveGroupType {

    /**
     * 系统分组
     */
    @ApiModelProperty(value = "系统分组")
    public static final String SYS = "0";

    /**
     * 自定义分组
     */
    @ApiModelProperty(value = "自定义分组")
    public static final String CUSTOM = "1";
}
