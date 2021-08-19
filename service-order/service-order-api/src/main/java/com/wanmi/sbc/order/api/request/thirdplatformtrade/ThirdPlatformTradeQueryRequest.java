package com.wanmi.sbc.order.api.request.thirdplatformtrade;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.marketing.bean.vo.TradeGrouponVO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.*;
import com.wanmi.sbc.order.bean.util.XssUtils;
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
 * <p>第三方渠道订单查询参数结构</p>
 * Created by of628-wenzhi on 2017-07-18-下午3:25.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThirdPlatformTradeQueryRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 149142593703964072L;

    /**
     * 订单编号
     */
    private String id;

    /**
     * 父订单s
     */
    private List<String> parentIds;

    /**
     * 父订单
     */
    private String parentId;

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
     * 商品名称-模糊查询
     */
    private String skuName;
    private String skuNo;

    /**
     * 收货人-模糊查询
     */
    private String consigneeName;
    private String consigneePhone;

    /**
     * 订单状态-精确查询
     */
    private TradeStateDTO tradeState;

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
     * 供应商名称-模糊查询
     */
    private String providerName;

    /**
     * 供应商code
     */
    private String providerCode;

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
     *
     * @return
     */
    public Boolean getIsBoss(){
        if(this.isBoss == null){
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
        if(customerOrderListAllType){
            criterias.add(Criteria.where("tradeState.payState").in(PayState.PAID));
            criterias.add(Criteria.where("tradeState.flowState").in(FlowState.AUDIT
                    ,FlowState.DELIVERED
                    ,FlowState.CONFIRMED
                    ,FlowState.COMPLETED
                    ,FlowState.VOID,FlowState.DELIVERED_PART));
        }

        // 订单编号
        if (StringUtils.isNoneBlank(id)) {
            criterias.add(XssUtils.regex("id", id));
        }

        // 父订单编号
        if (StringUtils.isNoneBlank(parentId)) {
            criterias.add(XssUtils.regex("parentId", parentId));
        }

        //批量订单编号
        if (Objects.nonNull(ids) && ids.length > 0) {
            criterias.add(Criteria.where("id").in(Arrays.asList(ids)));
        }

        //批量父订单编号
        if (Objects.nonNull(parentIds) && parentIds.size() > 0) {
            criterias.add(Criteria.where("parentId").in(parentIds));
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
        if(StringUtils.isNotBlank(completionBeginTime)){
            criterias.add(Criteria.where("tradeState.endTime").gte(DateUtil.parseDay(completionBeginTime)));
        }
        //小与传入的结束时间+1天，零点前
        if(StringUtils.isNotBlank(completionEndTime)){
            criterias.add(Criteria.where("tradeState.endTime").lt(DateUtil.parseDay(completionEndTime).plusDays(1)));
        }

        //所属业务员
        if(StringUtils.isNotBlank(employeeId)){
            criterias.add(Criteria.where("buyer.employeeId").is(employeeId));
        }

        if(CollectionUtils.isNotEmpty(employeeIds)){
            criterias.add(Criteria.where("buyer.employeeId").in(employeeIds));
        }

        //商家ID
        if (Objects.nonNull(supplierId)) {
            criterias.add(Criteria.where("supplier.supplierId").is(supplierId));
        }

        //支付单id
        if (Objects.nonNull(payOrderId)) {
            criterias.add(Criteria.where("payOrderId").is(payOrderId));
        }

        if(Objects.nonNull(storeId)){
            criterias.add(Criteria.where("supplier.storeId").is(storeId));
        }

        if(Objects.nonNull(providerName)){
            criterias.add(XssUtils.regex("supplier.supplierName", providerName));
        }

        if(Objects.nonNull(providerName)){
            criterias.add(XssUtils.regex("supplier.supplierCode", providerCode));
        }

        //批量客户
        if(StringUtils.isNotBlank(buyerId)){
            criterias.add(Criteria.where("buyer.id").is(buyerId));
        }

        //批量客户
        if(Objects.nonNull(customerIds) && customerIds.length > 0){
            criterias.add(Criteria.where("buyer.id").in(customerIds));
        }

        // 发货状态
        if(Objects.nonNull(tradeState)){
            // 发货状态
            if (Objects.nonNull(tradeState.getDeliverStatus())) {
                criterias.add(Criteria.where("tradeState.deliverStatus").is(tradeState.getDeliverStatus().getStatusId()));
            }

            // 支付状态
            if (Objects.nonNull(tradeState.getPayState())) {
                criterias.add(Criteria.where("tradeState.payState").is(tradeState.getPayState().getStateId()));
                if(tradeState.getPayState() == PayState.NOT_PAID){
                    criterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
                }
            }

            // 流程状态
            if (Objects.nonNull(tradeState.getFlowState())) {
                criterias.add(Criteria.where("tradeState.flowState").is(tradeState.getFlowState().getStateId()));
            }

            //订单来源
            if(Objects.nonNull(tradeState.getOrderSource())){
                criterias.add(Criteria.where("orderSource").is(tradeState.getOrderSource().toValue()));
            }
        }

        //批量流程状态
        if(CollectionUtils.isNotEmpty(flowStates)){
            criterias.add(Criteria.where("tradeState.flowState").in(flowStates.stream().map(FlowState::getStateId).collect(Collectors.toList())));
        }

        if (StringUtils.isNotBlank(supplierCode)) {
            criterias.add(XssUtils.regex("supplierCode", supplierCode));
        }

        if (StringUtils.isNotBlank(supplierName)) {
            criterias.add(XssUtils.regex("supplierName", supplierName));
        }

        // 客户名称-模糊查询
        if (StringUtils.isNotBlank(buyerName)) {
            criterias.add(XssUtils.regex("buyer.name", buyerName));
        }

        // 客户账号-模糊查询
        if (StringUtils.isNotBlank(buyerAccount)) {
            criterias.add(XssUtils.regex("buyer.account", buyerAccount));
        }

        // 收货人
        if (StringUtils.isNotBlank(consigneeName)) {
            criterias.add(XssUtils.regex("consignee.name", consigneeName));
        }

        // 收货电话
        if (StringUtils.isNotBlank(consigneePhone)) {
            criterias.add(XssUtils.regex("consignee.phone", consigneePhone));
        }

        // skuName模糊查询
        if (StringUtils.isNotBlank(skuName)) {
            Criteria orCriteria = new Criteria();
            orCriteria.orOperator(
                    XssUtils.regex("tradeItems.skuName", skuName),
                    XssUtils.regex("gifts.skuName", skuName));
            criterias.add(orCriteria);
        }

        // skuNo模糊查询
        if (StringUtils.isNotBlank(skuNo)) {
            Criteria orCriteria = new Criteria();
            orCriteria.orOperator(
                    XssUtils.regex("tradeItems.skuNo", skuNo),
                    XssUtils.regex("gifts.skuNo", skuNo));
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
        if(CollectionUtils.isNotEmpty(notFlowStates)){
            criterias.add(Criteria.where("tradeState.flowState").nin(notFlowStates.stream().map(FlowState::getStateId).collect(Collectors.toList())));
        }

        // 订单支付顺序
        if(Objects.nonNull(paymentOrder)){
            criterias.add(Criteria.where("paymentOrder").is(paymentOrder.getStateId()));
        }

        // 订单开始支付时间，开始支付的订单，进行锁定，不能进行其他操作，比如未支付超时作废
        if(Objects.nonNull(startPayTime)){
            criterias.add(new Criteria().orOperator(Criteria.where("tradeState.startPayTime").lt(startPayTime), Criteria.where("tradeState.startPayTime").exists(false)));
        }

        //分销渠道类型和邀请人ID不为空
        if (Objects.nonNull(channelType) && Objects.nonNull(inviteeId) && !"".equals(inviteeId)) {
            Criteria andCriteria = new Criteria();
            andCriteria.andOperator(Criteria.where("channelType").is(channelType.toString()), Criteria.where("distributionShareCustomerId").is(inviteeId));
//            criterias.add(new Criteria().orOperator(andCriteria,Criteria.where("storeBagsFlag").is(DefaultFlag.YES)));
            criterias.add(new Criteria().orOperator(andCriteria,Criteria.where("storeBagsInviteeId").is(inviteeId)));
        } else {
            // 邀请人id
            if(Objects.nonNull(inviteeId) && !"".equals(inviteeId)){
                criterias.add(Criteria.where("inviteeId").is(inviteeId));
            }
        }

        //订单类型
        if(Objects.nonNull(orderType)){
            if (orderType == OrderType.ALL_ORDER){
                criterias.add(Criteria.where("id").exists(true));
            }
            else if(orderType == OrderType.NORMAL_ORDER){
                criterias.add(Criteria.where("id").exists(true).orOperator(Criteria.where("orderType").exists(false),Criteria.where("orderType").is(orderType.getOrderTypeId())));
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

        return criterias;
    }

    /**
     * 公共条件
     * @return
     */
    public Criteria getWhereCriteria(){
        List<Criteria> criteriaList = this.getCommonCriteria();
        if(CollectionUtils.isEmpty(criteriaList)){
            return new Criteria();
        }
        return new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
    }

    /**
     * 设定状态条件逻辑
     * 已审核状态需筛选出已审核与部分发货
     */
    public void makeAllAuditFlow(){
        if(Objects.nonNull(tradeState) && tradeState.getFlowState() ==  FlowState.AUDIT){
            //待发货状态需筛选出未发货与部分发货
            tradeState.setFlowState(null);
            flowStates = Arrays.asList(FlowState.AUDIT, FlowState.DELIVERED_PART);
        }
    }

    /**
     * 可退订单的条件
     * @return
     */
    public Criteria getCanReturnCriteria(){
        /**
         * 允许退单
         */
        if (Objects.nonNull(status) && status == 0) {
            if(tradeState == null){
                tradeState = new TradeStateDTO();
            }
            tradeState.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
        }

        List<Criteria> criteria = this.getCommonCriteria();

        Criteria dayCriteria = new Criteria();
        // 开店礼包不支持退单
        criteria.add(Criteria.where("storeBagsFlag").ne(DefaultFlag.YES));

        //已完成订单允许申请退单时间
        if (Objects.nonNull(day) && day > 0) {
            dayCriteria.andOperator(
                Criteria.where("tradeState.flowState").is(FlowState.COMPLETED.getStateId()),
                Criteria.where("tradeState.endTime").gte(LocalDateTime.now().minusDays(day))
            );
        }else {
            dayCriteria.andOperator(Criteria.where("tradeState.flowState").is(FlowState.COMPLETED.getStateId()));
        }

        criteria.add(
            new Criteria().orOperator(
                dayCriteria,
                new Criteria().andOperator(
                        Criteria.where("tradeState.deliverStatus").is(DeliverStatus.NOT_YET_SHIPPED.getStatusId()),
                        Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()),
                        Criteria.where("tradeState.auditState").is(AuditState.CHECKED.getStatusId()),
                        Criteria.where("tradeState.flowState").nin(
                                FlowState.VOID.getStateId(), FlowState.INIT.getStateId(), FlowState.GROUPON.getStateId())
                )
            )
        );
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
