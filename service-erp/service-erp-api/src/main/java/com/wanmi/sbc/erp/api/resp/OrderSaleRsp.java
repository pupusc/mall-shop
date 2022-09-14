package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSaleRsp implements Serializable {

    /**
     */
    private Long tid;

    private Long orderId;

    private Integer price;

    private String saleCode;

    private Integer num;

    /**
     * 第三方平台商品ID
     */
    private String platformGoodsId;

    /**
     * 第三方平台规格ID
     */
    private String platformSkuId;

    /**
     * 第三方平台商品名称
     */
    private String platformGoodsName;

    /**
     * 第三方平台规格名称
     */
    private String platformSkuName;
}
