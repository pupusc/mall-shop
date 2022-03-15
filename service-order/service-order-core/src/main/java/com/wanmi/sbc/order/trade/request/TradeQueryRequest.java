package com.wanmi.sbc.order.trade.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.marketing.bean.vo.TradeGrouponVO;
import com.wanmi.sbc.order.bean.enums.*;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.util.XssUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>订单查询参数结构</p>
 * Created by of628-wenzhi on 2017-07-18-下午3:25.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeQueryRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 149142593703964072L;

    /**
     * 主订单编号
     */
    private String id;

    /**
     * 父订单编号
     */
    private String parentId;

    /**
     * 子订单编号
     */
    private String sonOrderIdAccount;

    /**
     * 客户名称-模糊查询
     */
    private String buyerName;

    /**
     * 客户名称
     */
    private String buyerId;

    /**
     * 客户账号-模糊查询
     */
    private String buyerAccount;
    /**
     * 电子卡券id
     */
    private Long virtualCouponId;
    /**
     * 商品名称-模糊查询
     */
    private String skuName;
    private String skuNo;

    /**
     * 供应商-模糊查询
     */
    private String providerName;

    private String providerCode;

    /**
     * 收货人-模糊查询
     */
    private String consigneeName;
    private String consigneePhone;

    /**
     * 订单状态-精确查询
     */
    private TradeState tradeState;

    /**
     * 用于根据ids批量查询场景
     */
    private String[] ids;

    /**
     * 退单创建开始时间，精确到天
     */
    private String beginTime;

    /**
     * 退单创建结束时间，精确到天
     */
    private String endTime;

    /**
     * 客户端条件搜索时使用，订单编号或商品名称
     */
    private String keyworks;

    /**
     * 商家id-精确查询
     */
    private Long supplierId;

    /**
     * 商家编码-模糊查询
     */
    private String supplierCode;

    /**
     * 商家名称-模糊查询
     */
    private String supplierName;

    /**
     * 店铺Id
     */
    private Long storeId;
    /**
     * 已完成订单允许申请退单时间
     */
    private Integer day;

    /**
     * 是否允许退单
     */
    private Integer status;

    /**
     * 业务员id
     */
    private String employeeId;

    /**
     * 业务员id集合
     */
    private List<String> employeeIds;

    /**
     * 客户id
     */
    private Object[] customerIds;

    /**
     * 是否boss或商家端
     */
