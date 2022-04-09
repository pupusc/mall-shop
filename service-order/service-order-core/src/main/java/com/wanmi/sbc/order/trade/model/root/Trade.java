package com.wanmi.sbc.order.trade.model.root;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.PointsOrderType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.EvaluateStatus;
import com.wanmi.sbc.order.bean.enums.OrderSource;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import com.wanmi.sbc.order.trade.model.entity.PayInfo;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradePointsCouponItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.Invoice;
import com.wanmi.sbc.order.trade.model.entity.value.MiniProgram;
import com.wanmi.sbc.order.trade.model.entity.value.Seller;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.entity.value.TradeCycleBuyInfo;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.*;
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
public class Trade implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * 订单号
     */
    @Id
    private String id;

    /**
     * 有赞订单id
     */
    private String yzTid;

    /**
     * 父订单号，用于组织批量订单合并支付，目前仅在支付与退款中使用。
     */
    private String parentId;

    /**
     * 订单组号
     */
    private String groupId;

    /**
     * 购买人
     */
    private Buyer buyer;

    /**
     * boss卖方
     */
    private Seller seller;

    /**
     * 商家
     */
    private Supplier supplier;

    /**
     * 买家备注
     */
    private String buyerRemark;

    /**
     * 卖家备注
     */
    private String sellerRemark;

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
    private Invoice invoice;

    /**
     * 订单总体状态
     */
    private TradeState tradeState;

    /**
     * 收货人信息
     */
    private Consignee consignee;

    /**
     * 订单价格
     */
    private TradePrice tradePrice;

    /**
     * 订单商品列表
     */
    private List<TradeItem> tradeItems = new ArrayList<>();

    /**
     * 积分订单优惠券
     */
    private TradePointsCouponItem tradeCouponItem;

    /**
     * 发货单
     */
    private List<TradeDeliver> tradeDelivers = new ArrayList<>();

    /**
     * 配送方式
     */
    private DeliverWay deliverWay;


    private PayInfo payInfo;

    /**
     * 支付单ID
     */
    private String payOrderId;

    /**
     * 尾款支付单ID
     */
    private String tailPayOrderId;


    /**
     * 订单来源方
     */
    private Platform platform;

    /**
     * 订单所属第三方平台类型
     */
    private ThirdPlatformType thirdPlatformType;

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
    private List<TradeEventLog> tradeEventLogs = new ArrayList<>();

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
     * 虚拟商品直冲手机号
     */
    private String directChargeMobile;

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
     * 是否组合套装
     */
    private Boolean suitMarketingFlag;

    /**
     * 开店礼包邀请人id
     */
    private String storeBagsInviteeId;

    /**
     * 分销单品列表
     */
    private List<TradeDistributeItem> distributeItems = new ArrayList<>();

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
    private List<TradeCommission> commissions = new ArrayList<>();

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
    private List<TradeItem> gifts = new ArrayList<>();

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
     * 可退积分 TODO 确定什么时候使用
     */
    private Long canReturnPoints;

    /**
     * 可退知豆 TODO 确定什么时候使用
     */
    @ApiModelProperty(value = "可退知豆")
    private Long canReturnKnowledge;

    /**
     * 可退金额  TODO 确定什么时候使用
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
     * 是否是全部卡券商品(不含赠品)
     */
    private Boolean isVirtualCouponGoods;
    /**
     * 是否是全部卡券商品(赠品)
     */
    private Boolean isVirtualCouponGiveawayGoods;

    /**
     * 是否是预售商品
     */
    private Boolean isBookingSaleGoods;

    /**
     * 预售类型
     */
    private BookingType bookingType;

    /**
     * 尾款订单号
     */
    private String tailOrderNo;

    /**
     * 尾款通知手机号
     */
    private String tailNoticeMobile;

    /**
     * 是否拼团订单
     */
    private Boolean grouponFlag = false;

    /**
     * 订单拼团信息
     */
    private TradeGroupon tradeGroupon;

    /**
     * 是否周期购订单
     */
    private Boolean cycleBuyFlag = Boolean.FALSE;

    /**
     * 是否是有赞订单
     */
    private Boolean yzOrderFlag = Boolean.FALSE;

    /**
     * 周期购信息
     */
    private TradeCycleBuyInfo tradeCycleBuyInfo;


    /**
     * 樊登积分抵扣码
     */
    private String deductCode;

    /**
     * cps推广人用户id
     */
    private String promoteUserId;

    /**
     * cps来源
     */
    private String source;

    private String emallSessionId;

    /**
     * 组合购场景
     */
    private Integer suitScene;

    /**
     * 小程序相关信息，有时间单独建表处理
     */
    private MiniProgram miniProgram;

    /**
     * 增加
     *
     * @param log
     * @return
     */
    public List<TradeEventLog> appendTradeEventLog(TradeEventLog log) {
        tradeEventLogs.add(0, log);
        return tradeEventLogs;
    }

    /**
     * 增加发货单
     *
     * @param tradeDeliver 收款单信息
     */
    public void addTradeDeliver(TradeDeliver tradeDeliver) {
        tradeDelivers.add(0, tradeDeliver);
    }


    /**
     * @return
     */
    @JsonIgnore
    public ConcurrentHashMap<String, TradeItem> skuItemMap() {
        return new ConcurrentHashMap<>(
                tradeItems.stream().collect(Collectors.toMap(TradeItem::getSkuId, Function.identity())));
    }

    /**
     * @return
     */
    @JsonIgnore
    public ConcurrentHashMap<String, TradeItem> giftSkuItemMap() {
        return new ConcurrentHashMap<>(
                gifts.stream().collect(Collectors.toMap(TradeItem::getSkuId, Function.identity())));
    }

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 外部交易编号
     */
    private String outTradeNo;
    /**
     * 外部交易平台：FDDS:樊登读书
     */
    private String outTradePlat;
    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 小程序订单场景：0普通订单；1小程序；2视频号；
     */
    private Integer miniProgramScene;
}
