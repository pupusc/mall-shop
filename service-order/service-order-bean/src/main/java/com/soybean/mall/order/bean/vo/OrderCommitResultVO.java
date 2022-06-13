package com.soybean.mall.order.bean.vo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.vo.ConsigneeVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradePriceVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderCommitResultVO implements Serializable {
    private static final long serialVersionUID = 3015950032444463773L;

    /**
     * 订单编号
     */
    private String id;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    private String parentId;

    private List<TradeItemVO> tradeItems;

    private ConsigneeVO consignee;

    private TradePriceVO tradePrice;

    private Boolean couponFlag;

    /**
     * 超时未支付取消订单时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTimeOut;

    
}
