package com.soybean.mall.order.bean.vo;
import com.wanmi.sbc.order.bean.vo.ConsigneeVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradePriceVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCommitResultVO implements Serializable {
    private static final long serialVersionUID = 3015950032444463773L;

    /**
     * 订单编号
     */
    private String tid;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    private String parentTid;

    private List<TradeItemVO> tradeItems;

    private ConsigneeVO consignee;

    private TradePriceVO tradePrice;


    
}
