package com.wanmi.sbc.order.provider.impl.payorder;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.*;
import com.wanmi.sbc.order.api.response.payorder.*;
import com.wanmi.sbc.order.bean.vo.PayOrderDetailVO;
import com.wanmi.sbc.order.bean.vo.PayOrderResponseVO;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.payorder.request.PayOrderRequest;
import com.wanmi.sbc.order.payorder.response.PayOrderPageResponse;
import com.wanmi.sbc.order.payorder.response.PayOrderResponse;
import com.wanmi.sbc.order.payorder.service.PayOrderService;
import com.wanmi.sbc.order.thirdplatformtrade.model.root.ThirdPlatformTrade;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import com.wanmi.sbc.order.trade.service.TradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Validated
@RestController
public class PayOrderQueryController implements PayOrderQueryProvider {

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private LinkedMallTradeService thirdPlatformTradeService;


    private List<PayOrderVO> toVoList(List<PayOrder> voList) {

        List<PayOrderVO> result = new ArrayList<>();

        voList.forEach(e -> {
            PayOrderVO target = new PayOrderVO();
            BeanUtils.copyProperties(e, target);
            result.add(target);
        });

        return result;
    }

    @Override
    public BaseResponse<FindPayOrderByPayOrderIdsResponse> findPayOrderByPayOrderIds(@RequestBody @Valid FindPayOrderByPayOrderIdsRequest request) {


        FindPayOrderByPayOrderIdsResponse response = FindPayOrderByPayOrderIdsResponse.builder()
                .orders(toVoList(payOrderService.findPayOrderByPayOrderIds(request.getPayOrderIds())))
                .build();

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<FindPayOrderByOrderCodeResponse> findPayOrderByOrderCode(@RequestBody @Valid FindPayOrderByOrderCodeRequest request) {

        PayOrderVO target = new PayOrderVO();

        Optional<PayOrder> raw = payOrderService.findPayOrderByOrderCode(request.getValue());

        if (raw.isPresent()) {

            BeanUtils.copyProperties(raw.get(), target);


        } else {

            target = null;
        }


        FindPayOrderByOrderCodeResponse response = FindPayOrderByOrderCodeResponse.builder().value(target)
                .build();

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<FindPayOrderByOrderCodesResponse> findPayOrderByOrderCodes(@RequestBody @Valid FindPayOrderByOrderCodesRequest request) {

        List<PayOrder> list=payOrderService.findPayOrderByOrderCodes(request.getValue());
        //转换成map
        Map<String,PayOrder> payOrderMap = list.stream().collect(Collectors.toMap(PayOrder::getOrderCode,Function.identity()));

        List<PayOrderVO> payOrderVOList = new ArrayList<>();
        list.forEach(vo -> {
            PayOrder payOrder = payOrderMap.get(vo.getOrderCode());
            if(Objects.nonNull(payOrder)){
                PayOrderVO target = new PayOrderVO();
                BeanUtils.copyProperties(payOrder, target);
                payOrderVOList.add(target);
            }
        });
        FindPayOrderByOrderCodesResponse response = FindPayOrderByOrderCodesResponse.builder().values(payOrderVOList)
                .build();

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<FindPayOrderResponse> findPayOrder(@RequestBody @Valid FindPayOrderRequest orderNo) {

        PayOrderResponse src = payOrderService.findPayOrder(orderNo.getValue());

        FindPayOrderResponse target = new FindPayOrderResponse();

        BeanUtils.copyProperties(src, target);

        return BaseResponse.success(target);
    }

    @Override
    public BaseResponse<FindPayOrderListResponse> findPayOrderList(@RequestBody @Valid FindPayOrderListRequest request) {
        List<Trade> trades = tradeService.detailsByParentId(request.getParentTid());
        List<PayOrderDetailVO> list = trades.stream().map(i -> {
            PayOrderResponse src = payOrderService.findPayOrder(i.getId());
            PayOrderDetailVO target = new PayOrderDetailVO();
            BeanUtils.copyProperties(src, target);
            target.setStoreName(i.getSupplier().getStoreName());
            target.setIsSelf(i.getSupplier().getIsSelf());
            if(i.getGrouponFlag() == Boolean.TRUE) {
                target.setGrouponNo(i.getTradeGroupon().getGrouponNo());
            }
            return target;
        }).collect(Collectors.toList());
        return BaseResponse.success(new FindPayOrderListResponse(list));
    }

    @Override
    public BaseResponse<FindPayOrdersResponse> findPayOrders(@RequestBody @Valid FindPayOrdersRequest srcrequest) {

        PayOrderRequest target = new PayOrderRequest();

        BeanUtils.copyProperties(srcrequest, target);

        PayOrderPageResponse rawresponse = payOrderService.findPayOrders(target);

        FindPayOrdersResponse response = KsBeanUtil.convert(rawresponse, FindPayOrdersResponse.class);

        //查询主订单编号列表
        List<PayOrderResponseVO> payOrderVOList=response.getPayOrderResponses();
        List<String> parentIdList=new ArrayList<>();
        parentIdList.add(srcrequest.getOrderNo());
        //根据主订单编号列表查询子订单
        List<ProviderTrade> result=providerTradeService.findListByParentIdList(parentIdList);
        List<TradeVO> items=new ArrayList<>();

        List<ThirdPlatformTrade> thirdPlatformTrades = thirdPlatformTradeService.findListByParentIds(result.stream().map(ProviderTrade::getId).collect(Collectors.toList()));

        if (CollectionUtils.isNotEmpty(result)){
            result.forEach(item->{
                TradeVO tradeVO = KsBeanUtil.convert(item, TradeVO.class);
                // 是linkedmall订单,填充linkedmall子单信息
                if(Objects.nonNull(item.getThirdPlatformType()) && ThirdPlatformType.LINKED_MALL.equals(item.getThirdPlatformType())) {
//                    List<ThirdPlatformTrade> thirdPlatformTrades = thirdPlatformTradeService.findListByParentId(item.getId());
                    if(CollectionUtils.isNotEmpty(thirdPlatformTrades)){
                        tradeVO.setTradeVOList(thirdPlatformTrades.stream().filter(vo -> StringUtils.equals(vo.getParentId(),item.getId())).map(vo -> KsBeanUtil.convert(vo, TradeVO.class)).collect(Collectors.toList()));
                    }else{
                        tradeVO.setTradeVOList(Lists.newArrayList());
                    }
                }
                if (srcrequest.getOrderNo().equals(item.getParentId())){
                    items.add(tradeVO);
                }
        });
            PayOrderResponseVO vo = new PayOrderResponseVO();
            if(CollectionUtils.isNotEmpty(payOrderVOList) && payOrderVOList.size() == 1) {
                vo = payOrderVOList.get(0);
                vo.setTradeVOList(items);
            } else {
                vo.setTradeVOList(items);
                payOrderVOList.add(vo);
            }
        }
        response.setPayOrderResponses(payOrderVOList);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<FindPayOrdersWithNoPageResponse> findPayOrdersWithNoPage(@RequestBody @Valid FindPayOrdersWithNoPageRequest payOrderRequest) {

        PayOrderRequest request = KsBeanUtil.convert(payOrderRequest, PayOrderRequest.class);

        PayOrderPageResponse rawresponse = payOrderService.findPayOrdersWithNoPage(request);

        FindPayOrdersWithNoPageResponse target = KsBeanUtil.convert(rawresponse, FindPayOrdersWithNoPageResponse.class);

        return BaseResponse.success(target);

    }

    @Override
    public BaseResponse<FindByOrderNosResponse> findByOrderNos(@RequestBody @Valid FindByOrderNosRequest request) {

        List<PayOrder> rawOrders = payOrderService.findByOrderNos(request.getOrderNos(), request.getPayOrderStatus());

        List<PayOrderVO> target = toVoList(rawOrders);

        FindByOrderNosResponse response = FindByOrderNosResponse.builder().orders(target).build();

        return BaseResponse.success(response);

    }

    @Override
    public BaseResponse<SumPayOrderPriceResponse> sumPayOrderPrice(@RequestBody @Valid SumPayOrderPriceRequest payOrderRequest) {

        PayOrderRequest request = KsBeanUtil.convert(payOrderRequest, PayOrderRequest.class);


        SumPayOrderPriceResponse response =
                SumPayOrderPriceResponse.builder().value(payOrderService.sumPayOrderPrice(request)).build();

        return BaseResponse.success(response);
    }
}
