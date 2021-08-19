package com.wanmi.sbc.marketing.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel
@Data
public class CouponInfoDTO {

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
    @Enumerated
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
    @Enumerated
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
    private Long storeId;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @ApiModelProperty(value = "是否平台优惠券")
    @Enumerated
    private DefaultFlag platformFlag;

    /**
     * 营销范围类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @ApiModelProperty(value = "营销范围类型")
    @Enumerated
    private ScopeType scopeType;

    /**
     * 优惠券说明
     */
    @ApiModelProperty(value = "优惠券说明")
    private String couponDesc;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    @ApiModelProperty(value = "优惠券类型")
    @Enumerated
    private CouponType couponType;

    /**
     * 是否删除标志 0：否，1：是
     */
    @ApiModelProperty(value = "是否已删除")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}
