package com.wanmi.sbc.marketing.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午1:58 2018/10/8
 * @Description: 订单营销插件请求类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeMarketingPluginRequest {

    /**
     * 客户id
     */
    String customerId;

    /**
     * 商品列表
     */
    List<TradeItemInfo> tradeItems;

    /**
     * 满系营销信息
     */
    private TradeMarketingRequest tradeMarketingDTO;

    /**
     * 优惠券码id
     */
    private String couponCodeId;

    /**
     * 是否强制提交（忽略失效营销）
     */
    private boolean forceCommit;
}
