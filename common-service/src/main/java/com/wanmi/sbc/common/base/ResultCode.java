package com.wanmi.sbc.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 结果码
 * Created by aqlu on 15/11/30.
 */
@ApiModel
public class ResultCode {

    /**
     * 操作成功；
     */
    @ApiModelProperty(value = "操作成功")
    public static final String SUCCESSFUL = "K-000000";

    /**
     * 操作失败；
     */
    @ApiModelProperty(value = "操作失败")
    public static final String FAILED = "K-000001";
}
