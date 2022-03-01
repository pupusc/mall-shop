package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soybean.mall.wx.mini.order.bean.dto.WxProductDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class WxDeliverySendRequest implements Serializable {
    private static final long serialVersionUID = -2733761653186206206L;
    /**
     * 商家自定义订单ID
     */
    @JSONField(name ="out_order_id")
    private String outOrderId;
    private String openid;
    /**
     *发货完成标志位, 0: 部分发货, 1:全部发货
     */
    @JSONField(name ="finish_all_delivery")
    private Integer finishAllDelivery;
    @JSONField(name ="delivery_list")
    private List<WxDeliveryInfo> deliveryList;

    @Data
    public static class WxDeliveryInfo {
        /**
         * 快递公司ID，通过获取快递公司列表获取
         */
        @JSONField(name ="delivery_id")
        private String deliveryId;
        /**
         * 快递单号
         */
        @JSONField(name ="waybill_id")
        private String waybillId;
        @JSONField(name ="product_info_list")
        private List<WxProductDTO> productInfoList;

    }
   
}
