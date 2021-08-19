package com.wanmi.sbc.marketing.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@ApiModel
@Data
public class CouponCodeDTO {

    /**
     *  优惠券码id
     */
    @ApiModelProperty(value = "优惠券码id")
    private String couponCodeId;

    /**
     *  优惠券码
     */
    @ApiModelProperty(value = "优惠券券码")
    private String couponCode;

    /**
     * 优惠券Id
     */
    @ApiModelProperty(value = "优惠券Id")
    private String couponId;

    /**
     *  优惠券活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    private String activityId;

    /**
     *  领取人id,同时表示领取状态
     */
    @ApiModelProperty(value = "领取人id")
    private String customerId;

    /**
     *  使用状态,0(未使用)，1(使用)
     */
    @ApiModelProperty(value = "优惠券是否已使用")
    @Enumerated
    private DefaultFlag useStatus;

    /**
     *  获得优惠券时间
     */
    @ApiModelProperty(value = "获得优惠券时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime acquireTime;

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
     *  是否删除标志 0：否，1：是
     */
    @ApiModelProperty(value = "是否已删除")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     *  创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}
