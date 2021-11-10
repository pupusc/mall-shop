package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TopicStoreyGoodsDTO implements Serializable {
    private static final long serialVersionUID = 6469272901302359606L;

    private Integer id;

    @ApiModelProperty("楼层id")
    private Integer storeyId;
    @ApiModelProperty("spu编码")
    private String spuNo;
    @ApiModelProperty("sku编码")
    private String skuNo;
    @ApiModelProperty("skuId")
    private String skuId;
    @ApiModelProperty("类型1商品+图片2图片+链接")
    private Integer type;
    @ApiModelProperty("图片地址")
    private String imageUrl;
    @ApiModelProperty("链接地址")
    private String linkUrl;
    @ApiModelProperty("排序")
    private Integer sorting;
}
