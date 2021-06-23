package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.PointsOrderType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.order.bean.enums.OrderSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName PointsTradeVo
 * @Description 积分订单Vo
 * @Author lvzhenwei
 * @Date 2019/5/10 13:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PointsTradeVO implements Serializable {
    private static final long serialVersionUID = 2640791189172261126L;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String id;

    /**
     * 订单组号
     */
    @ApiModelProperty(value = "订单组号")
    private String groupId;

    /**
     * 购买人
     */
    @ApiModelProperty(value = "购买人")
    private BuyerVO buyer;

    /**
     * boss卖方
     */
    @ApiModelProperty(value = "boss卖方")
    private SellerVO seller;

    /**
     * 商家
     */
    @ApiModelProperty(value = "商家")
    private SupplierVO supplier;

    /**
     * 买家备注
     */
    @ApiModelProperty(value = "买家备注")
    private String buyerRemark;

    /**
     * 卖家备注
     */
    @ApiModelProperty(value = "卖家备注")
    private String sellerRemark;

    /**
     * 订单附件，以逗号隔开
     */
    @ApiModelProperty(value = "订单附件，以逗号隔开")
    private String encloses;

    /**
     * 调用方的请求 IP
     * added by shenchunnan
     */
    @ApiModelProperty(value = "调用方的请求 IP")
    private String requestIp;

    /**
     * 订单总体状态
     */
    @ApiModelProperty(value = "订单总体状态")
    private TradeStateVO tradeState;

    /**
     * 收货人信息
     */
    @ApiModelProperty(value = "收货人信息")
    private ConsigneeVO consignee;

    /**
     * 订单价格
     */
    @ApiModelProperty(value = "订单价格")
    private TradePriceVO tradePrice;

    /**
     * 订单商品列表
     */
    @ApiModelProperty(value = "订单商品列表")
    private List<TradeItemVO> tradeItems = new ArrayList<>();

    /**
     * 积分订单优惠券
     */
    @ApiModelProperty(value = "积分订单优惠券")
    private TradePointsCouponItemVO tradeCouponItem;

    /**
     * 发货单
     */
    @ApiModelProperty(value = "发货单")
    private List<TradeDeliverVO> tradeDelivers = new ArrayList<>();

    /**
     * 配送方式
     */
    @ApiModelProperty(value = "配送方式")
    private DeliverWay deliverWay;

    @ApiModelProperty(value = "支付信息")
    private PayInfoVO payInfo;

    /**
     * 支付单ID
     */
    @ApiModelProperty(value = "支付单ID")
    private String payOrderId;

    /**
     * 订单来源方
     */
    @ApiModelProperty(value = "订单来源方")
    private Platform platform;

    /**
     * 订单所属第三方平台类型
     */
    @ApiModelProperty(value = "订单所属第三方平台类型")
    private ThirdPlatformType thirdPlatformType;

    /**
     * 订单所属第三方平台的订单id
     */
    @ApiModelProperty(value = "订单所属第三方平台的订单id")
    private List<String> thirdPlatformOrderIds;

    /**
     * 外部订单id
     * linkedMall -> 淘宝订单号
     */
    @ApiModelProperty(value = "外部订单id")
    private List<String> outOrderIds;

    /**
     * 超时未支付取消订单时间
     */
    @ApiModelProperty(value = "超时未支付取消订单时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTimeOut;

    /**
     * 操作日志记录（状态变更）
     */
    @ApiModelProperty(value = "操作日志记录")
    private List<TradeEventLogVO> tradeEventLogs = new ArrayList<>();

    /**
     * @return
     */
    @JsonIgnore
    public ConcurrentHashMap<String, TradeItemVO> skuItemMap() {
        return new ConcurrentHashMap<>(
                tradeItems.stream().collect(Collectors.toMap(TradeItemVO::getSkuId, Function.identity())));
    }

    /**
     * 是否被结算
     */
    @ApiModelProperty(value = "是否被结算", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean hasBeanSettled;

    /**
     * 是否可退标识
     */
    @ApiModelProperty(value = "是否可退标识", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean canReturnFlag;

    /**
     * 退款标识
     * 仅供结算使用 - 标识该订单是未收货的退款单子
     * <p>
     * 该单子flowState是作废不会入账，但是退单是COMPLETE状态会入账，导致收支不公，加了单独的状态作为判断
     */
    @ApiModelProperty(value = "退款标识")
    private Boolean refundFlag;

    /**
     * 订单来源--区分h5,pc,app,小程序,代客下单
     */
    @ApiModelProperty(value = "订单来源")
    private OrderSource orderSource;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private PayWay payWay;

    /**
     * 订单类型 0：普通订单；1：积分订单；
     */
    @ApiModelProperty(value = "订单类型 0：普通订单；1：积分订单")
    private OrderType orderType;

    /**
     * 积分订单类型 0：积分商品 1：积分优惠券
     */
    private PointsOrderType pointsOrderType;

    /**
     * 主订单id(商户)
     */
    private String parentId;


    /**
     * 子订单列表
     */
    @ApiModelProperty(value = "子订单列表")
    private List<PointsTradeVO> pointsTradeVOList;

    /**
     * 所属商户id-供应商使用
     */
    @ApiModelProperty(value = "所属商户id-供应商使用")
    private Long storeId;

    /**
     * 所属商户名称-供应商使用
     */
    @ApiModelProperty(value = "所属商户名称-供应商使用")
    private String supplierName;

    /**
     * 所属商户编号-供应商使用
     */
    @ApiModelProperty(value = "所属商户编号-供应商使用")
    private String supplierCode;

    /**
     * 第三方内部卖家名称
     */
    private String thirdSellerName;

    /**
     * 第三方内部卖家编号
     */
    private String thirdSellerId;

    /**
     * 第三方平台支付失败状态  true:失败 false:成功
     */
    private Boolean thirdPlatformPayErrorFlag;

    /**
     * 子单是否全都包含商家订单
     */
    @ApiModelProperty(value = "子单是否全都包含商家订单")
    private Boolean isContainsTrade;

}
