package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxCreateNewAfterSaleRequest {

    /**
     * 订单号
     */
    @JSONField(name = "out_order_id")
    private String outOrderId;
    /**
     * 退单号
     */
    @JSONField(name = "out_aftersale_id")
    private String outAftersaleId;
    /**
     * 用户openid
     */
    @JSONField(name = "openid")
    private String openid;
    /**
     * 售后类型，1:退款,2:退款退货
     */
    @JSONField(name = "type")
    private Integer type;
    /**
     * 商品信息
     */
    @JSONField(name = "product_info")
    private ProductInfo productInfo;
    /**
     * 退款原因
     */
    @JSONField(name = "refund_reason")
    private String refundReason;
    /**
     * 退款原因类型
     * INCORRECT_SELECTION = 1; // 拍错/多拍
     * NO_LONGER_WANT = 2; // 不想要了
     * NO_EXPRESS_INFO = 3; // 无快递信息
     * EMPTY_PACKAGE = 4; // 包裹为空
     * REJECT_RECEIVE_PACKAGE = 5; // 已拒签包裹
     * NOT_DELIVERED_TOO_LONG = 6; // 快递长时间未送达
     * NOT_MATCH_PRODUCT_DESC = 7; // 与商品描述不符
     * QUALITY_ISSUE = 8; // 质量问题
     * SEND_WRONG_GOODS = 9; // 卖家发错货
     * THREE_NO_PRODUCT = 10; // 三无产品
     * FAKE_PRODUCT = 11; // 假冒产品
     * OTHERS = 12; // 其它
     */
    @JSONField(name = "refund_reason_type")
    private Integer refundReasonType;
    /**
     * 退款金额，单位分
     */
    @JSONField(name = "orderamt")
    private Long orderamt;

    @Data
    public static class ProductInfo {

        /**
         * goods id
         */
        @JSONField(name = "out_product_id")
        private String outProductId;
        /**
         * goodsInfo id
         */
        @JSONField(name = "out_sku_id")
        private String outSkuId;
        /**
         * 商品数量
         */
        @JSONField(name = "product_cnt")
        private Integer productCnt;
    }
}
