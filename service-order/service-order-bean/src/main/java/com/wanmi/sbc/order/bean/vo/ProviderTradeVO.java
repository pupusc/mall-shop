package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.enums.*;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeGrouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.order.bean.enums.EvaluateStatus;
import com.wanmi.sbc.order.bean.enums.OrderSource;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

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
public class ProviderTradeVO implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * 子订单id(供应商)
     */
    @Id
    private String id;

    /**
     * 主订单id(商户)
     */
    private String parentId;

    /**
     * 有赞订单id
     */
    private String yzTid;

    /**
     * 所属经营商户id
     */
    private Long storeId;

    /**
     * 所属经营商户名称
     */
    private String storeName;

    /**
     * 订单组号
     */
    private String groupId;

    /**
     * 购买人
     */
    private BuyerVO buyer;

    /**
     * boss卖方
     */
    private SellerVO seller;

    /**
     * 供应商
     */
    private SupplierVO supplier;

    /**
     * 买家备注
     */
    private String buyerRemark;

    /**
     * 卖家备注
     */
    private String sellerRemark;

    /**
     * 虚拟商品直冲手机号
     */
    private String directChargeMobile;

    /**
     * 是否是全部卡券商品(不含赠品)
     */
    private Boolean isVirtualCouponGoods;
    /**
     * 是否是全部卡券商品(赠品)
     */
    private Boolean isVirtualCouponGiveawayGoods;


    /**
     * 订单附件，以逗号隔开
     */
    private String encloses;

    /**
     * 调用方的请求 IP
     * added by shenchunnan
     */
    private String requestIp;

    /**
     * 发票
     */
    private InvoiceVO invoice;
    /**
     * 订单外部来源
     */
    private OrderExternalSource orderExternalSource;
    /**
     * 订单总体状态
     */
    private TradeStateVO tradeState;

    /**
     * 收货人信息
     */
    private ConsigneeVO consignee;

    /**
     * 订单价格
     */
    private TradePriceVO tradePrice;

    /**
     * 订单商品列表
     */
    private List<TradeItemVO> tradeItems = new ArrayList<>();

    /**
     * 积分订单优惠券
     */
    private TradePointsCouponItemVO tradeCouponItem;

    /**
     * 发货单
     */
    private List<TradeDeliverVO> tradeDelivers = new ArrayList<>();

    /**
     * 配送方式
     */
    private DeliverWay deliverWay;


    private PayInfoVO payInfo;

    /**
     * 支付单ID
     */
    private String payOrderId;


    /**
     * 订单来源方
     */
    private Platform platform;

    /**
     * 订单所属第三方平台类型
     */
    private ThirdPlatformType thirdPlatformType;

    /**
     * 订单所属第三方平台的订单id
     */
    private List<String> thirdPlatformOrderIds;

    /**
     * 第三方平台支付失败状态  true:失败 false:成功
     */
    private Boolean thirdPlatformPayErrorFlag;

    /**
     * 下单时是否已开启订单自动审核
     */
    private Boolean isAuditOpen = true;

    /**
     * 订单支付顺序
     */
    private PaymentOrder paymentOrder;

    /**
     * 超时未支付取消订单时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderTimeOut;

    /**
     * 操作日志记录（状态变更）
     */
    private List<TradeEventLogVO> tradeEventLogs = new ArrayList<>();

    /**
     * 分销渠道类型
     */
    private ChannelType channelType;

    /**
     * 小B店铺内分享链接携带的邀请人ID（会员ID）
     */
    private String distributionShareCustomerId;

    /**
     * 返利人分销员id
     */
    private String distributorId;

    /**
     * 返利人会员id
     */
    private String inviteeId;

    /**
     * 小店名称
     */
    private String shopName;

    /**
     * 返利人名称
     */
    private String distributorName;

    /**
     * 开店礼包
     */
    private DefaultFlag storeBagsFlag = DefaultFlag.NO;

    /**
     * 开店礼包邀请人id
     */
    private String storeBagsInviteeId;

    /**
     * 分销单品列表
     */
    private List<TradeDistributeItemVO> distributeItems = new ArrayList<>();

    /**
     * 返利人佣金
     */
    private BigDecimal commission = BigDecimal.ZERO;

    /**
     * 总佣金(返利人佣金 + 提成人佣金)
     */
    private BigDecimal totalCommission = BigDecimal.ZERO;

    /**
     * 提成人佣金列表
     */
    private List<TradeCommissionVO> commissions = new ArrayList<>();

    /**
     * 是否返利
     */
    private Boolean commissionFlag = Boolean.FALSE;

    /**
     * 正在进行的退单数量
     */
    private Integer returnOrderNum = 0;

    /**
     * 是否被结算
     */
    private Boolean hasBeanSettled;

    /**
     * 是否可退标识
     */
    private Boolean canReturnFlag;

    /**
     * 退款标识
     * 仅供结算使用 - 标识该订单是未收货的退款单子
     * <p>
     * 该单子flowState是作废不会入账，但是退单是COMPLETE状态会入账，导致收支不公，加了单独的状态作为判断
     */
    private Boolean refundFlag;

    /**
     * 订单营销信息
     */
    private List<TradeMarketingVO> tradeMarketings;

    /**
     * 订单使用的店铺优惠券
     */
    private TradeCouponVO tradeCoupon;

    /**
     * 营销赠品全量列表
     */
    private List<TradeItemVO> gifts = new ArrayList<>();

    /**
     * 订单来源--区分h5,pc,app,小程序,代客下单
     */
    private OrderSource orderSource;

    /**
     * 订单评价状态
     */
    private EvaluateStatus orderEvaluateStatus = EvaluateStatus.NO_EVALUATE;

    /**
     * 店铺服务评价状态
     */
    private EvaluateStatus storeEvaluate = EvaluateStatus.NO_EVALUATE;

    /**
     * 支付方式
     */
    private PayWay payWay;

    /**
     * 可退积分
     */
    private Long canReturnPoints;

    /**
     * 已退金额
     */
    private BigDecimal canReturnPrice;

    /**
     * 订单类型 0：普通订单；1：积分订单；
     */
    private OrderType orderType;

    /**
     * 积分订单类型 0：积分商品 1：积分优惠券
     */
    private PointsOrderType pointsOrderType;

    /**
     * 分享人id
     */
    private String shareUserId;

    /**
     * 是否是秒杀抢购商品订单
     */
    private Boolean isFlashSaleGoods;

    /**
     * 是否拼团订单
     */
    private Boolean grouponFlag = false;

    /**
     * 订单拼团信息
     */
    private TradeGrouponVO tradeGroupon;

    /**
     * 是否周期购订单
     */
    private Boolean cycleBuyFlag = Boolean.FALSE;


    /**
     * 是否是有赞订单
     */
    private Boolean yzOrderFlag = Boolean.FALSE;

    private String deliveryOrderId;

    /**
     * @return
     */
    @JsonIgnore
    public ConcurrentHashMap<String, TradeItemVO> skuItemMap() {
        return new ConcurrentHashMap<>(
                tradeItems.stream().collect(Collectors.toMap(TradeItemVO::getSkuId, Function.identity())));
    }


}
