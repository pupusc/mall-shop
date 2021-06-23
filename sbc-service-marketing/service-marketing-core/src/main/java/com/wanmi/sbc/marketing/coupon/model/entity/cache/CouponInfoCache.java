package com.wanmi.sbc.marketing.coupon.model.entity.cache;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import com.wanmi.sbc.marketing.common.BaseBean;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: hht
 * @Date: Created In 11:15 AM 2018/9/12
 * @Description: 优惠券缓存
 */
@Data
@Builder
public class CouponInfoCache extends BaseBean {

    private static final long serialVersionUID = -7565084760860098844L;

    /**
     * 优惠券主键Id
     */
    private String couponId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 起止时间类型 0：按起止时间，1：按N天有效
     */
    private RangeDayType rangeDayType;

    /**
     * 优惠券开始时间
     */
    private LocalDateTime startTime;

    /**
     * 优惠券结束时间
     */
    private LocalDateTime endTime;

    /**
     * 有效天数
     */
    private Integer effectiveDays;

    /**
     * 购满多少钱
     */
    private Double fullBuyPrice;

    /**
     * 购满类型 0：无门槛，1：满N元可使用
     */
    private FullBuyType fullBuyType;

    /**
     * 优惠券面值
     */
    private Double denomination;

    /**
     * 商户id
     */
    private Long storeId;

    /**
     * 是否平台优惠券 0平台 1店铺
     */
    private DefaultFlag platformFlag;

    /**
     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    private ScopeType scopeType;

    /**
     * 优惠券说明
     */
    private String couponDesc;

    /**
     * 优惠券类型，用于分页排序
     */
    private Integer couponTypeInteger;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    private CouponType couponType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
