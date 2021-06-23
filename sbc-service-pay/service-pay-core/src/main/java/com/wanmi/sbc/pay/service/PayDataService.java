package com.wanmi.sbc.pay.service;

import com.wanmi.sbc.common.handler.aop.MasterRouteOnly;
import com.wanmi.sbc.pay.api.request.PayGatewaySaveByTerminalTypeRequest;
import com.wanmi.sbc.pay.api.request.PayGatewaySaveRequest;
import com.wanmi.sbc.pay.api.request.PayGatewayUploadPayCertificateRequest;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGateway;
import com.wanmi.sbc.pay.model.root.PayGatewayConfig;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import com.wanmi.sbc.pay.repository.ChannelItemRepository;
import com.wanmi.sbc.pay.repository.GatewayConfigRepository;
import com.wanmi.sbc.pay.repository.GatewayRepository;
import com.wanmi.sbc.pay.repository.TradeRecordRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by sunkun on 2017/8/9.
 */
@Service
public class PayDataService {

    @Autowired
    private TradeRecordRepository recordRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private ChannelItemRepository channelItemRepository;

    @Autowired
    private GatewayConfigRepository gatewayConfigRepository;

    /**
     * 获取交易记录
     *
     * @param id 交易记录主键
     * @return
     */
    public PayTradeRecord queryTradeRecord(String id) {
        return recordRepository.findById(id).get();
    }

    /**
     * 获取交易记录
     *
     * @param chargeId 交易对象id
     * @return
     */
    public PayTradeRecord queryByChargeId(String chargeId) {
        return recordRepository.findByChargeId(chargeId);
    }

    /**
     * 获取交易记录，订单查询场景下父订单号也作为匹配项，用于合并支付查询
     *
     * @param businessId       业务id
     * @return
     */
    @MasterRouteOnly
    public PayTradeRecord queryByBusinessId(String businessId) {
        return recordRepository.findByBusinessId(businessId);
    }

    /**
     * 根据业务id获取交易记录统计
     *
     * @param businessId 业务id
     * @return
     */
    public long countByBusinessId(String businessId) {
        return recordRepository.countByBusinessId(businessId);
    }

    /**
     * 获取网关列表
     *
     * @return
     */
    public List<PayGateway> queryGatewaysByStoreId(Long storeId) {
        return gatewayRepository.findByStoreId(storeId);
    }

    /**
     * 获取网关
     *
     * @param id 网关id
     * @return
     */
    public PayGateway queryGateway(Long id) {
        return gatewayRepository.findById(id).get();
    }

    /**
     * 保存网关
     *
     * @param payGateway
     */
    @Transactional
    public void saveGateway(PayGateway payGateway) {
        gatewayRepository.save(payGateway);
    }

    @Transactional
    public void modifyGateway(PayGateway payGateway) {
        gatewayRepository.update(payGateway.getId(), payGateway.getIsOpen(), payGateway.getType(),
                payGateway.getName());
    }

    /**
     * 获取网关下支付渠道项
     *
     * @param payGatewayEnum
     * @return
     */
    public List<PayChannelItem> queryItemByGatewayName(PayGatewayEnum payGatewayEnum) {
        return channelItemRepository.findByGatewayName(payGatewayEnum);
    }

    /**
     * 获取网关下开启的支付渠道项
     *
     * @param payGatewayEnum
     * @return
     */
    public List<PayChannelItem> queryOpenItemByGatewayName(PayGatewayEnum payGatewayEnum, TerminalType terminalType) {
        return channelItemRepository.findOpenItemByGatewayName(payGatewayEnum, terminalType);
    }

    /**
     * 获取渠道支付项
     *
     * @param id 渠道支付项id
     * @return
     */
    public PayChannelItem queryItemById(Long id) {
        return channelItemRepository.findById(id).orElse(null);
    }

    /**
     * 保存支付渠道项
     *
     * @param payChannelItem
     */
    @Transactional
    public void saveItem(PayChannelItem payChannelItem) {
        channelItemRepository.save(payChannelItem);
    }

