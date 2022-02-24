package com.soybean.mall.wx.mini.order.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WxCreateAfterSaleRequest implements Serializable {
    private static final long serialVersionUID = 80446637791842273L;

    /**
     * 订单号
     */
    @JsonProperty("out_order_id")
    private String outOrderId;
    /**
     * 售后单ID
     */
    @JsonProperty("out_aftersale_id")
    private String outAftersaleId;
    private String openid;
    /**
     * 商家小程序该售后单的页面path，不存在则使用订单path
     */
    private String path;
    /**
     * 退款金额，单位：分
     */
    private BigDecimal refund;
    /**
     * 售后类型，1:退款,2:退款退货,3:换货
     */
    private Integer type;

    @JsonProperty("create_time")
    private String createTime;

    /**
     * 0:未受理,1:用户取消,2:商家受理中,3:商家逾期未处理,4:商家拒绝退款,5:商家拒绝退货退款,6:待买家退货,7:退货退款关闭,8:待商家收货,11:商家退款中,12:商家逾期未退款,13:退款完成,14:退货退款完成,15:换货完成,16:待商家发货,17:待用户确认收货,18:商家拒绝换货,19:商家已收到货
     */
    private Integer status;
    /**
     * 0:订单存在可售后商品，1:订单所有商品售后完成（订单维度）
     */
    @JsonProperty("finish_all_aftersale")
    private Integer finishAllAftersale;


}
