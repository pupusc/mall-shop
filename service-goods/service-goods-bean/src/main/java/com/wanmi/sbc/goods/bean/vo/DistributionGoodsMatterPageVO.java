package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.MatterType;
import com.wanmi.sbc.goods.bean.enums.MediaType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel
public class DistributionGoodsMatterPageVO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商品sku的id")
    private String goodsInfoId;

    @ApiModelProperty(value = "素材类型")
    private MatterType matterType;

    @ApiModelProperty(value = "素材")
    private String matter;

    @ApiModelProperty(value = "推荐语")
    private String recommend;

    @ApiModelProperty(value = "推荐次数")
    private Integer recommendNum;

    @ApiModelProperty(value = "发布者id")
    private String operatorId;

    @ApiModelProperty(value = "发布者名称")
    private String name;

    @ApiModelProperty(value = "发布者账号")
    private String account;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "商品信息")
    private GoodsInfoVO goodsInfo;
}