//    @Builder.Default
    private Boolean isBoss;

    /**
     * 批量流程状态
     */
    private List<FlowState> flowStates;

    /**
     * 批量非流程状态
     */
    private List<FlowState> notFlowStates;

    /**
     * 订单支付顺序
     */
    private PaymentOrder paymentOrder;

    /**
     * 开始支付时间
     */
    private LocalDateTime startPayTime;

    /**
     * 邀请人id
     */
    private String inviteeId;

    /**
     * 分销渠道类型
     */
    private ChannelType channelType;

    /**
     *  商品类型
     */
    private GoodsType goodsType;


    /**
     * 小b端我的客户列表是否是查询全部
     */
    @ApiModelProperty(value = "小b端我的客户列表是否是查询全部")
    private boolean customerOrderListAllType;


    /**
     * 是否拼团订单
     */
    @ApiModelProperty(value = "是否拼团订单")
    private Boolean grouponFlag;

    /**
     * 是否秒杀订单
     */
    @ApiModelProperty(value = "是否秒杀订单")
    private Boolean flashSaleFlag;

    /**
     * 是否预售订单
     */
    @ApiModelProperty(value = "是否预售订单")
    private Boolean bookingSaleFlag;

    /**
     * 是否周期购订单
     */
    private Boolean cycleBuyFlag;


    /**
     * 订单拼团信息
     */
    private TradeGrouponVO tradeGroupon;

    /**
     * 订单类型 0：普通订单；1：积分订单
     */
    @ApiModelProperty(value = "订单类型")
    private OrderType orderType;

    /**
     * 订单完成开始时间
     */
    private String completionBeginTime;

    /**
     * 订单完成结束时间
     */
    private String completionEndTime;

    /**
     * 支付单ID
     */
    private String payOrderId;


    /**
     * 筛选订单类型
     */
    @ApiModelProperty(value = "筛选订单类型")
    private QueryOrderType queryOrderType;

    /**
     * 筛选支付方式
     */
    private QueryPayType queryPayType;

    /**
     * 是否是预售商品
     */
    private Boolean isBookingSaleGoods;

    /**
     * 预售类型
     */
    private BookingType bookingType;

    /**
     * 筛选第三方平台订单
     */
    private ThirdPlatformType thirdPlatformType;

    /**
     * 批量多个第三方平台订单
     */
    private List<ThirdPlatformType> thirdPlatformTypes;

    /**
     * 筛选第三方平台订单
     */
    @ApiModelProperty(value = "渠道订单待处理")
    private Boolean thirdPlatformToDo;

    /**
     * 预售尾款到达时间
     */
    @ApiModelProperty(value = "预售尾款到达时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime bookingTailTime;

    /**
     * 尾款订单号
     */
    private String tailOrderNo;

    /**
     * 退单标识 false:没有退单  true:有退单
     */
    private Boolean returnHasFlag;

    /**
     * 物流单号
     */
    private List<String> logisticNos;

    /**
     * 有赞订单号
     */
    private List<String> yzTids;

    @ApiModelProperty("虚拟全部发货，0否1是")
    private Integer virtualAllDelivery;
    /**
     * @return
     */
    public Boolean getIsBoss() {
        if (this.isBoss == null) {
            return true;
        }
        return this.isBoss;
    }

    /**
     * 封装公共条件
     *
     * @return
     */
    private List<Criteria> getCommonCriteria() {
        List<Criteria> criterias = new ArrayList<>();

        //判断--小b端我的客户列表是否是查询全部
        if (customerOrderListAllType) {
            criterias.add(Criteria.where("tradeState.payState").in(PayState.PAID));
            criterias.add(Criteria.where("tradeState.flowState").in(FlowState.AUDIT
                    , FlowState.DELIVERED
                    , FlowState.CONFIRMED
                    , FlowState.COMPLETED
                    , FlowState.VOID, FlowState.DELIVERED_PART));
        }

        // 订单编号
        if (StringUtils.isNoneBlank(id)) {
            criterias.add(Criteria.where("id").is(id));
        }

        //批量订单编号
        if (Objects.nonNull(ids) && ids.length > 0) {
            criterias.add(Criteria.where("id").in(Arrays.asList(ids)));
        }

        // 父订单编号
        if (StringUtils.isNoneBlank(parentId)) {
            criterias.add(Criteria.where("parentId").is(parentId));
        }


        //时间范围，大于开始时间
        if (StringUtils.isNotBlank(beginTime)) {
            criterias.add(Criteria.where("tradeState.createTime").gte(DateUtil.parseDay(beginTime)));
        }

        //小与传入的结束时间+1天，零点前
        if (StringUtils.isNotBlank(endTime)) {
            criterias.add(Criteria.where("tradeState.createTime").lt(DateUtil.parseDay(endTime).plusDays(1)));
        }

        //订单完成日期，大于/等于开始时间
        if (StringUtils.isNotBlank(completionBeginTime)) {
            criterias.add(Criteria.where("tradeState.endTime").gte(DateUtil.parseDay(completionBeginTime)));
        }
        //小与传入的结束时间+1天，零点前
        if (StringUtils.isNotBlank(completionEndTime)) {
            criterias.add(Criteria.where("tradeState.endTime").lt(DateUtil.parseDay(completionEndTime).plusDays(1)));
        }

        if (Objects.nonNull(bookingTailTime)) {
            criterias.add(new Criteria().andOperator(Criteria.where("tradeState.tailStartTime").lt(bookingTailTime), Criteria.where("tradeState.tailEndTime").gt(bookingTailTime)));
        }

        //所属业务员
        if (StringUtils.isNotBlank(employeeId)) {
            criterias.add(Criteria.where("buyer.employeeId").is(employeeId));
        }

        if (CollectionUtils.isNotEmpty(employeeIds)) {
            criterias.add(Criteria.where("buyer.employeeId").in(employeeIds));
        }

        //商家ID
        if (Objects.nonNull(supplierId)) {
            criterias.add(Criteria.where("supplier.supplierId").is(supplierId));
        }
        //电子卡券id
        if (Objects.nonNull(virtualCouponId)) {
            if(virtualCouponId== DefaultFlag.NO.toValue()){
                criterias.add(Criteria.where("tradeItems.virtualCouponId").exists(true));
                criterias.add(Criteria.where("tradeItems.virtualCouponId").ne(null));
            }else {
                criterias.add(Criteria.where("tradeItems.virtualCouponId").is(virtualCouponId));
            }
        }
        /**
         * 包含商品类型
         */
        if(Objects.nonNull(goodsType)){
            criterias.add(Criteria.where("tradeItems.goodsType").is(goodsType));
        }
        if (Objects.nonNull(storeId)) {
            criterias.add(Criteria.where("supplier.storeId").is(storeId));
        }

        //批量客户
        if (StringUtils.isNotBlank(buyerId)) {
            criterias.add(Criteria.where("buyer.id").is(buyerId));
        }

        //批量客户
        if (Objects.nonNull(customerIds) && customerIds.length > 0) {
            criterias.add(Criteria.where("buyer.id").in(customerIds));
        }

        // 发货状态
        if (Objects.nonNull(tradeState)) {
            // 发货状态
            if (Objects.nonNull(tradeState.getDeliverStatus())) {
                criterias.add(Criteria.where("tradeState.deliverStatus").is(tradeState.getDeliverStatus().getStatusId()));
            }

            // 支付状态
            if (Objects.nonNull(tradeState.getPayState())) {

                if (tradeState.getPayState() == PayState.NOT_PAID) {
                    Criteria payCriterias = new Criteria();
                    // 非尾款支付订单
                    Criteria comCriterias = new Criteria();
                    Criteria comPayCriterias = new Criteria();
                    comPayCriterias.orOperator(Criteria.where("tradeState.payState").is(tradeState.getPayState().getStateId()),
                            Criteria.where("tradeState.payState").is(PayState.UNCONFIRMED.getStateId()));
                    Criteria comFlowCriterias = Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId());
                    comCriterias.andOperator(comPayCriterias, comFlowCriterias);

                    // 待支付定金
                    Criteria   waitPayEarnest = new Criteria();
                    waitPayEarnest.and("tradeState.flowState").is(FlowState.WAIT_PAY_EARNEST.getStateId());
                    waitPayEarnest.and("tradeState.payState").is(PayState.NOT_PAID);
                    // 待支付尾款
                    Criteria   waitPayTailWait = new Criteria();
                    waitPayTailWait.and("tradeState.flowState").is(FlowState.WAIT_PAY_TAIL.getStateId());
                    waitPayTailWait.and("tradeState.payState").is(PayState.PAID_EARNEST);
                    Criteria   waitPayTail = new Criteria();
                    waitPayTail.and("tradeState.flowState").is(FlowState.AUDIT.getStateId());
                    waitPayTail.and("tradeState.payState").is(PayState.PAID_EARNEST);
                    Criteria   waitPayTailWaitor = new Criteria();
                    // 待支付尾款订单
                    Criteria bookingCriterias = new Criteria();
                    bookingCriterias.orOperator(waitPayEarnest, waitPayTailWaitor.orOperator(waitPayTailWait,waitPayTail));
                    payCriterias.orOperator(
                            comCriterias,
                            bookingCriterias);
                    criterias.add(payCriterias);
                }else{
                    // 非待支付
                    Criteria comCriterias =Criteria.where("tradeState.payState").is(tradeState.getPayState().getStateId());
                    criterias.add(comCriterias);
                }
            }

            // 流程状态
            if (Objects.nonNull(tradeState.getFlowState())) {
                // 待支付定金
                if (FlowState.WAIT_PAY_EARNEST == tradeState.getFlowState()){
                    Criteria waitPayEarnest = new Criteria();
                    waitPayEarnest.and("tradeState.flowState").is(FlowState.WAIT_PAY_EARNEST.getStateId());
                    waitPayEarnest.and("tradeState.payState").is(PayState.NOT_PAID);
                    criterias.add(waitPayEarnest);
                }else if(FlowState.WAIT_PAY_TAIL == tradeState.getFlowState()){
                    // 待支付尾款
                    Criteria waitPayTailWait = new Criteria();
                    waitPayTailWait.and("tradeState.flowState").is(FlowState.WAIT_PAY_TAIL.getStateId());
                    waitPayTailWait.and("tradeState.payState").is(PayState.PAID_EARNEST);
                    Criteria waitPayTail = new Criteria();
                    waitPayTail.and("tradeState.flowState").is(FlowState.AUDIT.getStateId());
                    waitPayTail.and("tradeState.payState").is(PayState.PAID_EARNEST);
                    criterias.add(new Criteria().orOperator(waitPayTailWait, waitPayTail));
                } else {
                    criterias.add(Criteria.where("tradeState.flowState").is(tradeState.getFlowState().getStateId()));
                }
            }

            //订单来源
            if (Objects.nonNull(tradeState.getOrderSource())) {
                criterias.add(Criteria.where("orderSource").is(tradeState.getOrderSource().toValue()));
            }
        }

        //支付单id
        if (Objects.nonNull(payOrderId)) {
            criterias.add(Criteria.where("payOrderId").is(payOrderId));
        }
        if (Objects.nonNull(isBookingSaleGoods)) {
            criterias.add(Criteria.where("isBookingSaleGoods").is(isBookingSaleGoods));
        }

        if (Objects.nonNull(bookingType)) {
            criterias.add(Criteria.where("bookingType").is(bookingType));
        }

        //批量流程状态
        if (CollectionUtils.isNotEmpty(flowStates)) {
            criterias.add(Criteria.where("tradeState.flowState").in(flowStates.stream().map(FlowState::getStateId).collect(Collectors.toList())));
        }

        if (StringUtils.isNotBlank(supplierCode)) {
            criterias.add(Criteria.where("supplier.supplierCode").is(supplierCode));
        }

        if (StringUtils.isNotBlank(supplierName)) {
            criterias.add(Criteria.where("supplier.supplierName").is(supplierName));
        }

        //供应商名称 模糊查询
        if(StringUtils.isNoneBlank(providerName)){
            criterias.add(Criteria.where("tradeItems.providerName").is(providerName));
        }
        //供应商编号
        if (StringUtils.isNoneBlank(providerCode)) {
            criterias.add(Criteria.where("tradeItems.providerCode").is(providerCode));
        }

        // 客户名称-模糊查询
        if (StringUtils.isNotBlank(buyerName)) {
            criterias.add(Criteria.where("buyer.name").is(buyerName));
        }

        // 客户账号-模糊查询
        if (StringUtils.isNotBlank(buyerAccount)) {
            criterias.add(Criteria.where("buyer.account").is(buyerAccount));
        }

        // 收货人
        if (StringUtils.isNotBlank(consigneeName)) {
            criterias.add(Criteria.where("consignee.name").is(consigneeName));
        }

        // 收货电话
        if (StringUtils.isNotBlank(consigneePhone)) {
            criterias.add(Criteria.where("consignee.phone").is(consigneePhone));
        }

        // 尾款订单号
        if (StringUtils.isNotBlank(tailOrderNo)) {
            criterias.add(Criteria.where("tailOrderNo").is(tailOrderNo));
        }

        // skuName模糊查询
        if (StringUtils.isNotBlank(skuName)) {
            Criteria orCriteria = new Criteria();
            orCriteria.orOperator(
                    Criteria.where("tradeItems.skuName").is(skuName),
                    Criteria.where("gifts.skuName").is(skuName));
            criterias.add(orCriteria);
        }

        // skuNo模糊查询
        if (StringUtils.isNotBlank(skuNo)) {
            Criteria orCriteria = new Criteria();
            orCriteria.orOperator(
                    Criteria.where("tradeItems.skuNo").is(skuNo),
                    Criteria.where("gifts.skuNo").is(skuNo));
            criterias.add(orCriteria);
        }

        //关键字
        if (StringUtils.isNotBlank(keyworks)) {
            Criteria orCriteria = new Criteria();
            orCriteria.orOperator(
                    XssUtils.regex("id", keyworks),
                    XssUtils.regex("tradeItems.skuName", keyworks),
                    XssUtils.regex("gifts.skuName", keyworks));
            criterias.add(orCriteria);
        }

        //批量流程状态
        if (CollectionUtils.isNotEmpty(notFlowStates)) {
            criterias.add(Criteria.where("tradeState.flowState").nin(notFlowStates.stream().map(FlowState::getStateId).collect(Collectors.toList())));
        }

        //是否有错误
        if(Boolean.TRUE.equals(thirdPlatformToDo)){
            criterias.add(Criteria.where("thirdPlatformPayErrorFlag").is(thirdPlatformToDo));
        }

        // 第三方平台订单
        if(Objects.nonNull(thirdPlatformType)) {
            criterias.add(Criteria.where("thirdPlatformType").is(thirdPlatformType));
        }

        // 批量 - 第三方平台订单
        if(CollectionUtils.isNotEmpty(thirdPlatformTypes)) {
            criterias.add(Criteria.where("thirdPlatformType").in(thirdPlatformTypes));
        }

        // 订单支付顺序
        if (Objects.nonNull(paymentOrder)) {
            criterias.add(Criteria.where("paymentOrder").is(paymentOrder.getStateId()));
        }

        // 订单开始支付时间，开始支付的订单，进行锁定，不能进行其他操作，比如未支付超时作废
        if (Objects.nonNull(startPayTime)) {
            criterias.add(new Criteria().orOperator(Criteria.where("tradeState.startPayTime").lt(startPayTime), Criteria.where("tradeState.startPayTime").exists(false)));
        }

        //分销渠道类型和邀请人ID不为空
        if (Objects.nonNull(channelType) && StringUtils.isNotEmpty(inviteeId)) {
            Criteria andCriteria = new Criteria();
            andCriteria.andOperator(Criteria.where("channelType").is(channelType.toString()), Criteria.where("distributionShareCustomerId").is(inviteeId));
//            criterias.add(new Criteria().orOperator(andCriteria,Criteria.where("storeBagsFlag").is(DefaultFlag.YES)));
            criterias.add(new Criteria().orOperator(andCriteria, Criteria.where("storeBagsInviteeId").is(inviteeId)));
        } else {
            // 邀请人id
            if (StringUtils.isNotEmpty(inviteeId)) {
                criterias.add(Criteria.where("inviteeId").is(inviteeId));
            }
        }

        //订单类型
        if (Objects.nonNull(orderType)) {
            if (orderType == OrderType.ALL_ORDER) {
                criterias.add(Criteria.where("id").exists(true));
            } else if (orderType == OrderType.NORMAL_ORDER) {
                criterias.add(Criteria.where("id").exists(true).orOperator(Criteria.where("orderType").exists(false), Criteria.where("orderType").is(orderType.getOrderTypeId())));
                //查询包含积分价商品的订单
                if(Objects.nonNull(queryOrderType) && queryOrderType == QueryOrderType.BUY_POINTS_ORDER){
                    criterias.add(Criteria.where("tradeItems.buyPoint").gt(0));
                }
            } else {
                criterias.add(Criteria.where("orderType").is(orderType));
            }
        } else {
            criterias.add(Criteria.where("id").exists(true).orOperator(Criteria.where("orderType").exists(false),Criteria.where("orderType").is(OrderType.NORMAL_ORDER)));
        }

        // 是否拼团订单
        if (Objects.nonNull(grouponFlag)) {
            criterias.add(Criteria.where("grouponFlag").is(grouponFlag));
        }

        // 是否秒杀订单
        if (Objects.nonNull(flashSaleFlag)) {
            criterias.add(Criteria.where("isFlashSaleGoods").is(flashSaleFlag));
        }

        // 是否预售订单
        if (Objects.nonNull(bookingSaleFlag)) {
            criterias.add(Criteria.where("isBookingSaleGoods").is(bookingSaleFlag));
        }

        // 是否周期购
        if (Objects.nonNull(cycleBuyFlag)) {
            criterias.add(Criteria.where("cycleBuyFlag").is(cycleBuyFlag));
        }

        if (Objects.nonNull(queryPayType)) {
            switch (queryPayType) {
                case OFFLINE:
                    criterias.add(Criteria.where("payInfo.payTypeId").is(String.valueOf(PayType.OFFLINE.toValue())));
                    break;
                case ONLINE:
                    criterias.add(
                            Criteria.where("payInfo.payTypeId").is(String.valueOf(PayType.ONLINE.toValue()))
                            .orOperator(Criteria.where("payWay").exists(false), Criteria.where("payWay").ne(PayWay.BALANCE)));
                    break;
                case BALANCE:
                    criterias.add(Criteria.where("payWay").is(PayWay.BALANCE));
                    break;
                case POINT:
                    //纯积分支付
                    criterias.add(Criteria.where("tradePrice.points").gt(0)
                            .orOperator(Criteria.where("tradePrice.totalPrice").exists(false), Criteria.where("tradePrice.totalPrice").lte("0.00")));
                    break;
                case POINT_ONLINE:
                    //线上支付，非余额支付
                    criterias.add(Criteria.where("payInfo.payTypeId").is(String.valueOf(PayType.ONLINE.toValue()))
                                    .orOperator(Criteria.where("payWay").exists(false), Criteria.where("payWay").ne(PayWay.BALANCE)));
                    criterias.add(Criteria.where("tradePrice.points").gt(0));
                    criterias.add(Criteria.where("tradePrice.totalPrice").gt("0.00"));
                    break;
                case POINT_BALANCE:
                    criterias.add(Criteria.where("payWay").is(PayWay.BALANCE));
                    criterias.add(Criteria.where("tradePrice.points").gt(0));
                    criterias.add(Criteria.where("tradePrice.totalPrice").gt("0.00"));
                    break;
                case KNOWLEDGE:
                    //纯积分支付
                    criterias.add(Criteria.where("tradePrice.knowledge").gt(0)
                            .orOperator(Criteria.where("tradePrice.totalPrice").exists(false), Criteria.where("tradePrice.totalPrice").lte("0.00")));
                    break;
                case KNOWLEDGE_ONLINE:
                    //线上支付，非余额支付
                    criterias.add(Criteria.where("payInfo.payTypeId").is(String.valueOf(PayType.ONLINE.toValue()))
                            .orOperator(Criteria.where("payWay").exists(false), Criteria.where("payWay").ne(PayWay.BALANCE)));
                    criterias.add(Criteria.where("tradePrice.knowledge").gt(0));
                    criterias.add(Criteria.where("tradePrice.totalPrice").gt("0.00"));
                    break;
                case KNOWLEDGE_BALANCE:
                    criterias.add(Criteria.where("payWay").is(PayWay.BALANCE));
                    criterias.add(Criteria.where("tradePrice.knowledge").gt(0));
                    criterias.add(Criteria.where("tradePrice.totalPrice").gt("0.00"));
                    break;
            }
        }

        if (Objects.nonNull(tradeGroupon)) {
            // 是否团长订单
            if (Objects.nonNull(tradeGroupon.getLeader())) {
                criterias.add(Criteria.where("tradeGroupon.leader").is(tradeGroupon.getLeader()));
            }

            // 团订单状态
            if (Objects.nonNull(tradeGroupon.getGrouponOrderStatus())) {
                criterias.add(Criteria.where("tradeGroupon.grouponOrderStatus").is(tradeGroupon.getGrouponOrderStatus
                        ().toString()));
            }

            // 团编号
            if (Objects.nonNull(tradeGroupon.getGrouponNo())) {
                criterias.add(Criteria.where("tradeGroupon.grouponNo").is(tradeGroupon.getGrouponNo()));
            }

            // 团活动id
            if (Objects.nonNull(tradeGroupon.getGrouponActivityId())) {
                criterias.add(Criteria.where("tradeGroupon.grouponActivityId").is(tradeGroupon.getGrouponActivityId()));
            }

            // 团商品
            if (Objects.nonNull(tradeGroupon.getGoodInfoId())) {
                criterias.add(Criteria.where("tradeGroupon.goodInfoId").is(tradeGroupon.getGoodInfoId()));
            }
        }

        //物流号查询
        if(CollectionUtils.isNotEmpty(logisticNos)){
            criterias.add(Criteria.where("tradeDelivers.logistics.logisticNo").in(logisticNos));
        }

        //退单标记
        if(Objects.nonNull(returnHasFlag)){
            //有退单 > 0
            if(returnHasFlag) {
                criterias.add(Criteria.where("returnOrderNum").gt(0));
            }else{//有退单 <= 0
                criterias.add(Criteria.where("returnOrderNum").lte(0));
            }
        }

        if(CollectionUtils.isNotEmpty(yzTids)) {
            criterias.add(Criteria.where("yzTid").in(yzTids));
        }
        if(Objects.nonNull(virtualAllDelivery) && virtualAllDelivery == 1){
            criterias.add(Criteria.where("tradeState.virtualAllDelivery").is(virtualAllDelivery));
        }else if(Objects.nonNull(virtualAllDelivery)){
            criterias.add(Criteria.where("tradeState.virtualAllDelivery").is(null));
        }
        return criterias;
    }

    /**
     * 公共条件
     *
     * @return
     */
    public Criteria getWhereCriteria() {
        List<Criteria> criteriaList = this.getCommonCriteria();
        if (CollectionUtils.isEmpty(criteriaList)) {
            return new Criteria();
        }
        return new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
    }

    /**
     * 设定状态条件逻辑
     * 已审核状态需筛选出已审核与部分发货
     */
    public void makeAllAuditFlow() {
        if (Objects.nonNull(tradeState) && tradeState.getFlowState() == FlowState.AUDIT) {
            //待发货状态需筛选出未发货与部分发货
            tradeState.setFlowState(null);
            flowStates = Arrays.asList(FlowState.AUDIT, FlowState.DELIVERED_PART);
        }
    }

    //待客退单是否包含周期购订单中的部分发货
    private  Boolean periodicPurchaseRefund=Boolean.FALSE;

    /**
     * 可退订单的条件
     *
     * @return
     */
    public Criteria getCanReturnCriteria() {
        /**
         * 允许退单
         */
        if (Objects.nonNull(status) && status == 0) {
            if (tradeState == null) {
                tradeState = new TradeState();
            }
            tradeState.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
        }

        List<Criteria> criteria = this.getCommonCriteria();
        //过滤掉2021年之前的单子
        criteria.add(Criteria.where("tradeState.createTime").gte(LocalDateTime.of(2021,1,1,0,0,0)));
        Criteria dayCriteria = new Criteria();
        // 开店礼包不支持退单
        criteria.add(Criteria.where("storeBagsFlag").ne(DefaultFlag.YES));

//        //已完成订单允许申请退单时间
//        if (Objects.nonNull(day) && day > 0) {
//            dayCriteria.andOperator(
//                    Criteria.where("tradeState.flowState").is(FlowState.COMPLETED.getStateId()),
//                    Criteria.where("tradeState.endTime").gte(LocalDateTime.now().minusDays(day))
//            );
//        } else {
//            dayCriteria.andOperator(Criteria.where("tradeState.flowState").is(FlowState.COMPLETED.getStateId()));
//        }



        //部分发货仍然可以发起售后
        Criteria criteria2=  new Criteria().andOperator(Criteria.where("tradeState.deliverStatus").is(DeliverStatus.PART_SHIPPED.getStatusId()));

        //其他订单未发货
        Criteria criteria3=  new Criteria().andOperator(Criteria.where("tradeState.deliverStatus").is(DeliverStatus.NOT_YET_SHIPPED.getStatusId()));

        Criteria criteria4=  new Criteria().orOperator(criteria2,criteria3);

        Criteria criteria1= new Criteria().andOperator(
                criteria4,
                Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()),
                Criteria.where("tradeState.auditState").is(AuditState.CHECKED.getStatusId()),
                Criteria.where("tradeState.flowState").nin(
                        FlowState.VOID.getStateId(), FlowState.INIT.getStateId(), FlowState.GROUPON.getStateId()));

        criteria.add(new Criteria().orOperator(dayCriteria,criteria1));

        criteria.add(Criteria.where("yzOrderFlag").is(Boolean.FALSE));

        return new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
    }


    @Override
    public String getSortColumn() {
        return "tradeState.createTime";
    }

    @Override
    public String getSortRole() {
        return "desc";
    }

    @Override
    public String getSortType() {
        return "Date";
    }
}
