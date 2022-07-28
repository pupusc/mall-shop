package com.wanmi.sbc.pay.provider.impl;

import com.soybean.common.util.RedisLockKeyUtil;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.PayResponse;
import com.wanmi.sbc.pay.api.response.RefundResponse;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGateway;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import com.wanmi.sbc.pay.repository.TradeRecordRepository;
import com.wanmi.sbc.pay.service.PayDataService;
import com.wanmi.sbc.pay.service.PayService;
import com.wanmi.sbc.pay.unionpay.acp.sdk.AcpService;
import com.wanmi.sbc.pay.utils.GeneratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>支付操作接口实现</p>
 * Created by of628-wenzhi on 2018-08-18-下午4:41.
 */
@RestController
@Validated
@Slf4j
public class PayController implements PayProvider {

    @Autowired
    private PayService payService;

    @Autowired
    private PayDataService payDataService;

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public BaseResponse addGateway(@RequestBody @Valid GatewayAddRequest gatewayAddRequest) {
        PayGateway gateway = PayGateway.builder()
                .createTime(LocalDateTime.now())
                .isOpen(IsOpen.YES)
                .name(gatewayAddRequest.getGatewayEnum())
                .type(gatewayAddRequest.getType())
                .build();
        payDataService.saveGateway(gateway);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyGateway(@RequestBody @Valid GatewayModifyRequest gatewayModifyRequest) {
        PayGateway gateway = PayGateway.builder()
                .isOpen(gatewayModifyRequest.getIsOpen())
                .name(gatewayModifyRequest.getGatewayEnum())
                .id(gatewayModifyRequest.getId())
                .type(gatewayModifyRequest.getType())
                .build();
        payDataService.modifyGateway(gateway);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse saveChannelItem(@RequestBody @Valid ChannelItemSaveRequest channelItemSaveRequest) {
        PayChannelItem payChannelItem = KsBeanUtil.copyPropertiesThird(channelItemSaveRequest, PayChannelItem.class);
        if (payChannelItem.getId() == null || payChannelItem.getCreateTime() == null) {
            payChannelItem.setCreateTime(LocalDateTime.now());
            payChannelItem.setIsOpen(channelItemSaveRequest.getIsOpen());
        }
        payChannelItem.setGateway(PayGateway.builder().id(channelItemSaveRequest.getGatewayId()).build());
        payDataService.saveItem(payChannelItem);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse saveGatewayConfig(@RequestBody @Valid GatewayConfigSaveRequest gatewayConfigSaveRequest) {
        PayGatewayConfig config = KsBeanUtil.copyPropertiesThird(gatewayConfigSaveRequest, PayGatewayConfig.class);
        if (config.getId() == null || config.getCreateTime() == null) {
            config.setCreateTime(LocalDateTime.now());
        }
        config.setPayGateway(PayGateway.builder().id(gatewayConfigSaveRequest.getGatewayId()).build());
        payDataService.saveConfig(config);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<PayResponse> getPayCharge(@RequestBody @Valid PayExtraRequest payRequest) {
        return BaseResponse.success(new PayResponse(payService.pay(payRequest)));
    }

    @Override
    public BaseResponse<RefundResponse> refund(@RequestBody @Valid RefundRequest refundRequest) {
        return BaseResponse.success(new RefundResponse(payService.refund(refundRequest)));
    }

    @Override
    public BaseResponse callback(@RequestBody @Valid TradeCallbackRequest request) {
        payService.callback(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<String> unionB2BPay(@RequestBody @Valid UnionPayRequest unionPay) {
        return BaseResponse.success(payService.unionB2BPay(unionPay));
    }

    @Override
    public BaseResponse<Boolean> unionCheckSign(@RequestBody Map<String, String> validateData) {
        return BaseResponse.success(AcpService.validate(validateData, "UTF-8"));
    }

    /**
     * 银联企业支付 同步回调添加交易数据
     *
     * @param resMap
     * @return
     */
    @Override
    public BaseResponse unionCallBack(@RequestBody Map<String, String> resMap) {
        PayTradeRecord payTradeRecord = payDataService.queryByBusinessId(resMap.get("orderId"));
        if (payTradeRecord == null) {
            payTradeRecord = new PayTradeRecord();
            payTradeRecord.setId(GeneratorUtils.generatePT());
        }
        payService.unionCallBack(resMap, payTradeRecord);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 微信支付 同步回调添加交易数据
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse wxPayCallBack(@RequestBody PayTradeRecordRequest request) {
        //因为当前是查询和新增在一起有并发问题，所以这要添加锁信息

        PayTradeRecord payTradeRecord = payDataService.queryByBusinessId(request.getBusinessId());
        if (payTradeRecord == null) {
            RLock rLock = redissonClient.getFairLock(RedisLockKeyUtil.PAY_TRADE_RECORD_LOCK_PREFIX + request.getBusinessId());
            try {
                if (rLock.tryLock(3, 3, TimeUnit.SECONDS)) {
                    payTradeRecord = payDataService.queryByBusinessId(request.getBusinessId());
                    if (payTradeRecord == null) {
                        payTradeRecord = new PayTradeRecord();
                        payTradeRecord.setId(GeneratorUtils.generatePT());
                        payTradeRecord.setBusinessId(request.getBusinessId());
                        payTradeRecord.setCreateTime(LocalDateTime.now());
                    }
                }  else {
                    throw new SbcRuntimeException("K-000001", "操作频繁");
                }
            } catch (InterruptedException ex) {
                log.error("PayController wxPayCallBack interruptedException error", ex);
                throw new SbcRuntimeException("K-000001", "操作频繁");
            } catch (Exception ex) {
                log.error("PayController wxPayCallBack error", ex);
                throw ex;
            } finally {
                rLock.unlock();
            }
        }
        if (request.getChannelItemId() != null) {
            //更新支付记录支付项字段
            payTradeRecord.setChannelItemId(request.getChannelItemId());
        }
        //设置appid
        if (StringUtils.isNotBlank(request.getAppId())) {
            payTradeRecord.setAppId(request.getAppId());
        }
        payService.wxPayCallBack(request, payTradeRecord);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse savePayGateway(@RequestBody @Valid PayGatewaySaveRequest payGatewaySaveRequest) {
        payDataService.savePayGateway(payGatewaySaveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse uploadPayCertificate(@RequestBody PayGatewayUploadPayCertificateRequest request) {
        payDataService.uploadPayCertificate(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse savePayTradeRecord(@RequestBody PayTradeRecordRequest request) {
        payService.addPayTradeRecord(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse batchSavePayTradeRecord(@RequestBody List<PayTradeRecordRequest> payTradeRecordRequestList) {
        payTradeRecordRequestList.forEach(
                recordRequest -> payService.addPayTradeRecord(recordRequest));
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse savePayGatewayByTerminalType(@RequestBody @Valid PayGatewaySaveByTerminalTypeRequest request) {
        payDataService.savePayGatewayByTerminalType(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse saveAppId(String appId, String tradeNo) {
        payDataService.saveAppId(appId, tradeNo);
        return BaseResponse.SUCCESSFUL();
    }
}
