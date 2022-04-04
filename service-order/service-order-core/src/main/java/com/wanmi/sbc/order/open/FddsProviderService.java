package com.wanmi.sbc.order.open;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ShipperType;
import com.wanmi.sbc.order.open.model.UserOrderCreateParam;
import com.wanmi.sbc.order.open.model.UserOrderCreateResult;
import com.wanmi.sbc.order.open.model.UserOrderCreateResultContent;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.ProviderTradeRepository;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-04-02 00:40:00
 */
@Slf4j
@Service
public class FddsProviderService {
    /**
     * 开放平台商品下单地址
     */
    private String orderCreateUrl = "/user/order/createV2";
    @Autowired
    private FddsOpenPlatformService fddsOpenPlatformService;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private ProviderTradeRepository providerTradeRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 1.开放平台下单
     * 2.下单成功：更新发货状态
     * 3.下单失败：通知运营退款
     * 4.下单超时：补偿机制
     *      补偿机制->成功->更新发货状态
     *      补偿机制->失败->通知运营退款
     */
    public BaseResponse createFddsTrade(ProviderTrade providerTrade) {
        //供应商下单
        UserOrderCreateResult result = createOutOrder(providerTrade);

        if (result.isRepeatOrder()) {
            log.warn("重复下单，本次提交不做处理");
            return BaseResponse.FAILED();
        }

        if (!result.isSuccess()) {
            return createOutOrderFail(providerTrade, result);
        }

        return createOutOrderSuccess(providerTrade, result.getData());
    }

    /**
     * 下单成功
     * 1.更新订单号
     * 2.更新发货信息
     */
    private BaseResponse createOutOrderSuccess(ProviderTrade providerTrade, UserOrderCreateResultContent createResult) {
        //验证重复下单
        if (DeliverStatus.SHIPPED.equals(providerTrade.getTradeState().getDeliverStatus()) && StringUtils.isNotBlank(providerTrade.getDeliveryOrderId())) {
            log.warn("该订单已经完成发货，本次提交不做处理，tradeId = {}, providerTradeId = {}, deliveryOrderId = {}",
                    providerTrade.getParentId(), providerTrade.getId(), providerTrade.getDeliveryOrderId());
            return BaseResponse.FAILED();
        }

        //查询主单
        Trade trade = tradeRepository.findById(providerTrade.getParentId()).orElse(null);
        if (Objects.isNull(trade)) {
            log.error("根据主单id查询主单结果不存在，id = {}", providerTrade.getParentId());
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }

        //根据主单号查询所有发货单并以此判断主单是部分发货还是全部发货
        List<ProviderTrade> providerTrades = providerTradeRepository.findListByParentId(providerTrade.getParentId());
        if (CollectionUtils.isEmpty(providerTrades)) {
            log.error("根据主单id查询子单结果不存在，parentId = {}", providerTrade.getParentId());
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }

        //当前时间
        LocalDateTime nowTime = LocalDateTime.now();

        //生成发货单信息tradeDelivers（子单暂无、主单暂无）
        List<TradeDeliver> tradeDeliverVOList = new ArrayList<>();
        TradeDeliver tradeDeliverVO = new TradeDeliver();
        tradeDeliverVO.setTradeId(providerTrade.getId());
        tradeDeliverVO.setDeliverId(createResult.getOrderNumber().toString());
        tradeDeliverVO.setDeliverTime(nowTime);
        tradeDeliverVO.setShippingItems(Lists.newArrayList()); //发货清单设空
        tradeDeliverVO.setProviderName(providerTrade.getSupplier().getSupplierName());
        tradeDeliverVO.setShipperType(ShipperType.PROVIDER);
        tradeDeliverVO.setStatus(DeliverStatus.SHIPPED);
        tradeDeliverVOList.add(tradeDeliverVO);

        //更新订单号
        providerTrade.setDeliveryOrderId(createResult.getOrderNumber().toString());

        //更新发货状态：
        //子单全部发货
        //1.子单商品发货信息 tradeItems.deliveredNum、tradeItems.deliverStatus
        //2.子单订单发货信息 tradeState.deliverStatus、tradeState.deliverTime、tradeState.flowState
        providerTrade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
        providerTrade.getTradeState().setFlowState(FlowState.DELIVERED);
        providerTrade.getTradeState().setDeliverTime(nowTime);
        providerTrade.getTradeDelivers().addAll(tradeDeliverVOList);

        for (TradeItem tradeItem : providerTrade.getTradeItems()) {
            tradeItem.setDeliveredNum(tradeItem.getNum());
            tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
        }

        //主单发货状态，默认全部发货
        //3.主单商品发货信息 tradeItems.deliveredNum、tradeItems.deliverStatus
        //4.主单订单发货信息 tradeState.deliverStatus、tradeState.deliverTime、tradeState.flowState
        trade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
        trade.getTradeState().setFlowState(FlowState.DELIVERED);
        trade.getTradeState().setDeliverTime(nowTime);
        trade.getTradeDelivers().addAll(tradeDeliverVOList);
        //此发货单全部发货，判断其他发货单存在部分发货，则主订单是部分发货
        if (providerTrades.stream().anyMatch(p -> !providerTrade.getId().equals(p.getId())
                && !DeliverStatus.SHIPPED.equals(providerTrade.getTradeState().getDeliverStatus())
                && !FlowState.VOID.equals(p.getTradeState().getFlowState()))) {
            trade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
            trade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
        }

        //更新子单信息
        providerTradeRepository.save(providerTrade);

        List<String> itemOids = providerTrade.getTradeItems().stream().map(TradeItem::getOid).collect(Collectors.toList());
        List<TradeItem> tradeItems = trade.getTradeItems().stream().filter(item -> !itemOids.contains(item.getOid())).collect(Collectors.toList());
        tradeItems.addAll(providerTrade.getTradeItems());
        trade.setTradeItems(tradeItems);

        //如果全部发货，则更新发货数量
        if (DeliverStatus.SHIPPED.equals(trade.getTradeState().getDeliverStatus())
                && FlowState.DELIVERED.equals(trade.getTradeState().getFlowState())) {
            for (TradeItem tradeItem : trade.getTradeItems()) {
                tradeItem.setDeliveredNumHis(tradeItem.getDeliveredNum());
                if (tradeItem.getNum() > tradeItem.getDeliveredNum()) {
                    tradeItem.setDeliveredNum(tradeItem.getNum());
                }
            }
            for (TradeItem giftsTradeItem : trade.getGifts()) {
                giftsTradeItem.setDeliveredNumHis(giftsTradeItem.getDeliveredNum());
                if (giftsTradeItem.getNum() > giftsTradeItem.getDeliveredNum()) {
                    giftsTradeItem.setDeliveredNum(giftsTradeItem.getNum());
                }
            }
        }
        //更新主单信息
        tradeRepository.save(trade);
        return BaseResponse.success("开放平台下单成功");
    }

