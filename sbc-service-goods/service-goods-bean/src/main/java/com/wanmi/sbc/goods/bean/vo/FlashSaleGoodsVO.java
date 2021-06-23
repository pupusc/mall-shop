package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.FlashSaleGoodsStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>抢购商品表VO</p>
 *
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@ApiModel
@Data
public class FlashSaleGoodsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
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
    private String goodsInfoId;

    /**
     * spuID
     */
    @ApiModelProperty(value = "spuID")
    private String goodsId;

    /**
     * 抢购价
     */
    @ApiModelProperty(value = "抢购价")
    private BigDecimal price;

    /**
     * 上限数量
     */
    @ApiModelProperty(value = "上限数量")
    private Integer stock;

    /**
     * 抢购销量
     */
    @ApiModelProperty(value = "抢购销量")
    private Long salesVolume = 0L;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private Long cateId;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer maxNum = 0;

    /**
     * 起售数量
     */
    @ApiModelProperty(value = "起售数量")
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
    private Integer postage;

    /**
     * 删除标志，0:未删除 1:已删除
     */
    @ApiModelProperty(value = "删除标志，0:未删除 1:已删除")
    private DeleteFlag delFlag;

    /**
     * 分类信息
     */
    @ApiModelProperty(value = "分类信息")
    private FlashSaleCateVO flashSaleCateVO;

    /**
     * SPU信息
     */
    @ApiModelProperty(value = "SPU信息")
    private GoodsVO goods;

    /**
     * SKU信息
     */
    @ApiModelProperty(value = "SKU信息")
    private GoodsInfoVO goodsInfo;

    /**
     * 可兑换的最大库存
     */
    @ApiModelProperty(value = "可兑换的最大库存")
    private Long maxStock;

    /**
     * 规格信息
     */
    @ApiModelProperty(value = "规格信息")
    private String specText;

    /**
     * 秒杀商品状态
     */
    @ApiModelProperty(value = "秒杀商品状态")
    private FlashSaleGoodsStatus flashSaleGoodsStatus;

    /**
     * 是否可修改 0否 1是
     */
    @ApiModelProperty(value = "是否可编辑")
    private BoolFlag modifyFlag;

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

    /**
     * 活动日期+时间
     */
    @ApiModelProperty(value = "活动日期+时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime activityFullTime;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

}