    /**
     * 获取网关配置
     *
     * @param id 网关配置id
     * @return
     */
    public PayGatewayConfig queryConfig(Long id) {
        return gatewayConfigRepository.findById(id).get();
    }

    /**
     * 根据网关名称获取网关配置
     *
     * @return
     */
    public PayGatewayConfig queryConfigByNameAndStoreId(PayGatewayEnum payGatewayEnum,Long storeId) {
        return gatewayConfigRepository.queryConfigByNameAndStoreId(payGatewayEnum,storeId);
    }


    // /**
    //  * 根据网关名称获取网关配置
    //  *
    //  * @return
    //  */
    // // todo 退款改造完 queryConfigByName 用 queryConfigByNameAndStoreId 代替
    // public PayGatewayConfig queryConfigByName(PayGatewayEnum payGatewayEnum) {
    //     return gatewayConfigRepository.queryConfigByName(payGatewayEnum);
    // }

    /**
     * 根据网关id获取网关配置
     *
     * @param gatewayId 网关id
     * @return
     */
    public PayGatewayConfig queryConfigByGatwayIdAndStoreId(Long gatewayId,Long storeId) {
        return gatewayConfigRepository.queryConfigByGatwayIdAndStoreId(gatewayId,storeId);
    }

    public List<PayGatewayConfig> queryConfigByOpenAndStoreId(Long storeId) {
        return gatewayConfigRepository.queryConfigByOpenAndStoreId(storeId);
    }

    /**
     * 保存网关配置
     *
     * @param payGatewayConfig
     */
    @Transactional
    public void saveConfig(PayGatewayConfig payGatewayConfig) {
        gatewayConfigRepository.save(payGatewayConfig);
    }

