package com.wanmi.sbc.order.trade.reponse;

import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.order.trade.model.entity.PayInfo;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表返回对象
 * Created by sunkun on 2017/9/20.
 */
@Data
public class TradeReponse {

    private String id;

    /**
     * 订单总体状态
     */
    private TradeState tradeState;

    /**
     * 订单商品列表
     */
    private List<TradeItem> tradeItems = new ArrayList<>();

    /**
     * 订单价格
     */
    private TradePrice tradePrice;

    /**
     * 订单支付信息
     */
    private PayInfo payInfo;

    /**
     * 商户信息
     */
    private Supplier supplier;

    /**
     * 是否可退标识
     */
    private Boolean canReturnFlag;

    /**
     * 订单营销信息
     */
    private List<TradeMarketingVO> tradeMarketings;

    /**
     * 订单来源方
     */
    private Platform platform;

    /**
     * 营销赠品全量列表
     */
    private List<TradeItem> gifts = new ArrayList<>();

}
