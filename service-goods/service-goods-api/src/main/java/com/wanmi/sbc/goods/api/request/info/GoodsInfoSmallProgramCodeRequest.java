package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by feitingting on 2019/1/8.
 */
@ApiModel
@Data
public class GoodsInfoSmallProgramCodeRequest {
    /**
     * sku编号
     */
    @ApiModelProperty(value = "sku编号")
    private String goodsInfoId;

    /**
     * 小程序码地址
     */
    @ApiModelProperty(value = "小程序码地址")
    private String codeUrl;
}

