package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.FollowFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ApiModel
public class GoodsCustomerFollowDTO {

    /**
     * 图片编号
     */
    @ApiModelProperty(value = "图片编号")
    private Long followId;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    /**
     * 全局购买数
     */
    @ApiModelProperty(value = "全局购买数")
    private Long goodsNum;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 采购创建时间
     */
    @ApiModelProperty(value = "采购创建时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 收藏标记
     */
    @ApiModelProperty(value = "收藏标记")
    private FollowFlag followFlag;

    /**
     * 收藏时间
     */
    @ApiModelProperty(value = "收藏时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime followTime;
}
