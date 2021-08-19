package com.wanmi.sbc.elastic.api.request.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CouponStatus;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品库查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsCouponInfoInitRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -4485444157498437822L;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @ApiModelProperty(value = "是否平台优惠券")
    private DefaultFlag platformFlag;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 模糊条件-优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称模糊条件查询")
    private String likeCouponName;

    /**
     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @ApiModelProperty(value = "营销范围类型")
    private ScopeType scopeType;


    @ApiModelProperty(value = "查询类型")
    private CouponStatus couponStatus;

    /**
     * 是否只查询有效的优惠券（优惠券活动选择有效的优惠券）
     */
    @ApiModelProperty(value = "是否只查询有效的优惠券")
    private DefaultFlag isMarketingChose;

    /**
     * 查询开始时间，精确到天
     */
    @ApiModelProperty(value = "查询开始时间，精确到天")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 查询结束时间，精确到天
     */
    @ApiModelProperty(value = "查询结束时间，精确到天")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;


    @ApiModelProperty(value = "是否已删除")
    private DeleteFlag delFlag;

    /**
     * 批量优惠券id
     */
    @ApiModelProperty(value = "优惠券id列表")
    private List<String> couponIds;
}
