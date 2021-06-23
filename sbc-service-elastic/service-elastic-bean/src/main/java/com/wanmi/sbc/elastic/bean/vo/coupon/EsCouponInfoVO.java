package com.wanmi.sbc.elastic.bean.vo.coupon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.*;
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
 * @Author: songhanlin
 * @Date: Created In 10:18 AM 2018/9/12
 * @Description: 优惠券信息
 */
@ApiModel
@Data
public class EsCouponInfoVO implements Serializable {

    private static final long serialVersionUID = 8270069437627689630L;

    /**
     * 优惠券主键Id
     */
    @ApiModelProperty(value = "优惠券主键Id")
    private String couponId;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    /**
     * 起止时间类型 0：按起止时间，1：按N天有效
     */
    @ApiModelProperty(value = "起止时间类型")
    private RangeDayType rangeDayType;

    /**
     * 优惠券开始时间
     */
    @ApiModelProperty(value = "优惠券开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 优惠券结束时间
     */
    @ApiModelProperty(value = "优惠券结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 有效天数
     */
    @ApiModelProperty(value = "有效天数")
    private Integer effectiveDays;

    /**
     * 购满多少钱
     */
    @ApiModelProperty(value = "购满多少钱")
    private BigDecimal fullBuyPrice;

    /**
     * 购满类型 0：无门槛，1：满N元可使用
     */
    @ApiModelProperty(value = "购满类型")
    private FullBuyType fullBuyType;

    /**
     * 优惠券面值
     */
    @ApiModelProperty(value = "优惠券面值")
    private BigDecimal denomination;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "店铺id")
    @JsonIgnore
    private Long storeId;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @ApiModelProperty(value = "是否平台优惠券")
    @JsonIgnore
    private DefaultFlag platformFlag;

    /**
     * 营销范围类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @ApiModelProperty(value = "优惠券营销范围")
    private ScopeType scopeType;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    @ApiModelProperty(value = "优惠券类型")
    @JsonIgnore
    private CouponType couponType;
//
//    /**
//     * 是否删除标志 0：否，1：是
//     */
//    @ApiModelProperty(value = "是否已删除")
//    private DeleteFlag delFlag;
//
//    /**
//     * 创建时间
//     */
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
//    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    private LocalDateTime createTime;

    /**
     * 是否已经绑定营销活动 0否 1是
     */
    @ApiModelProperty(value = "是否已经绑定营销活动")
    private DefaultFlag  isFree;

    /**
     * 促销范围Ids
     */
    @ApiModelProperty(value = "优惠券关联的商品范围id集合(可以为0(全部)，brand_id(品牌id)，cate_id(分类id), goods_info_id(货品id))")
    @JsonIgnore
    private List<String> scopeIds = new ArrayList<>();

    /**
     * 优惠券分类Ids
     */
    @ApiModelProperty(value = "优惠券分类Id集合")
    @JsonIgnore
    private List<String> cateIds = new ArrayList<>();

    /**
     * 关联分类-只有分类名
     */
    @ApiModelProperty(value = "优惠券分类名集合")
    private List<String> cateNames = new ArrayList<>();

    /**
     * 指定商品-只有scopeName
     */
    @ApiModelProperty(value = "关联的商品范围名称集合，如分类名、品牌名")
    private  List<String> scopeNames = new ArrayList<>();

    /**
     * 优惠券状态
     */
    private CouponStatus couponStatus;

    public CouponStatus getCouponStatus() {
        if (Objects.equals(RangeDayType.DAYS, this.getRangeDayType())) {
            return CouponStatus.DAYS;
        } else {
            if (this.getStartTime() != null && this.getEndTime() != null) {
                if (LocalDateTime.now().isBefore(this.getStartTime())) {
                    return CouponStatus.NOT_START;
                } else if (LocalDateTime.now().isAfter(this.getEndTime())) {
                    return CouponStatus.ENDED;
                } else {
                    return CouponStatus.STARTED;
                }
            }
        }
        return null;
    }


}
