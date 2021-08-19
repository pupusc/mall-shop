package com.wanmi.sbc.order.bean.dto.yzorder;

import com.wanmi.sbc.order.bean.dto.yzorder.deliver.MultiPeriodOrderDeliverDTO;
import com.wanmi.sbc.order.bean.dto.yzorder.deliver.YzOrderDeliverDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 有赞订单对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YzOrderDTO implements Serializable {

    private static final long serialVersionUID = -7825396290765940672L;

    /**
     * 订单号
     */
    private String id;

    /**
     * 交易基础信息结构体
     */
    private FullOrderInfoDTO full_order_info;

    /**
     * 订单发货详情结构体
     */
    private List<DeliveryOrderDTO> delivery_order;

    /**
     * 订单优惠详情结构体
     */
    private OrderPromotionDTO order_promotion;

    /**
     * 订单退款信息结构体
     */
    private List<RefundOrderDTO> refund_order;

    /**
     * 发货单信息
     */
    private List<YzOrderDeliverDTO> delivery_order_detail;

    /**
     * 周期购最新一次发货记录
     */
    private List<MultiPeriodOrderDeliverDTO> multi_period_order_deliver;

    /**
     * 是否已经转换
     */
    private Boolean convertFlag = Boolean.FALSE;

}
