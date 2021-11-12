package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class TopicStoreyContentDTO implements Serializable {
    private static final long serialVersionUID = 6469272901302359606L;

    @NotNull
    @ApiModelProperty("楼层id")
    private Integer storeyId;
    @NotNull
    @ApiModelProperty("专题Id")
    private Integer topicId;
    @ApiModelProperty("spu编码")
    private String spuNo;
    @ApiModelProperty("sku编码")
    private String skuNo;
    @ApiModelProperty("skuId")
    private String skuId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("类型1商品+图片2图片+链接")
    private Integer type;
    @ApiModelProperty("图片地址")
    private String imageUrl;
    @ApiModelProperty("链接地址")
    private String linkUrl;
    @ApiModelProperty("排序")
    private Integer sorting;
}
