package com.soybean.mall.marketing.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CouponCodeStatus;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/20 10:50 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CouponByCustomerResp {
    /**
     *  开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     *  结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 优惠券码id
     */
    private String couponCodeId;

//    /**
//     * 优惠券Id
//     */
//    private String couponId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券面值
     */
    private BigDecimal denomination;

    /**
     * 满减金额显示内容
     */
    private String fullContent;

    /**
     * 平台内容
     */
    private String platformContent;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    private CouponType couponType;

    /**
     * 优惠券营销范围
     */
    private String scopeContent;
//
//    /**
//     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
//     */
//    @ApiModelProperty(value = "营销范围类型")
//    private ScopeType scopeType;

    /**
     * 是否即将过期 true 是 false 否
     */
    private boolean nearOverdue;

    /**
     * 是否可以立即使用 true 是(立即使用) false(查看可用商品)
     */
    private boolean couponCanUse;


    /**
     * 优惠券码状态(使用优惠券页券码的状态)
     */
    private CouponCodeStatus status;

}
