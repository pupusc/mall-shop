package com.soybean.mall.wx.mini.order.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.util.List;

@Data
public class WxDetailAfterSaleResponse extends WxResponseBase {

    @JSONField(name = "after_sales_order")
    private AfterSalesOrder afterSalesOrder;

    @Data
    public static class AfterSalesOrder {

        @JSONField(name = "out_order_id")
        private String outOrderId;
        @JSONField(name = "order_id")
        private Long orderId;

//        @JSONField(name = "out_aftersale_id")
//        private String outAftersaleId;
        @JSONField(name = "aftersale_id")
        private Long aftersaleId;

        @JSONField(name = "product_info")
        private ProductInfo productInfo;

        @JSONField(name = "media_list")
        private List<MediaListInfo> mediaList;

        /**
         * 售后类型，1:退款,2:退款退货
         */
        @JSONField(name = "type")
        private Integer type;
        /**
         * 物流信息
         */
        @JSONField(name = "return_info")
        private ReturnInfo returnInfo;
        @JSONField(name = "orderamt")
        private Long orderamt;
        @JSONField(name = "refund_reason_type")
        private Integer refundReasonType;
        @JSONField(name = "refund_reason")
        private String refundReason;
        @JSONField(name = "status")
        private Integer status;
        @JSONField(name = "createTime")
        private String create_time;
        @JSONField(name = "updateTime")
        private String update_time;
        @JSONField(name = "openid")
        private String openid;

        @JSONField(name = "refund_pay_detail")
        private RefundPayDetail refundPayDetail;

    }

    @Data
    public static class ProductInfo {

        @JSONField(name = "out_product_id")
        private String outProductId;
        @JSONField(name = "product_id")
        private Long productId;
        @JSONField(name = "out_sku_id")
        private String outSkuId;
        @JSONField(name = "sku_id")
        private Long skuId;
        @JSONField(name = "product_cnt")
        private Long productCnt;
    }

    @Data
    public static class ReturnInfo {

        @JSONField(name = "order_return_time")
        private Long orderReturnTime;
        /**
         * 物流单号
         */
        @JSONField(name = "waybill_id")
        private String waybillId;

        /**
         * code
         */
        @JSONField(name = "delivery_id")
        private String deliveryId;

        /**
         * code name
         */
        @JSONField(name = "delivery_name")
        private String deliveryName;
    }

    @Data
    public static class MediaListInfo {

        @JSONField(name = "type")
        private Integer type;

        @JSONField(name = "url")
        private String url;

        @JSONField(name = "thumb_url")
        private String thumbUrl;
    }

    @Data
    public static class RefundPayDetail {
        @JSONField(name = "refund_id")
        private String refundId;
    }
}