    /**
     * 下单失败：
     * 1.更新订单备注
     * 2.消息通知退款
     *
     * 返回状态码：
     * 2001	会期商品不存在
     * 2006	已领取不可重复领取
     * 2007	商品已下架
     * 2008	书籍包已下线或未发布
     * 2009	书籍包重复购买
     * 2010	结算方式未配置
     * 2007	商品已下架
     */
    private BaseResponse createOutOrderFail(ProviderTrade providerTrade, UserOrderCreateResult result) {
        log.warn("开放平台商品下单失败, 响应编码 = {}, 响应信息 = {}", result.getStatus(), result.getMsg());

        String msg = "充值失败：" + result.getMsg();
        //更新子单备注
        ProviderTrade result4Child = mongoTemplate.findAndModify(Query.query(Criteria.where("_id").is(providerTrade.getId())), Update.update("sellerRemark", msg), ProviderTrade.class);
        if (Objects.isNull(result4Child)) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "订单子单备注信息更新失败");
        }
        //更新主单备注
        Trade result4Parent = mongoTemplate.findAndModify(Query.query(Criteria.where("_id").is(providerTrade.getParentId())), Update.update("sellerRemark", msg), Trade.class);
        if (Objects.isNull(result4Parent)) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "订单主单备注信息更新失败");
        }
        //消息通知客服
        // TODO: 2022/4/4 消息通知客服
        return BaseResponse.FAILED();
    }

    /**
     * 创建樊登读书开放平台订单
     */
    private UserOrderCreateResult createOutOrder(ProviderTrade providerTrade) {
        if (CollectionUtils.isEmpty(providerTrade.getTradeItems())) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "樊登读书直冲商品为空");
        }
        if (providerTrade.getTradeItems().size() > 1) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "樊登读书直冲商品仅支持单一充值");
        }

        UserOrderCreateParam createParam = new UserOrderCreateParam();
        createParam.setTradeNo(providerTrade.getId());
        createParam.setMobile(providerTrade.getDirectChargeMobile());
        createParam.setExternalProductNo(providerTrade.getTradeItems().get(0).getSkuId());
        createParam.setPayType(convertPayType(providerTrade.getPayWay()));
        createParam.setPayTime(providerTrade.getTradeState().getPayTime());
        return fddsOpenPlatformService.doRequest(orderCreateUrl, JSON.toJSONString(createParam), UserOrderCreateResult.class);
    }

    /**
     * 合作方支付方式：99-其他（无支付渠道费），1-IAP为苹果支付（支付渠道费30%），2-支付宝 3-微信（支付渠道费0.6%）
     */
    private String convertPayType(PayWay payWay) {
        if (PayWay.ALIPAY.equals(payWay)) {
            return "2";
        }
        if (PayWay.WECHAT.equals(payWay)) {
            return "3";
        }
        return "99";
    }
}
