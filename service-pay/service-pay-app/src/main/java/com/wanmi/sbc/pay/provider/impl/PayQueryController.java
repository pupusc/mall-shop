package com.wanmi.sbc.pay.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.*;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.bean.vo.PayGatewayConfigVO;
import com.wanmi.sbc.pay.bean.vo.PayGatewayVO;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGateway;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import com.wanmi.sbc.pay.service.PayDataService;
import com.wanmi.sbc.pay.service.PayService;
import com.wanmi.sbc.pay.unionpay.acp.sdk.AcpService;
import com.wanmi.sbc.pay.unionpay.acp.sdk.SDKConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>支付查询接口实现</p>
 * Created by of628-wenzhi on 2018-08-18-下午4:45.
 */
@RestController
@Validated
@Slf4j
public class PayQueryController implements PayQueryProvider {

    @Autowired
    private PayDataService payDataService;

    @Autowired
    private PayService payService;

    @Override
    public BaseResponse<PayTradeRecordResponse> getTradeRecordById(@RequestBody @Valid TradeRecordByIdRequest
                                                                           tradeRecordByIdRequest) {
        PayTradeRecord payTradeRecord = payDataService.queryTradeRecord(tradeRecordByIdRequest.getRecodId());
        return BaseResponse.success(wraperResponseForRecord(payTradeRecord));
    }

    @Override
    public BaseResponse<PayTradeRecordResponse> getTradeRecordByChargeId(@RequestBody @Valid TradeRecordByChargeRequest
                                                                                 recordByChangeRequest) {
        PayTradeRecord payTradeRecord = payDataService.queryByChargeId(recordByChangeRequest.getChargeId());
        return BaseResponse.success(wraperResponseForRecord(payTradeRecord));
    }

    @Override
    public BaseResponse<PayTradeRecordResponse> getTradeRecordByOrderCode(@RequestBody @Valid
                                                                                  TradeRecordByOrderCodeRequest
                                                                                  recordByOrderCodeRequest) {
        PayTradeRecord payTradeRecord = payDataService.queryByBusinessId(recordByOrderCodeRequest.getOrderId());
        return BaseResponse.success(wraperResponseForRecord(payTradeRecord));
    }

    @Override
    public BaseResponse<PayTradeRecordResponse> getTradeRecordByOrderOrParentCode(
            @RequestBody @Valid TradeRecordByOrderOrParentCodeRequest tradeRecordByOrderOrParentCodeRequest) {
        PayTradeRecord payTradeRecord = null;
        if (StringUtils.isNotBlank(tradeRecordByOrderOrParentCodeRequest.getOrderId())) {
            payTradeRecord = payDataService.queryByBusinessId(tradeRecordByOrderOrParentCodeRequest.getOrderId());
        }
        if (payTradeRecord == null && StringUtils.isNotBlank(tradeRecordByOrderOrParentCodeRequest.getParentId())) {
            payTradeRecord = payDataService.queryByBusinessId(tradeRecordByOrderOrParentCodeRequest.getParentId());
        }
        return BaseResponse.success(wraperResponseForRecord(payTradeRecord));
    }

    @Override
    public BaseResponse<PayTradeRecordCountResponse> getTradeRecordCountByOrderOrParentCode(
            @RequestBody @Valid TradeRecordCountByOrderOrParentCodeRequest tradeRecordCountByOrderOrParentCodeRequest) {
        long count = 0L;
        if (StringUtils.isNotBlank(tradeRecordCountByOrderOrParentCodeRequest.getOrderId())) {
            count = payDataService.countByBusinessId(tradeRecordCountByOrderOrParentCodeRequest.getOrderId());
        }
        if (count == 0L && StringUtils.isNotBlank(tradeRecordCountByOrderOrParentCodeRequest.getParentId())) {
            count = payDataService.countByBusinessId(tradeRecordCountByOrderOrParentCodeRequest.getParentId());
        }
        return BaseResponse.success(new PayTradeRecordCountResponse(count));
    }

