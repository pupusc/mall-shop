package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品SKU实体类
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class GoodsInfoPageSimpleVO implements Serializable {

    private static final long serialVersionUID = 3973312626817597962L;

    /**
     * 商品SKU编号
     */
    @ApiModelProperty(value = "商品SKU编号")
    private String goodsInfoId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * 商品SKU名称
     */
    @ApiModelProperty(value = "商品SKU名称")
    private String goodsInfoName;

    /**
     * 商品SKU编码
     */
    @ApiModelProperty(value = "商品SKU编码")
    private String goodsInfoNo;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String goodsInfoImg;

    /**
     * 商品条形码
     */
    @ApiModelProperty(value = "商品条形码")
    private String goodsInfoBarcode;

    /**
     * 商品库存
     */
    @ApiModelProperty(value = "商品库存")
    private Long stock;

    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    @CanEmpty
    private BigDecimal supplyPrice;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

    /**
     * 是否定时上架
     */
    @ApiModelProperty(value = "是否定时上架 true:是,false:否")
    private Boolean addedTimingFlag;

    /**
     * 定时上架时间
     */
    @ApiModelProperty(value = "定时上架时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTimingTime;

    /**
     * 是否可售
     */
    @ApiModelProperty(value = "是否可售", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer vendibility;


    /**
     * 设价类型 0:客户,1:订货
     */
    @ApiModelProperty(value = "设价类型", dataType = "com.wanmi.sbc.goods.bean.enums.PriceType")
    private Integer priceType;

    /**
     * 商品分页，扁平化多个商品规格值ID
     */
    @ApiModelProperty(value = "商品分页，扁平化多个商品规格值ID")
    private List<Long> specDetailRelIds;

    /**
     * 购买积分
     */
    @ApiModelProperty(value = "购买积分")
    private Long buyPoint;

    /**
     * 企业购商品的审核状态
     */
    @ApiModelProperty(value = "企业购商品的审核状态", dataType = "com.wanmi.sbc.customer.bean.enums.EnterpriseAuditState")
    private EnterpriseAuditState enterPriseAuditState;

    /**
     * 企业购商品审核被驳回的原因
     */
    @ApiModelProperty(value = "企业购商品审核被驳回的原因")
    private String enterPriseGoodsAuditReason;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String cateName;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    /**
     * 预售价格
     */
    @ApiModelProperty(value = "预售价格")
    private BigDecimal bookingPrice;

    /**
     * 预约价格
     */
    @ApiModelProperty(value = "预约价格")
    private BigDecimal appointmentPrice;

    /**
     * 预售活动信息
     */
    @ApiModelProperty(value = "预售活动信息")
    private BookingSaleVO bookingSaleVO;


    /**
     * 预约活动信息
     */
    @ApiModelProperty(value = "预约活动信息")
    private AppointmentSaleVO appointmentSaleVO;

    private GoodsVO goods;

    /**
     * 所属供应商商品skuId
     */
    @ApiModelProperty(value = "所属供应商商品skuId")
    private String providerGoodsInfoId;

    /**
     * 所属供应商商品sku编码
     */
    @ApiModelProperty(value = "所属供应商商品sku编码")
    private String providerGoodsInfoNo;

    /**
     * 供应商Id
     */
    @ApiModelProperty(value = "供应商Id")
    private Long providerId;

    /**
     * 第三方平台的spuId
     */
    @ApiModelProperty(value = "第三方平台的spuId")
    private String thirdPlatformSpuId;

    /**
     * 第三方平台的skuId
     */
    @ApiModelProperty(value = "第三方平台的skuId")
    private String thirdPlatformSkuId;

    /**
     * 商品来源，0供应商，1商家 2linkedMall
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家 2linkedMall")
    private Integer goodsSource;

    /**
     *三方平台类型，0，linkedmall
     */
    @ApiModelProperty("三方平台类型，0，linkedmall")
    private ThirdPlatformType thirdPlatformType;

    /**
     * 供应商店铺状态 0：关店 1：开店
     */
    @ApiModelProperty(value = "供应商店铺状态 0：关店 1：开店")
    private Integer providerStatus;

    public Integer getVendibility(){
        if (Objects.nonNull(providerGoodsInfoId)) {
            //供应商商品可售（商品上架、未删除、已审核，店铺开店）
            if((Objects.nonNull(vendibility) && DefaultFlag.YES.toValue() == vendibility)
                    && Constants.yes.equals(providerStatus)){
                return Constants.yes;
            } else {
                return Constants.no;
            }
        }
        return Constants.yes;
    }

    /**
     * 单品选择的营销
     */
    private GoodsMarketingVO goodsMarketing;

    /**
     * 规格值关联列表
     */
    private List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelList;

    /**
     * 级别价列表
     */
    private List<GoodsLevelPriceVO> goodsLevelPriceList;

    /**
     * 区间价列表
     */
    private List<GoodsIntervalPriceVO> intervalPriceList;
}