    /**
     * 保存支付配置
     *
     * @param payGatewayRequest
     */
    @Transactional
    public void savePayGateway(PayGatewaySaveRequest payGatewayRequest) {
        //保存网关
        PayGateway gateway = new PayGateway();
        gateway.setId(payGatewayRequest.getId());
        gateway.setIsOpen(payGatewayRequest.getIsOpen());
        gateway.setName(PayGatewayEnum.valueOf(payGatewayRequest.getName()));
        gateway.setType(payGatewayRequest.getType());
        this.modifyGateway(gateway);
        //保存网关配置
        PayGatewayConfig payGatewayConfig = this.queryConfigByGatwayIdAndStoreId(payGatewayRequest.getId(),payGatewayRequest.getStoreId());
        // if(Objects.nonNull(payGatewayConfig)){
            PayGatewayConfig config = new PayGatewayConfig();
            config.setApiKey(payGatewayRequest.getPayGatewayConfig().getApiKey());
            config.setSecret(payGatewayRequest.getPayGatewayConfig().getSecret());
            config.setAppId(payGatewayRequest.getPayGatewayConfig().getAppId());
            config.setPrivateKey(payGatewayRequest.getPayGatewayConfig().getPrivateKey());
            config.setPublicKey(payGatewayRequest.getPayGatewayConfig().getPublicKey());
            config.setAppId2(payGatewayRequest.getPayGatewayConfig().getAppId2());
            config.setPayGateway(PayGateway.builder().id(payGatewayRequest.getId()).build());
            config.setBossBackUrl(payGatewayRequest.getPayGatewayConfig().getBossBackUrl());
            config.setPcBackUrl(payGatewayRequest.getPayGatewayConfig().getPcBackUrl());
            config.setPcWebUrl(payGatewayRequest.getPayGatewayConfig().getPcWebUrl());
            config.setAccount(payGatewayRequest.getPayGatewayConfig().getAccount());
            config.setOpenPlatformAppId(payGatewayRequest.getPayGatewayConfig().getOpenPlatformAppId());
            config.setOpenPlatformSecret(payGatewayRequest.getPayGatewayConfig().getOpenPlatformSecret());
            config.setOpenPlatformAccount(payGatewayRequest.getPayGatewayConfig().getOpenPlatformAccount());
            config.setOpenPlatformApiKey(payGatewayRequest.getPayGatewayConfig().getOpenPlatformApiKey());
            if (payGatewayConfig != null) {
                config.setId(payGatewayConfig.getId());
                config.setCreateTime(payGatewayConfig.getCreateTime());
                config.setWxPayCertificate(payGatewayConfig.getWxPayCertificate());
                config.setWxOpenPayCertificate(payGatewayConfig.getWxOpenPayCertificate());
                config.setStoreId(payGatewayConfig.getStoreId());
                if (StringUtils.isBlank(config.getAppId())) {
                    config.setAppId(payGatewayConfig.getAppId());
                }
                if (StringUtils.isBlank(config.getSecret())) {
                    config.setSecret(payGatewayConfig.getSecret());
                }
                if (StringUtils.isBlank(config.getOpenPlatformAppId())) {
                    config.setOpenPlatformAppId(payGatewayConfig.getOpenPlatformAppId());
                }
                if (StringUtils.isBlank(config.getOpenPlatformSecret())) {
                    config.setOpenPlatformSecret(payGatewayConfig.getOpenPlatformSecret());
                }
            }
            if (config.getId() == null || config.getCreateTime() == null) {
                config.setCreateTime(LocalDateTime.now());
                config.setStoreId(payGatewayRequest.getStoreId());
            }
            this.saveConfig(config);

            // payChannelItem 为系统预置数据 --保存渠道支付项
            // payGatewayRequest.getChannelItemList().forEach(item -> {
            //     PayChannelItem payChannelItem = this.queryItemById(item.getId());
            //     if (payChannelItem == null) {
            //         payChannelItem = new PayChannelItem();
            //         payChannelItem.setCreateTime(LocalDateTime.now());
            //         payChannelItem.setIsOpen(payGatewayRequest.getIsOpen());
            //         payChannelItem.setGateway(PayGateway.builder().id(payGatewayRequest.getId()).build());
            //     }
            //     //如果是银联b2b支付的话 pay_channel_item 表的开关状态和整个网管的状态保持一致
            //     if (PayGatewayEnum.UNIONB2B.toValue().equals(payGatewayRequest.getName())) {
            //         payChannelItem.setIsOpen(payGatewayRequest.getIsOpen());
            //     } else {
            //         payChannelItem.setIsOpen(item.getIsOpen());
            //     }
            //     this.saveItem(payChannelItem);
            // });
        // }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 上传微信支付证书
     * @Date 10:21 2019/5/7
     * @Param [request]
     **/
    public void uploadPayCertificate(PayGatewayUploadPayCertificateRequest request) {
        PayGatewayConfig payGatewayConfig = gatewayConfigRepository.findById(request.getId()).get();
        payGatewayConfig.setWxPayCertificate(request.getPayCertificate());
        payGatewayConfig.setWxOpenPayCertificate(request.getPayCertificate());//只有一个商户号，二个证书同一样数据
        gatewayConfigRepository.save(payGatewayConfig);
    }

    /**
     * 保存支付配置
     *
     * @param saveRequest
     */
    @Transactional
    public void savePayGatewayByTerminalType(PayGatewaySaveByTerminalTypeRequest saveRequest) {
        PayGatewayConfig config = this.queryConfigByNameAndStoreId(saveRequest.getPayGatewayEnum(), saveRequest.getStoreId());
        if(config == null){
            config = new PayGatewayConfig();
            config.setStoreId(saveRequest.getStoreId());
            config.setPayGateway(gatewayRepository.queryByNameAndStoreId(saveRequest.getPayGatewayEnum(), saveRequest.getStoreId()));
        }
        if(TerminalType.APP.equals(saveRequest.getTerminalType())) {
            config.setOpenPlatformAppId(saveRequest.getAppId());
            config.setOpenPlatformSecret(saveRequest.getSecret());
            gatewayConfigRepository.save(config);
        }else if(TerminalType.H5.equals(saveRequest.getTerminalType())){
            config.setAppId(saveRequest.getAppId());
            config.setSecret(saveRequest.getSecret());
            gatewayConfigRepository.save(config);
        }
    }
}
