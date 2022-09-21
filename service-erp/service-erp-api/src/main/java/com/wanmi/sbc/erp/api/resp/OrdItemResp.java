package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrdItemResp implements Serializable {
    private Long tid;

    /**
     * 子订单分类：1：正常单，2：补偿单
     */
    private Integer orderItemType;

    /**
     * 商品子类型，解决虚拟商品对应的会期、课程以及其他相关的数据查询问题,对应：sku类型
     */
    private Integer metaGoodsSubType;

    private Integer metaGoodsType;

    private Long metaGoodsId;

    private String metaGoodsName;

    private Long metaSkuId;

    private String metaSkuName;

    private String platformOrderId;

    private String platformItemId;

    private String platformSkuId;

    private Integer goodsId;

    private Integer productId;

    private String saleCode;

    private Integer marketPrice;

    private Integer price;

    private Integer num;

    private Integer status;

    private Integer stockType;

    private String expressCode;

    private String expressNo;

    private Long whId;

    private String whCode;

    private Integer costPrice;

    private Integer costPostFee;

    private Integer discountFee;

    private Integer oughtFee;

    private Integer refundStatus;

    private Integer refundFee;

    private String platformCode;

    private Integer deliveryStatus;

    private Date deliveryTime;

    private Date signatureTime;

    private Date planDeliveryTime;

    private Integer giftFlag;

    private String giftObjectType;

    private Long giftObjectId;

    private Long orderId;

    private Long orderPackId;

    private String shopId;
}
