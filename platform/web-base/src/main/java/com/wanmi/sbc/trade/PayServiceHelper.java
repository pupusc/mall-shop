package com.wanmi.sbc.trade;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeListAllRequest;
import com.wanmi.sbc.order.api.request.trade.TradeListByParentIdRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>客户端支付公共方法</p>
 * Created by of628-wenzhi on 2019-07-24-19:56.
 */
@Service
public class PayServiceHelper {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    /**
     * 获取可用于支付交易的订单号（子订单号或父订单号）
     *
     * @param id
     * @param parentId
     * @return
     */
    public String getPayBusinessId(String id, String parentId) {
        String oid = StringUtils.isNotBlank(id) ? id : parentId;
        List<TradeVO> trades = findTrades(oid);
        if (CollectionUtils.isNotEmpty(trades)) {
            TradeVO trade = trades.get(0);
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                    && trade.getBookingType() == BookingType.EARNEST_MONEY && trade.getTradeState().getPayState() == PayState.PAID_EARNEST) {
                if(trade.getTailOrderNo() == null) {
                    throw new SbcRuntimeException("K-100203");
                }
                oid = trade.getTailOrderNo();
            }
        }
        return oid;
    }

    /**
     * 根据订单号或父订单号获取订单信息，用于支付前获取订单信息
     *
     * @param businessId 订单号（单笔支付）或 父订单号（多笔订单合并支付）
     * @return 订单信息集合
     */
    public List<TradeVO> findTrades(String businessId) {
        List<TradeVO> tradeVOList = new ArrayList<>();
        if (businessId.startsWith(GeneratorService._PREFIX_TRADE_TAIL_ID)) {
            tradeVOList.addAll(tradeQueryProvider.listAll(TradeListAllRequest.builder().tradeQueryDTO(
                    TradeQueryDTO.builder().tailOrderNo(businessId).build()).build()).getContext().getTradeVOList());
        } else if (businessId.startsWith(GeneratorService._PREFIX_PARENT_TRADE_ID)) {
            tradeVOList.addAll(tradeQueryProvider.getListByParentId(TradeListByParentIdRequest.builder().parentTid(businessId)
                    .build()).getContext().getTradeVOList());
        } else if (businessId.startsWith(GeneratorService._PREFIX_TRADE_ID)) {
            tradeVOList.add(tradeQueryProvider.getById(TradeGetByIdRequest.builder()
                    .tid(businessId).build()).getContext().getTradeVO());
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return tradeVOList;
    }

    /**
     * 公共方法，支付前校验订单状态，已作废，未审核并且已支付的订单做异常处理
     *
     * @param tradeList 订单列表
     */
    public void checkPayBefore(List<TradeVO> tradeList) {
        tradeList.forEach(i -> {
            //添加订单失效时间过去了就不能支付
            if ((null != i.getOrderTimeOut() && i.getOrderTimeOut().isBefore(LocalDateTime.now()))
                    || (i.getTradeState().getFlowState() == FlowState.INIT) || (i.getTradeState().getFlowState() ==
                    FlowState.VOID)) {
                throw new SbcRuntimeException("K-050206");
            }
            if (i.getTradeState().getPayState() == PayState.PAID) {
                throw new SbcRuntimeException("K-100203");
            }
        });
    }

    public List<TradeVO> checkTrades(String id) {
        List<TradeVO> trades = findTrades(id);
        checkPayBefore(trades);
        return trades;
    }

    public BigDecimal calcTotalPriceByPenny(List<TradeVO> trades) {
        BigDecimal totalPrice;
        TradeVO trade = trades.get(0);
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getBookingType() == BookingType.EARNEST_MONEY
                && trade.getTradeState().getPayState() == PayState.NOT_PAID) {
            totalPrice = trade.getTradePrice().getEarnestPrice().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN);
        } else if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                && trade.getBookingType() == BookingType.EARNEST_MONEY && trade.getTradeState().getPayState() == PayState.PAID_EARNEST) {
            totalPrice = trade.getTradePrice().getTailPrice().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            //订单总金额
            totalPrice = trades.stream().map(i -> i.getTradePrice().getTotalPrice().multiply(new BigDecimal(100))
                    .setScale(0, BigDecimal.ROUND_DOWN)).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        //订单总金额
        return totalPrice;
    }

    public BigDecimal calcTotalPriceByYuan(List<TradeVO> trades) {
        BigDecimal totalPrice;
        TradeVO trade = trades.get(0);
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getBookingType() == BookingType.EARNEST_MONEY
                && trade.getTradeState().getPayState() == PayState.NOT_PAID) {
            totalPrice = trade.getTradePrice().getEarnestPrice();
        } else if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                && trade.getBookingType() == BookingType.EARNEST_MONEY && trade.getTradeState().getPayState() == PayState.PAID_EARNEST) {
            totalPrice = trade.getTradePrice().getTailPrice();
        } else {
            //订单总金额
            totalPrice = trades.stream().map(i -> i.getTradePrice().getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        //订单总金额
        return totalPrice;
    }

    public String buildBody(List<TradeVO> trades) {
        TradeVO trade = trades.get(0);
        StringBuilder body = new StringBuilder();
        String skuName = trade.getTradeItems().get(0).getSkuName();
        if (StringUtils.isNotEmpty(skuName) && skuName.length() > 35) {
            body.append(skuName, 0, 35).append("...");
        } else {
            body.append(StringUtils.isEmpty(skuName) ? "" : skuName);
        }

        String specDetails = trade.getTradeItems().get(0).getSpecDetails();
        if (StringUtils.isNotEmpty(specDetails) && specDetails.length() > 5) {
            body.append(specDetails, 0, 5).append("...");
        } else {
            body.append(StringUtils.isEmpty(specDetails) ? "" : specDetails);
        }
        return body.toString();
    }
}
