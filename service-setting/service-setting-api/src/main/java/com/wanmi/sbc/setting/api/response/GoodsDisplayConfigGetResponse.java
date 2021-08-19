package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 增专资质配置
 */
@ApiModel
@Data
public class GoodsDisplayConfigGetResponse implements Serializable {

    private static final long serialVersionUID = -6822963856850723024L;
    /**
     * 0:SKU 1:SPU
     */
    @ApiModelProperty(value = "商品维度-0:SKU 1:SPU",dataType = "com.wanmi.sbc.setting.bean.enums.GoodsShowType")
    private Integer goodsShowType;
    /**
     * 0:小图 1:大图
     */
    @ApiModelProperty(value = "图片显示方式-0:小图 1:大图",dataType = "com.wanmi.sbc.setting.bean.enums.ImageShowType")
    private Integer imageShowType;
}
