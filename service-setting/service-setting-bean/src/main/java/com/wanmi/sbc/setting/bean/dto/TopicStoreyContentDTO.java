package com.wanmi.sbc.setting.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
public class TopicStoreyContentDTO extends TopicStoreyCouponDTO implements Serializable {
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
    @ApiModelProperty("类型1商品+图片2图片+链接3异行轮播4导航5优惠券")
    private Integer type;
    @ApiModelProperty("图片地址")
    private String imageUrl;
    @ApiModelProperty("链接地址")
    private String linkUrl;
    @ApiModelProperty("排序")
    private Integer sorting;
    @ApiModelProperty("商品ID")
    private String spuId;
    @ApiModelProperty("开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "属性",hidden = true)
    private String attributeInfo;

    @ApiModelProperty("导航关联楼层Id")
    private Integer relStoreyId;
}
