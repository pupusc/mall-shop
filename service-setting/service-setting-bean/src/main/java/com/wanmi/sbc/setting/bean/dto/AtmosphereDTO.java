package com.wanmi.sbc.setting.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AtmosphereDTO implements Serializable {


    private static final long serialVersionUID = -4653924566911253775L;
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    @ApiModelProperty("氛围类型1通用类型2积分类型")
    private Integer atmosType;

    @ApiModelProperty("商品编码")
    private String skuNo;

    @ApiModelProperty("skuId")
    private String skuId;

    @ApiModelProperty("商品名称")
    private String goodsInfoName;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "元素内容",hidden = true)
    private String elementDesc;

    @ApiModelProperty("元素1,左侧第一行文字")
    private String elementOne;

    @ApiModelProperty("元素2：左侧第二行文字")
    private String elementTwo;

    @ApiModelProperty("元素3：右侧第一行文字")
    private String elementThree;

    @ApiModelProperty("元素4：右侧第二行文字")
    private String elementFour;

}
