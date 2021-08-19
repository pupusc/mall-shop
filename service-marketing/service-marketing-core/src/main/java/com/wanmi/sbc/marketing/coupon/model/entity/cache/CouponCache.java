package com.wanmi.sbc.marketing.coupon.model.entity.cache;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.common.BaseBean;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author: hht
 * @Date: Created In 11:15 AM 2018/9/12
 * @Description: 优惠券缓存 - mongo
 */
@EqualsAndHashCode(callSuper = true)
@Document
@Data
@Builder
public class CouponCache extends BaseBean {

    private static final long serialVersionUID = -6394339819079129787L;

    /**
     * 优惠券主键Id
     */
    private String id;

    /**
     * 优惠券活动配置表id
     */
    @Indexed(unique = true)
    private String activityConfigId;

    /**
     * 优惠券是否有剩余
     */
    private DefaultFlag hasLeft;

    /**
     * 优惠券总张数
     */
    private Long totalCount;

    /**
     * 优惠券活动Id
     */
    private String couponActivityId;

    /**
     * 优惠券Id
     */
    private String couponInfoId;

    /**
     * 优惠券关联 商品/分类/品牌 ids
     */
    private List<CouponMarketingScope> scopes;

    /**
     * 优惠券缓存
     */
    private CouponInfoCache couponInfo;

    /**
     * 优惠券活动缓存
     */
    private CouponActivityCache couponActivity;

    /**
     * 当前优惠券分类Id
     */
    private List<String> couponCateIds;

}
