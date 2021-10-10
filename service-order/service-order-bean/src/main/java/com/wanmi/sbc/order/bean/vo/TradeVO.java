package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.OrderExternalSource;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.PointsOrderType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeGrouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.OrderSource;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单状态
 * Created by jinwei on 14/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeVO implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     *子订单列表
     */
    @ApiModelProperty(value = "子订单列表")
    private List<TradeVO> tradeVOList;

    /**
     *子订单列表,新增接口的子单列表用这个
     */
    @ApiModelProperty(value = "子订单列表")
    private List<ProviderTradeVO> providerTradeVOList;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String id;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    @ApiModelProperty(value = "父订单号，用于不同商家订单合并支付场景")
    private String parentId;

    /**
     * 有赞订单id
     */
    private String yzTid;

    /**
     * 主订单号
     */
    @ApiModelProperty(value = "主订单号")
    private String tradeId;

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
     * 发票
     */
    @ApiModelProperty(value = "发票")
    private InvoiceVO invoice;

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
     * 是否是全部卡券商品(不含赠品)
     */
    @ApiModelProperty(value = "是否是全部卡券商品(不含赠品)")
    private Boolean isVirtualCouponGoods;
    /**
     * 是否是全部卡券商品(赠品)
     */
    @ApiModelProperty(value = "是否是全部卡券商品(赠品)")
    private Boolean isVirtualCouponGiveawayGoods;
    /**
     * 订单所属第三方平台类型
     */
    @ApiModelProperty(value = "订单所属第三方平台类型")
    private ThirdPlatformType thirdPlatformType;
    /**
     * 订单外部来源
     */
    private OrderExternalSource orderExternalSource;
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
     * 下单时是否已开启订单自动审核
     */
    @ApiModelProperty(value = "下单时是否已开启订单自动审核", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean isAuditOpen = true;

    /**
     * 订单支付顺序
     */
    @ApiModelProperty(value = "订单支付顺序")
    private PaymentOrder paymentOrder;
    /**
     * 虚拟商品直冲手机号
     */
    private String directChargeMobile;
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
     * 分销渠道类型
     */
    @ApiModelProperty(value = "分销渠道类型")
    private ChannelType channelType;

    /**
     * 小B店铺内分享链接携带的邀请人ID（会员ID）
     */
    private String distributionShareCustomerId;

    /**
     * 邀请人分销员id
     */
    @ApiModelProperty(value = "邀请人分销员id")
    private String distributorId;

    /**
     * 邀请人会员id
     */
    @ApiModelProperty(value = "邀请人会员id")
    private String inviteeId;

    /**
     * 基础分销设置-小店名称
     */
    @ApiModelProperty(value = "小店名称")
    private String shopName;

    /**
     * 佣金（订单返利）
     */
    @ApiModelProperty(value = "佣金（订单返利）")
    private BigDecimal commission;

    /**
     * 是否返利
     */
    @ApiModelProperty(value = "是否返利",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean commissionFlag;

    /**
     * 分销单品列表
     */
    private List<TradeDistributeItemVO> distributeItems = new ArrayList<>();


    /**
     * @return
     */
    @JsonIgnore
    public ConcurrentHashMap<String, TradeItemVO> skuItemMap() {
        return new ConcurrentHashMap<>(
                tradeItems.stream().collect(Collectors.toMap(TradeItemVO::getSkuId, Function.identity())));
    }

    /**
     * @return
     */
    @JsonIgnore
    public ConcurrentHashMap<String, TradeItemVO> giftSkuItemMap() {
        return new ConcurrentHashMap<>(
                gifts.stream().collect(Collectors.toMap(TradeItemVO::getSkuId, Function.identity())));
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
     * 订单营销信息
     */
    @ApiModelProperty(value = "订单营销信息")
    private List<TradeMarketingVO> tradeMarketings;

    /**
     * 订单使用的店铺优惠券
     */
    @ApiModelProperty(value = "订单使用的店铺优惠券")
    private TradeCouponVO tradeCoupon;

    /**
     * 营销赠品全量列表
     */
    @ApiModelProperty(value = "营销赠品全量列表")
    private List<TradeItemVO> gifts = new ArrayList<>();

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
     * 开店礼包
     */
    @ApiModelProperty(value = "开店礼包")
    private DefaultFlag storeBagsFlag = DefaultFlag.NO;


    /**
     * 是否组合套装
     */
    @ApiModelProperty(value = "是否组合套装")
    private Boolean suitMarketingFlag;

    /**
     * 分销员名称
     */
    @ApiModelProperty(value = "分销员名称")
    private String distributorName;

    /**
     * 可退积分
     */
    @ApiModelProperty(value = "可退积分")
    private Long canReturnPoints;

    /**
     * 可退知豆
     */
    @ApiModelProperty(value = "可退知豆")
    private Long canReturnKnowledge;

    /**
     * 已退金额
     */
    @ApiModelProperty(value = "已退金额")
    private BigDecimal canReturnPrice;

    /**
     * 订单类型 0：普通订单；1：积分订单；
     */
    @ApiModelProperty(value = "订单类型 0：普通订单；1：积分订单")
    private OrderType orderType;

    /**
     * 积分订单类型 0：积分商品 1：积分优惠券
     */
    @ApiModelProperty(value = "积分订单类型 0：积分商品 1：积分优惠券")
    private PointsOrderType pointsOrderType;

    /**
     * 是否拼团订单
     */
    private boolean grouponFlag;

    /**
     * 订单拼团信息
     */
    private TradeGrouponVO tradeGroupon;

    /**
     * 分销佣金提成信息列表
     */
    private List<TradeCommissionVO> commissions;

    /**
     * 总佣金(返利人佣金 + 提成人佣金)
     */
    private BigDecimal totalCommission;

    /**
     * 所属商户id-供应商使用
     */
    private Long storeId;

    /**
     * 所属商户名称-供应商使用
     */
    private String supplierName;

    /**
     * 所属商户编号-供应商使用
     */
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
    private Boolean isContainsTrade;

    /**
     * 是否是预售商品
     */
    private Boolean isBookingSaleGoods;

    /**
     * 预售类型
     */
    private BookingType bookingType;

    /**
     * 尾款通知手机号
     */
    private String tailNoticeMobile;

    /**
     * 尾款支付单ID
     */
    private String tailPayOrderId;

    /**
     * 尾款订单号
     */
    private String tailOrderNo;

    /**
     * 樊登积分抵扣码
     */
    @ApiModelProperty(value = "樊登积分抵扣码")
    private String deductCode;
    /**
     * 未使用的优惠券
     */
    @ApiModelProperty(value = "未使用的优惠券")
    private List<CouponCodeVO> couponCodes;

    /**
     * 积分订单的子订单列表，拷贝PointsTradeVO时使用
     */
    @ApiModelProperty(value = "积分订单的子订单列表，拷贝PointsTradeVO时使用")
    private List<PointsTradeVO> pointsTradeVOList;

    /**
     * 积分订单优惠券，拷贝PointsTradeVO时使用
     */
    @ApiModelProperty(value = "积分订单优惠券，拷贝PointsTradeVO时使用")
    private TradePointsCouponItemVO tradeCouponItem;

    /**
     * 是否周期购订单
     */
    @ApiModelProperty(value = "是否周期购订单")
    private Boolean cycleBuyFlag = Boolean.FALSE;

    /**
     * 是否是有赞订单
     */
    private Boolean yzOrderFlag = Boolean.FALSE;

    /**
     * 周期购信息
     */
    @ApiModelProperty(value = "周期购信息")
    private TradeCycleBuyInfoVO tradeCycleBuyInfo;

    private String deliveryOrderId;
}
