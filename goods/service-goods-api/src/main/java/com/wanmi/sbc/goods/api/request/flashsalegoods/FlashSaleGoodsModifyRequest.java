package com.wanmi.sbc.goods.api.request.flashsalegoods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>抢购商品表修改参数</p>
 *
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsModifyRequest extends GoodsBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    /**
     * 活动日期：2019-06-11
     */
    @ApiModelProperty(value = "活动日期：2019-06-11")
    private String activityDate;

    /**
     * 活动时间：13:00
     */
    @ApiModelProperty(value = "活动时间：13:00")
    private String activityTime;

    /**
     * skuID
     */
    @ApiModelProperty(value = "skuID")
    @NotBlank
    private String goodsInfoId;

    /**
     * spuID
     */
    @ApiModelProperty(value = "spuID")
    @NotBlank
    private String goodsId;


    /**
     * 抢购价
     */
    @ApiModelProperty(value = "抢购价")
    @NotNull
    private BigDecimal price;

    /**
     * 上限数量
     */
    @ApiModelProperty(value = "上限数量")
    @NotNull
    private Integer stock;

    /**
     * 抢购销量
     */
    @ApiModelProperty(value = "抢购销量")
    @NotNull
    private Long salesVolume;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    @NotNull
    private Long cateId;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer maxNum;

    /**
     * 起售数量
     */
    @ApiModelProperty(value = "起售数量")
    @NotNull
    private Integer minNum;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 包邮标志，0：不包邮 1:包邮
     */
    @ApiModelProperty(value = "包邮标志，0：不包邮 1:包邮")
    @NotNull
    private Integer postage;

    /**
     * 删除标志，0:未删除 1:已删除
     */
    @ApiModelProperty(value = "删除标志，0:未删除 1:已删除")
    private DeleteFlag delFlag;

    /**
     * 活动日期+时间
     */
    @ApiModelProperty(value = "活动日期+时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime activityFullTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updatePerson;

}