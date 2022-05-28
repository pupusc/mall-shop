package com.soybean.mall.wx.mini.order.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class WxVideoOrderDetailResponse extends WxResponseBase {

    @JSONField(name = "order")
    private WxVideoOrderResponse order;

    @Data
    public static class WxVideoOrderResponse {

        @JSONField(name = "order_id")
        private String orderId;

        @JSONField(name = "out_order_id")
        private String outOrderId;

        @JSONField(name = "status")
        private Integer status;

        @JSONField(name = "path")
        private String path;

        @JSONField(name = "order_detail")
        private OrderDetail orderDetail;

    }

    @Data
    public static class OrderDetail {

        //商品信息
        @JSONField(name = "product_infos")
        private List<ProductInfos> productInfos;
        //价格信息
        @JSONField(name = "pay_info")
        private PayInfo payInfo;
        //支付信息

    }

    @Data
    public static class ProductInfos{

        @JSONField(name = "product_id")
        private Long productId;
        //spuId
        @JSONField(name = "out_product_id")
        private String outProductId;

        @JSONField(name = "sku_id")
        private Long skuId;

        @JSONField(name = "out_sku_id")
        private String outSkuId;

        /**
         * 商品数量
         */
        @JSONField(name = "product_cnt")
        private Integer productCnt;
    }

    @Data
    public static class PayInfo{

        @JSONField(name = "pay_method_type")
        private Integer payMethodType;
    }
}