    @Override
    public BaseResponse<PayTradeRecordCountResponse> getTradeRecordCountByOrderCode
            (@RequestBody @Valid TradeRecordCountByOrderCodeRequest recordCountByOrderCodeRequest) {
        long count = payDataService.countByBusinessId(recordCountByOrderCodeRequest.getOrderId());
        return BaseResponse.success(new PayTradeRecordCountResponse(count));
    }

    @Override
    public BaseResponse<PayGatewayListResponse> listGatewayByStoreId(GatewayByStoreIdRequest request) {
        List<PayGateway> list = payDataService.queryGatewaysByStoreId(request.getStoreId());
        List<PayGatewayVO> results = list.stream().map(i -> {
            PayGatewayVO payGatewayVO = new PayGatewayResponse();
            KsBeanUtil.copyPropertiesThird(i, payGatewayVO);
            return payGatewayVO;
        }).collect(Collectors
                .toList());
        return BaseResponse.success(new PayGatewayListResponse(results));
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<PayGatewayResponse> getGatewayById(@RequestBody @Valid GatewayByIdRequest gatewayByIdRequest) {
        PayGateway gateway = payDataService.queryGateway(gatewayByIdRequest.getGatewayId());
        return BaseResponse.success(wraperResponseForGateway(gateway));
    }

    @Override
    public BaseResponse<PayChannelItemListResponse> listChannelItemByGatewayName(@RequestBody @Valid
                                                                                       ChannelItemByGatewayRequest
                                                                                       channelItemByGatewayRequest) {
        List<PayChannelItem> channelItems = payDataService.queryItemByGatewayName(channelItemByGatewayRequest
                .getGatewayName());
        return BaseResponse.success(wraperResponseListForChannelItems(channelItems));
    }

    @Override
    public BaseResponse<PayChannelItemListResponse> listOpenedChannelItemByGatewayName(@RequestBody @Valid
                                                                                             OpenedChannelItemRequest
                                                                                             openedChannelItemRequest) {
        List<PayChannelItem> channelItems = payDataService.queryOpenItemByGatewayName(openedChannelItemRequest
                .getGatewayName(), openedChannelItemRequest.getTerminalType());
        return BaseResponse.success(wraperResponseListForChannelItems(channelItems));
    }

    @Override
    public BaseResponse<PayChannelItemResponse> getChannelItemById(@RequestBody @Valid ChannelItemByIdRequest
                                                                           channelItemByIdRequest) {
        PayChannelItem channelItem = payDataService.queryItemById(channelItemByIdRequest.getChannelItemId());
        return BaseResponse.success(wraperResponseForChannelItem(channelItem));
    }

    @Override
    public BaseResponse<PayGatewayConfigResponse> getGatewayConfigById(@RequestBody @Valid GatewayConfigByIdRequest
                                                                               gatewayConfigByIdRequest) {
        PayGatewayConfig config = payDataService.queryConfig(gatewayConfigByIdRequest.getGatewayConfigId());
        return BaseResponse.success(wraperResponseForGatewayConfig(config));
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<PayGatewayConfigResponse> getGatewayConfigByGateway(@RequestBody @Valid
                                                                                    GatewayConfigByGatewayRequest
                                                                                    gatewayConfigByGatewayRequest) {
        PayGatewayConfig config = payDataService.queryConfigByNameAndStoreId(gatewayConfigByGatewayRequest.getGatewayEnum(),gatewayConfigByGatewayRequest.getStoreId());
        return BaseResponse.success(wraperResponseForGatewayConfig(config));
    }


    @Override
    @Transactional(readOnly = true)
    public BaseResponse<PayGatewayConfigResponse> getGatewayConfigByGatewayUnException(@RequestBody @Valid
                                                                                    GatewayConfigByGatewayRequest
                                                                                    gatewayConfigByGatewayRequest) {
        PayGatewayConfig config = payDataService.getGatewayConfigByGatewayUnException(gatewayConfigByGatewayRequest.getGatewayEnum(),gatewayConfigByGatewayRequest.getStoreId());
        return BaseResponse.success(wraperResponseForGatewayConfig(config));
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<PayGatewayConfigResponse> getGatewayConfigByGatewayId(@RequestBody @Valid
                                                                                      GatewayConfigByGatewayIdRequest
                                                                                      gatewayConfigByGatewayIdRequest) {
        PayGatewayConfig config = payDataService.queryConfigByGatwayIdAndStoreId(gatewayConfigByGatewayIdRequest.getGatewayId(),gatewayConfigByGatewayIdRequest.getStoreId());
        return BaseResponse.success(wraperResponseForGatewayConfig(config));
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<PayGatewayConfigListResponse> listOpenedGatewayConfig(@RequestBody @Valid GatewayOpenedByStoreIdRequest request) {
        // todo Saas 独立收款根据storeid过滤开启的在线支付方式
        List<PayGatewayConfig> configs = payDataService.queryConfigByOpenAndStoreId(request.getStoreId());
        List<PayGatewayConfigVO> responseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(configs)) {
            responseList = configs.stream().map(this::wraperVoForGatewayConfig).collect(Collectors.toList());
        }
        return BaseResponse.success(new PayGatewayConfigListResponse(responseList));
    }

    @Override
    public BaseResponse<PayResultResponse> getPayResponseByOrdercode(@RequestBody @Valid PayResultByOrdercodeRequest
                                                                             payResultByOrdercodeRequest) {
        return BaseResponse.success(new PayResultResponse(payService.queryPayResult(payResultByOrdercodeRequest
                .getOrderCode())));
    }

    @Override
    public BaseResponse<RefundResultResponse> getRefundResponseByOrdercode(@RequestBody @Valid
                                                                                   RefundResultByOrdercodeRequest
                                                                                   refundResultByOrdercodeRequest) {

        return BaseResponse.success(new RefundResultResponse(payService.queryRefundResult
                (refundResultByOrdercodeRequest.getReturnOrderCode(), refundResultByOrdercodeRequest.getOrderCode())));
    }

    @Override
    public BaseResponse<WxOpenIdResponse> getWxOpenIdByCodeAndStoreId(@RequestBody @Valid WxCodeRequest wxCodeRequest) {
        return BaseResponse.success(new WxOpenIdResponse(payService.getWxOpenIdAndStoreId(wxCodeRequest.getCode(),wxCodeRequest.getStoreId())));
    }

    @Override
    public BaseResponse<Map<String, String>> getUnionPayResult(@RequestBody UnionPayRequest unionPay) {
        String merId = unionPay.getApiKey();
        String orderId = unionPay.getBusinessId();
        String txnTime = unionPay.getTxnTime();

        Map<String, String> data = new HashMap<>();

        String encoding = "UTF-8";
        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", SDKConfig.getConfig().getVersion());                 //版本号
        data.put("encoding", encoding);               //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        data.put("txnType", "00");                             //交易类型 00-默认
        data.put("txnSubType", "00");                          //交易子类型  默认00
        data.put("bizType", "000202");                         //业务类型

        /***商户接入参数***/
        data.put("merId", merId);                               //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改

        /***要调通交易以下字段必须修改***/
        data.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
        data.put("txnTime", txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

        Map<String, String> reqData = AcpService.sign(data, encoding);            //报文中certId,
        // signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getSingleQueryUrl();
        //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
        Map<String, String> rspData = AcpService.post(reqData, url, encoding);
        //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;
        // 这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        return BaseResponse.success(rspData);
    }

    private PayTradeRecordResponse wraperResponseForRecord(PayTradeRecord record) {
        if (record != null) {
            PayTradeRecordResponse response = new PayTradeRecordResponse();
            KsBeanUtil.copyPropertiesThird(record, response);
            return response;
        }
        return null;
    }

    private PayGatewayResponse wraperResponseForGateway(PayGateway payGateway) {
        if (payGateway != null) {
            PayGatewayResponse response = new PayGatewayResponse();
            KsBeanUtil.copyPropertiesThird(payGateway, response);
            if (payGateway.getConfig() != null) {
                PayGatewayConfigVO configVO = new PayGatewayConfigVO();
                KsBeanUtil.copyPropertiesThird(payGateway.getConfig(), configVO);
                response.setConfig(configVO);
            }
            if (CollectionUtils.isNotEmpty(payGateway.getPayChannelItemList())) {
                List<PayChannelItemVO> voList = payGateway.getPayChannelItemList().stream().map(
                        i -> {
                            PayChannelItemVO channelItemVO = new PayChannelItemVO();
                            KsBeanUtil.copyPropertiesThird(i, channelItemVO);
                            return channelItemVO;
                        }
                ).collect(Collectors.toList());
                response.setPayChannelItemList(voList);
            }
            return response;
        }
        return null;
    }

    private PayChannelItemResponse wraperResponseForChannelItem(PayChannelItem channelItem) {
        if (channelItem != null) {
            PayChannelItemResponse response = new PayChannelItemResponse();
            KsBeanUtil.copyPropertiesThird(channelItem, response);
            if (channelItem.getGateway() != null) {
                PayGatewayVO gatewayVO = new PayGatewayVO();
                KsBeanUtil.copyPropertiesThird(channelItem.getGateway(), gatewayVO);
                response.setGateway(gatewayVO);
            }
            return response;
        }
        return null;
    }

    private PayChannelItemVO wraperVoForChannelItem(PayChannelItem channelItem) {
        if (channelItem != null) {
            PayChannelItemVO vo = new PayChannelItemVO();
            KsBeanUtil.copyPropertiesThird(channelItem, vo);
            if (channelItem.getGateway() != null) {
                PayGatewayVO gatewayVO = new PayGatewayVO();
                KsBeanUtil.copyPropertiesThird(channelItem.getGateway(), gatewayVO);
                vo.setGateway(gatewayVO);
            }
            return vo;
        }
        return null;
    }

    private PayChannelItemListResponse wraperResponseListForChannelItems(List<PayChannelItem> channelItems) {
        PayChannelItemListResponse response = new PayChannelItemListResponse();
        List<PayChannelItemVO> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(channelItems)) {
            voList = channelItems.stream().map(this::wraperVoForChannelItem)
                    .collect(Collectors
                            .toList());
        }
        response.setPayChannelItemVOList(voList);
        return response;
    }

    private PayGatewayConfigResponse wraperResponseForGatewayConfig(PayGatewayConfig payGatewayConfig) {
        if (payGatewayConfig != null) {
            PayGatewayConfigResponse response = new PayGatewayConfigResponse();
            KsBeanUtil.copyPropertiesThird(payGatewayConfig, response);
            if (payGatewayConfig.getPayGateway() != null) {
                PayGatewayVO payGatewayVO = new PayGatewayVO();
                KsBeanUtil.copyPropertiesThird(payGatewayConfig.getPayGateway(), payGatewayVO);
                response.setPayGateway(payGatewayVO);
                return response;
            }
        }
        return null;
    }

    private PayGatewayConfigVO wraperVoForGatewayConfig(PayGatewayConfig payGatewayConfig) {
        if (payGatewayConfig != null) {
            PayGatewayConfigVO vo = new PayGatewayConfigVO();
            KsBeanUtil.copyPropertiesThird(payGatewayConfig, vo);
            if (payGatewayConfig.getPayGateway() != null) {
                PayGatewayVO payGatewayVO = new PayGatewayVO();
                KsBeanUtil.copyPropertiesThird(payGatewayConfig.getPayGateway(), payGatewayVO);
                vo.setPayGateway(payGatewayVO);
                return vo;
            }
        }
        return null;
    }


    /**
     *  初始化店铺获取网关列表
     *  以boss生效的数据为基准 生成店铺的数据
     *  默认未启用
     */
    @Override
    public BaseResponse<PayGatewayListResponse> initGatewayByStoreId(GatewayInitByStoreIdRequest request) {
        List<PayGateway> list = payDataService.queryGatewaysByStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        if(CollectionUtils.isNotEmpty(list)){
            List<PayGateway> storeList =  list.stream().map(i -> {
                PayGateway payGateway = new PayGateway();
                KsBeanUtil.copyPropertiesThird(i, payGateway);
                payGateway.setId(null);
                payGateway.setPayChannelItemList(null);
                payGateway.setConfig(null);
                payGateway.setStoreId(request.getStoreId());
                payGateway.setIsOpen(IsOpen.NO);
                payDataService.saveGateway(payGateway);
                PayGatewayConfig payGatewayConfig =new PayGatewayConfig();
                payGatewayConfig.setStoreId(request.getStoreId());
                payGatewayConfig.setPayGateway(payGateway);
                payDataService.saveConfig(payGatewayConfig);
                return payGateway;
            }).collect(Collectors
                    .toList());

            List<PayGatewayVO> results = storeList.stream().map(i -> {
                PayGatewayVO payGatewayVO = new PayGatewayResponse();
                KsBeanUtil.copyPropertiesThird(i, payGatewayVO);
                return payGatewayVO;
            }).collect(Collectors
                    .toList());
            return BaseResponse.success(new PayGatewayListResponse(results));
        }
        return BaseResponse.success(new PayGatewayListResponse(new ArrayList<>()));
    }

    @Override
    public BaseResponse<List<PayChannelItemVO>> getAllChannelItem() {
        List<PayChannelItemVO> list =  this.payService.getAllChannelItem();
        return BaseResponse.success(list);
    }


//    @Override
//    public BaseResponse<PayGatewayConfigResponse> queryConfigByAccountAndStoreId(GatewayConfigByGatewayRequest req) {
//        List<PayGatewayConfig> configs = payDataService.queryConfigByAccountAndStoreId(req.getAccount(), req.getStoreId());
//        if (CollectionUtils.isEmpty(configs)) {
//            log.info("PayQueryController queryConfigByAccountAndStoreId account:{} storeId:{} 对应的配置信息不存在", req.getAccount(), req.getStoreId());
//            throw new SbcRuntimeException("K-99999", String.format("account:%s storeId:%s 对应的配置信息不存在", req.getAccount(), req.getStoreId()));
//        }
//        return BaseResponse.success(wraperResponseForGatewayConfig(configs.get(0)));
//    }


    @Override
    public BaseResponse<PayGatewayConfigResponse> queryConfigByAppIdAndStoreId(GatewayConfigByGatewayRequest req) {
        List<PayGatewayConfig> configs = null;
        log.info("PayQueryController queryConfigByAppIdAndStoreId param {}", JSON.toJSONString(req));
        if (Objects.equals(req.getGatewayEnum(), PayGatewayEnum.WECHAT)) {
            configs = payDataService.queryConfigByAppIdAndMchIdAndStoreId(req.getAppId(), req.getMchId(), req.getStoreId());
        } else if (Objects.equals(req.getGatewayEnum(), PayGatewayEnum.ALIPAY)) {
            configs = payDataService.queryConfigByAppIdAndStoreId(req.getAppId(), req.getStoreId());
        }
        log.info("PayQueryController queryConfigByAppIdAndStoreId result {}", JSON.toJSONString(configs));
        if (CollectionUtils.isEmpty(configs)) {
            log.info("PayQueryController queryConfigByAppIdAndStoreId account:{} storeId:{} 对应的配置信息不存在", req.getAppId(), req.getStoreId());
            throw new SbcRuntimeException("K-99999", String.format("account:%s storeId:%s 对应的配置信息不存在", req.getAppId(), req.getStoreId()));
        }
        return BaseResponse.success(wraperResponseForGatewayConfig(configs.get(0)));
    }


    @Override
    public BaseResponse<PayGatewayConfigResponse> queryConfigByAppId(GatewayConfigByGatewayRequest req) {
        PayGateway payGatewayNew = payService.getPayGatewayNew(req.getAppId(), req.getMchId(), req.getStoreId());
        return BaseResponse.success(wraperResponseForGatewayConfig(payGatewayNew.getConfig()));
    }
}
