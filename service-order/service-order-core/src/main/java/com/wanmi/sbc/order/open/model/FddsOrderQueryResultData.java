package com.wanmi.sbc.order.open.model;

import lombok.Data;

import java.io.*;
import java.math.BigDecimal;

/**
 * @author Liang Jun
 * @date 2022-04-06 11:47:00
 */
@Data
public class FddsOrderQueryResultData implements Serializable {
    /**
     * 合作方订单号
     */
    private String tradeNo;
    /**
     * 樊登读书订单号
     */
    private Long orderNumber;
    /**
     * 创建时间(时间戳)
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 订单状态(NEW:新订单；COMPLETE:已完成)
     */
    private String orderStatus;
    /**
     * 会员编号
     */
    private Integer userId;
    /**
     * 订单总价(元)
     */
    private BigDecimal totalFee;
    /**
     * 商品编号
     */
    private Integer productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 支付方式
     */
    private String payType;
}
