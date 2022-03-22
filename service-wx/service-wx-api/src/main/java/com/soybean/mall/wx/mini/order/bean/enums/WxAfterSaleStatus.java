package com.soybean.mall.wx.mini.order.bean.enums;


/**
 * 微信订单售后状态
 */
public enum WxAfterSaleStatus {

    REJECT_REFUND(4, "商家拒绝退款"),

    REJECT_RETURN(5, "商家拒绝退货退款"),

    WAIT_RETURN(6, "待买家退货"),

    REFUNDING(11,"商家退款中"),

    REFUNDED(13,"退款完成"),

    RETURNED(14,"退货退款完成");

    private Integer id;

    private String description;

    WxAfterSaleStatus(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
