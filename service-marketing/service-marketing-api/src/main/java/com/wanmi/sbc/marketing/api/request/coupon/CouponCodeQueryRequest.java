package com.wanmi.sbc.marketing.api.request.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 1:23 PM 2018/9/28
 * @Description: 查询优惠券码列表请求
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeQueryRequest extends BaseQueryRequest  {

    private static final long serialVersionUID = -1924077031934211666L;

    /**
     * 领取人id
     */
    @ApiModelProperty(value = "领取人id")
    private String customerId;

    /**
     * 优惠券是否已使用,0(未使用)，1(使用)
     */
    @ApiModelProperty(value = "优惠券是否已使用")
    private DefaultFlag useStatus;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private String couponId;

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    private String activityId;

    /**
     * 查询未过期的券
     */
    @ApiModelProperty(value = "查询未过期的券")
    private Boolean notExpire;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "是否已删除")
    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 优惠券id集合
     */
    @ApiModelProperty(value = "优惠券id集合")
    private List<String> couponIds;

    @ApiModelProperty(value = "获得优惠券-开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime acquireStartTime;

    @ApiModelProperty(value = "获得优惠券-结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime acquireEndTime;

    @ApiModelProperty(value = "优惠券使用到期时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;


}
