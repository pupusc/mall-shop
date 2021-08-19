package com.wanmi.sbc.trade.request;


import com.wanmi.sbc.common.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class BatchDeliverCheckRequest implements Serializable{

    private static final long serialVersionUID = -5514593295638480862L;

    /**
     * 文件后缀
     */
    @ApiModelProperty(value = "文件后缀")
    private String ext;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "平台类型")
    private Platform platform;
}
