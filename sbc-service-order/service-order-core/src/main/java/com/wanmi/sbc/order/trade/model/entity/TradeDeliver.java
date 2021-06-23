package com.wanmi.sbc.order.trade.model.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.ShipperType;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.Logistics;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 发货单
 *
 * @author wumeng[OF2627]
 * company qianmi.com
 * Date 2017-04-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeDeliver implements Serializable {

    /**
     * 发货单属于的订单号
     */
    private String tradeId;

    /**
     * 订单的所属商家/供应商
     */
    private String providerName;

    /**
     * 发货单号
     */
    private String deliverId;
    /**
     * 物流信息
     */
    private Logistics logistics;

    /**
     * 收货人信息
     */
    private Consignee consignee;
    

    /**
     * 是否卡券商品
     */
    private  Boolean isVirtualCoupon;

    /**
     * 发货清单
     */
    private List<ShippingItem> shippingItems = new ArrayList<>();

    /**
     * 赠品信息
     */
    private List<ShippingItem> giftItemList = new ArrayList<>();

    /**
     * 发货时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deliverTime;

    /**
     * 发货状态
     */
    private DeliverStatus status;

    /**
     * 子订单 发货单号
     */
    @ApiModelProperty(value = "子订单 发货单号")
    private String sunDeliverId;

    /**
     * 所属 商家/供应商
     */
    @ApiModelProperty(value = "所属 商家/供应商")
    private ShipperType shipperType;

    /**
     * 第三方平台订单物流标识
     */
    @ApiModelProperty(value = "第三方平台订单物流标识")
    private ThirdPlatformType thirdPlatformType;

    /**
     * 期数
     */
    @ApiModelProperty(value = "期数")
    private Integer cycleNum;

    @ApiModelProperty(value = "（老订单）物流信息")
    private String expressProgressInfo;

}
