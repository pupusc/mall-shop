package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CouponCodeStatus;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: chenli
 * @Date: Created In 10:18 AM 2018/9/19
 * @Description: 查询我的优惠券信息
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeVO implements Serializable {

    private static final long serialVersionUID = 1065888802669323068L;

    /**
     * 优惠券码id
     */
    @ApiModelProperty(value = "优惠券码id")
    private String couponCodeId;

    /**
     * 优惠券活动Id
     */
    @ApiModelProperty(value = "优惠券活动Id")
    private String activityId;

    /**
     *  优惠券码
     */
    @ApiModelProperty(value = "优惠券码")
    private String couponCode;

    /**
     *  使用状态,0(未使用)，1(使用)
     */
    @ApiModelProperty(value = "优惠券是否已使用")
    private DefaultFlag useStatus;

    /**
     *  使用时间
     */
    @ApiModelProperty(value = "使用时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime useDate;

    /**
     *  使用的订单号
     */
    @ApiModelProperty(value = "使用的订单号")
    private String orderCode;

    /**
     *  开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     *  结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 优惠券Id
     */
    @ApiModelProperty(value = "优惠券Id")
    private String couponId;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    /**
     * 购满类型 0：无门槛，1：满N元可使用
     */
    @ApiModelProperty(value = "购满类型")
    private FullBuyType fullBuyType;

    /**
     * 购满多少钱
     */
    @ApiModelProperty(value = "购满多少钱")
    private BigDecimal fullBuyPrice;

    /**
     * 优惠券面值
     */
    @ApiModelProperty(value = "优惠券面值")
    private BigDecimal denomination;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @ApiModelProperty(value = "是否平台优惠券")
    private DefaultFlag platformFlag;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    @ApiModelProperty(value = "优惠券类型")
    private CouponType couponType;

    /**
     *  优惠券创建时间
     */
    @ApiModelProperty(value = "优惠券创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @ApiModelProperty(value = "营销范围类型")
    private ScopeType scopeType;

    /**
     * 优惠券说明
     */
    @ApiModelProperty(value = "优惠券说明")
    private String couponDesc;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;


    /**
     * 是否即将过期 true 是 false 否
     */
    @ApiModelProperty(value = "是否即将过期")
    private boolean nearOverdue;

    /**
     * 是否可以立即使用 true 是(立即使用) false(查看可用商品)
     */
    @ApiModelProperty(value = "是否可以立即使用")
    private boolean couponCanUse;

    /**
     * 优惠券适用平台类目名称
     */
    @ApiModelProperty(value = "优惠券适用平台类目名称集合")
    private List<String> goodsCateNames;

    /**
     * 优惠券适用店铺分类名称
     */
    @ApiModelProperty(value = "优惠券适用店铺分类名称集合")
    private List<String> storeCateNames;

    /**
     * 优惠券适用品牌名称
     */
    @ApiModelProperty(value = "优惠券适用品牌名称集合")
    private List<String> brandNames;

    /**
     * 优惠券码状态(使用优惠券页券码的状态)
     */
    @ApiModelProperty(value = "使用优惠券码状态")
    private CouponCodeStatus status;

    /**
     * 参与成功通知标题
     */
    @ApiModelProperty(value = "参与成功通知标题")
    private String activityTitle;

    /**
     * 参与成功通知描述
     */
    @ApiModelProperty(value = "参与成功通知描述")
    private String activityDesc;
}
