package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.ShipperType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ApiModel
public class TradeDeliverDTO extends BaseQueryRequest {

    /**
     * 发货单属于的订单号
     */
    @ApiModelProperty(value = "发货单属于的订单号")
    private String tradeId;

    /**
     * 订单的所属商家/供应商 名称
     */
    @ApiModelProperty(value = "订单的所属商家/供应商")
    private String providerName;

    /**
     * 发货单号
     */
    @ApiModelProperty(value = "发货单号")
    private String deliverId;

    /**
     * 物流信息
     */
    @ApiModelProperty(value = "物流信息")
    private LogisticsDTO logistics;

    /**
     * 收货人信息
     */
    @ApiModelProperty(value = "收货人信息")
    private ConsigneeDTO consignee;
    /**
     * 是否卡券商品
     */
    private  Boolean isVirtualCoupon;
    /**
     * 发货清单
     */
    @ApiModelProperty(value = "发货清单")
    private List<ShippingItemDTO> shippingItems = new ArrayList<>();

    /**
     * 赠品信息
     */
    @ApiModelProperty(value = "赠品信息")
    private List<ShippingItemDTO> giftItemList = new ArrayList<>();

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deliverTime;

    /**
     * 发货状态
     */
    @ApiModelProperty(value = "发货状态")
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
     * 周期购订单 -- 总期数
     */
    @ApiModelProperty(value = "总期数")
    private Integer cycleNum;

    /**
     * 周期购订单 --  第几期
     */
    @ApiModelProperty(value = "第几期")
    private Integer  issuesCycleNum;

    @ApiModelProperty(value = "(老订单)物流信息")
    private String expressProgressInfo;

}
