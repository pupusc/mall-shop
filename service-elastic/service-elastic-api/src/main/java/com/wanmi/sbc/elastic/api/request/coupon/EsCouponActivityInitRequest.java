package com.wanmi.sbc.elastic.api.request.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class EsCouponActivityInitRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 4243718077145628609L;


    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     *优惠券活动名称
     */
    @ApiModelProperty(value = "优惠券活动名称")
    private String activityName;


    /**
     * 优惠券活动类型
     */
    @ApiModelProperty(value = "优惠券活动类型")
    private CouponActivityType couponActivityType;

    /**
     *开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     *结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     *目标客户 -1:全部客户 0:全部等级 other:其他等级 ,
     */
    @ApiModelProperty(value = "关联的客户等级", dataType = "com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel")
    private String joinLevel;

    @ApiModelProperty(value = "查询类型")
    private MarketingStatus queryTab;

    @ApiModelProperty(value = "活动ID集合")
    private List<String> activityIds;

}
