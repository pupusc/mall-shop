package com.wanmi.sbc.order.provider.impl.thirdplatformtrade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.order.api.provider.thirdplatformtrade.ThirdPlatformTradeProvider;
import com.wanmi.sbc.order.api.request.thirdplatformtrade.ThirdPlatformTradeAddRequest;
import com.wanmi.sbc.order.api.request.thirdplatformtrade.ThirdPlatformTradeCompensateRequest;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradeUpdateRequest;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradeUpdateStateRequest;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.order.returnorder.service.ThirdPlatformReturnOrderService;
import com.wanmi.sbc.order.thirdplatformtrade.model.entity.LinkedMallTradeResult;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static com.wanmi.sbc.common.constant.RedisKeyConstant.LINKED_MALL_MQ_REPEATED;

/**
 * @Description: 第三方平台订单处理
 * @Autho daiyitian
 * @Date：2020-03-27 09:17
 */
@Slf4j
@Validated
@RestController
public class ThirdPlatformTradeController implements ThirdPlatformTradeProvider {

    @Autowired
    private LinkedMallTradeService linkedMallTradeService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ThirdPlatformReturnOrderService thirdPlatformReturnOrderService;

    @Autowired
    private RedisService redisService;

    @Override
    public BaseResponse add(@RequestBody @Valid ThirdPlatformTradeAddRequest request) {
        //防止重复消费
        if(redisService.hasKey(LINKED_MALL_MQ_REPEATED.concat(request.getBusinessId()))){
            return BaseResponse.SUCCESSFUL();
        }
        redisService.setString(LINKED_MALL_MQ_REPEATED.concat(request.getBusinessId()), "1", 60*60);
        if(ThirdPlatformType.LINKED_MALL.equals(request.getThirdPlatformType())){
            this.addByLinkedMall(request.getBusinessId());
        }
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse compensate(@RequestBody @Valid ThirdPlatformTradeCompensateRequest request) {
        if(ThirdPlatformType.LINKED_MALL.equals(request.getThirdPlatformType())){
            this.compensateByLinkedMall();
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新订单
     *
     * @param tradeUpdateRequest 订单信息 {@link ThirdPlatformTradeUpdateRequest}
     * @return
     */
    @Override
    public BaseResponse update(@RequestBody @Valid ThirdPlatformTradeUpdateRequest tradeUpdateRequest) {
        linkedMallTradeService.updateThirdPlatformTrade(tradeUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新第三方平台订单，同时修改ProviderTrade及Trade状态
     *
     * @param tradeUpdateStateRequest 订单信息 {@link ThirdPlatformTradeUpdateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse updateTradeState(@RequestBody @Valid ThirdPlatformTradeUpdateStateRequest tradeUpdateStateRequest){
        linkedMallTradeService.updateThirdPlatformTradeState(tradeUpdateStateRequest.getTradeUpdateStateDTO());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据业务id新增“下单并支付”的linkedmall订单
     * 此处不能加事务，里面每个子方法事务的
     * @param businessId 业务id
     */
    private void addByLinkedMall(String businessId){
        String now = DateUtil.nowTime();
        LinkedMallTradeResult result = null;
        try {
            result = linkedMallTradeService.add(businessId);
        } catch (Exception e) {
            log.error("订单业务id：" + businessId + "，第三方订单创建处理异常，自动退款! ", e);
            //其他异常，整笔含linkedMall交易全退
            thirdPlatformReturnOrderService.autoOrderRefundByBusinessId(businessId);
            return;
        }
        //创建失败需要退款的订单
        if (CollectionUtils.isNotEmpty(result.getAutoRefundTrades())) {
            thirdPlatformReturnOrderService.autoOrderRefund(result.getAutoRefundTrades());
        }

        //创建成功的订单
        if (CollectionUtils.isNotEmpty(result.getSuccessTrades())) {
            for (Trade trade : result.getSuccessTrades()) {
                try {
                    int res = linkedMallTradeService.pay(trade.getId());
                    //0：完成  1:自动退款 2：标记支付失败并定时观察 3标记支付失败
                    if (res == 1) {
                        thirdPlatformReturnOrderService.autoOrderRefund(Collections.singletonList(trade));
                    } else if (res == 2) {
                        tradeService.updateThirdPlatformPay(trade.getId(), true);
                        redisService.hset(RedisKeyConstant.LINKED_MALL_CHECKED_PEY_AUTO_REFUND, trade.getId(), now);
                    } else if (res == 3) {
                        tradeService.updateThirdPlatformPay(trade.getId(), true);
                    }
                }catch (Exception e){
                    log.error("订单id：" + trade.getId() + "，第三方订单支付处理异常，继续观察! ", e);
                    redisService.hset(RedisKeyConstant.LINKED_MALL_CHECKED_PEY_AUTO_REFUND, trade.getId(), now);
                }
            }
        }
    }

    /**
     * 补偿LinkedMall订单
     * 此处不能加事务，里面每个子方法事务的
     */
    private void compensateByLinkedMall() {
        Map<String, Object> keyMap = redisService.hgetAll(RedisKeyConstant.LINKED_MALL_CHECKED_PEY_AUTO_REFUND);
        if (MapUtils.isNotEmpty(keyMap)) {
            for (Map.Entry<String, Object> entry : keyMap.entrySet()) {
                String key = entry.getKey();
                int res = 0;
                LinkedMallTradeResult result = null;
                try {
                    result = linkedMallTradeService.compensate(key);
                    res = result.getStatus();
                } catch (Exception e) {
                    log.error("订单id：" + key + "，第三方订单补偿处理异常! ", e);
                    continue;
                }
                //0：完成  1:自动退款 2：继续观察 3标记支付失败
                if (res == 0) {
                    tradeService.updateThirdPlatformPay(key, false);
                    redisService.hdelete(RedisKeyConstant.LINKED_MALL_CHECKED_PEY_AUTO_REFUND, key);
                } else if (res == 1) {
                    thirdPlatformReturnOrderService.autoOrderRefund(result.getAutoRefundTrades());
                    redisService.hdelete(RedisKeyConstant.LINKED_MALL_CHECKED_PEY_AUTO_REFUND, key);
                } else if (res == 2) {
                    tradeService.updateThirdPlatformPay(key, true);
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime time = entry.getValue() != null ? DateUtil.parse(Objects.toString(entry.getValue()), DateUtil.FMT_TIME_1).plusMinutes(30) : null;
                    //如果超时30秒，还处于网络异常就停止继续观察
                    if (time == null || time.isBefore(now)) {
                        redisService.hdelete(RedisKeyConstant.LINKED_MALL_CHECKED_PEY_AUTO_REFUND, key);
                    }
                } else if (res == 3) {
                    tradeService.updateThirdPlatformPay(key, true);
                    redisService.hdelete(RedisKeyConstant.LINKED_MALL_CHECKED_PEY_AUTO_REFUND, key);
                }
            }
        }
    }
}