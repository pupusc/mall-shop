package com.wanmi.sbc.marketing.request;


import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.marketing.bean.vo.GrouponVO;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponCache;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 插件公共Request
 * Created by dyt on 2017/11/20.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketingPluginRequest {

    /**
     * 当前客户
     */
    private CustomerVO customer;

    /**
     * 当前客户的等级
     * 内容：<店铺ID，等级信息>
     */
    private Map<Long, CommonLevelVO> levelMap;

    /**
     * 营销Map
     * 内容:<SkuId,多个营销>
     */
    private Map<String, List<MarketingResponse>> marketingMap;

    /**
     * 优惠券Map
     * 内容:<SkuId,多个优惠券>
     */
    private Map<String, List<CouponCache>> couponMap;

    /**
     * 拼团营销信息
     * 用户是否拼团
     * 开团or参团-是否团长
     */
    private GrouponVO grouponVO;

    /**
     * 是否魔方商品列表
     */
    private Boolean moFangFlag;

    /**
     * 是否为秒杀走购物车普通商品提交订单
     */
    private Boolean isFlashSaleMarketing;


    /**
     * 是否提交订单commit接口
     */
    private Boolean commitFlag = Boolean.FALSE;

    /**
     * 是否设置独立字段（付费会员价使用）
     */
    private Boolean isIndependent = Boolean.FALSE;

    /**
     * 使用场景
     */
    private Integer couponScene;
}
