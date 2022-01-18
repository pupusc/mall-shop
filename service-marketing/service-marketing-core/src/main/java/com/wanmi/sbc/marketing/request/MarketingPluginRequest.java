package com.wanmi.sbc.marketing.request;


import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import com.wanmi.sbc.marketing.bean.vo.GrouponVO;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponCache;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 插件公共Request
 * Created by dyt on 2017/11/20.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketingPluginRequest implements Serializable {

    private static final long serialVersionUID = 3011079095798197792L;
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

    @ApiModelProperty(value = "优惠券使用场景1商详页+领券中心+购物车2专题页")
    private List<String> couponScene = Stream.of(CouponSceneType.DETAIL_CART_CENTER.getType().toString()).collect(Collectors.toList());
}

