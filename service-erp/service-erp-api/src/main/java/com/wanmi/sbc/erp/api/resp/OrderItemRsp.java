package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.util.Date;

@Data
public class OrderItemRsp extends OrderSaleRsp {

    private static final long serialVersionUID = 7863032136712277278L;

    /**
     * 平台子单ID
     */
    private String platformItemId;

    /**
     * 仓库ID
     */
    private Long whId;

    /**
     * 仓库编码
     */
    private String whCode;


    /**
     * 业务类型 1:实物商品 2：虚拟商品-樊登，3：图书订阅商品 4-历史虚拟
     */
    private Integer metaGoodsType;

    /**
     * 成本价
     */
    private Integer costPrice;

    /**
     * 成本价
     */
    private Integer marketPrice;


    /**
     * 物流编码
     */
    private String expressCode;

    /**
     * 物流单号
     */
    private String expressNo;

    /**
     * 签收时间
     */
    private Date signatureTime;

    /**
     * 优惠金额
     */
    private Integer discountFee;

    /**
     * 实付金额
     */
    private Integer oughtFee;

    /**
     * 商品包Id，可用于判断是否为包商品
     */
    private Long orderPackId;

    /**
     * 是否为赠品，0非赠送，1赠送，3补偿
     */
    private Integer giftFlag;


    /**
     * 状态
     *
     * @see com.soybean.unified.order.api.enums.UnifiedOrderItemStatusEnum
     */
    private Integer status;

    /**
     * 货品ID
     */
    private Long metaGoodsId;

    /**
     * 货品规格ID
     */
    private Long metaSkuId;

    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 会期类型 0 体验，1 正式
     */
    private Integer rightsCategory;
    /**
     * 退货数量
     */
    private Integer refundNum;

    /**
     * 退款金额
     */
    private Integer refundFee;


    /**
     * 库存类型 1-真实库存 2-预售库存
     */
    private Integer stockType;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 计划发货时间
     */
    private Date planDeliveryTime;
}
