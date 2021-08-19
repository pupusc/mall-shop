package com.wanmi.sbc.setting.audit;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.request.ConfigContextModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.GoodsDisplayConfigGetResponse;
import com.wanmi.sbc.setting.bean.dto.TradeConfigDTO;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigRepository;
import com.wanmi.sbc.setting.util.SpecificationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 审核管理service
 * Created by CHENLI on 2017/5/12.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class AuditService {

    @Resource
    private ConfigRepository configRepository;

    /**
     * 自营商品是否需审核
     *
     * @return true:审核 false:不审核
     */
    public boolean isBossGoodsAudit() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.S2BAUDIT.toString());
        request.setConfigType(ConfigType.BOSSGOODSAUDIT.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        if (CollectionUtils.isEmpty(configList)) {
            return false;
        }
        return Constants.yes.equals(configList.get(0).getStatus());
    }

    /**
     * 商家商品是否需审核
     *
     * @return true:审核 false:不审核
     */
    public boolean isSupplierGoodsAudit() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.S2BAUDIT.toString());
        request.setConfigType(ConfigType.SUPPLIERGOODSAUDIT.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        return CollectionUtils.isNotEmpty(configList) && Constants.yes.equals(configList.get(0).getStatus());
    }


    /**
     * 商家订单是否需审核
     *
     * @return true:审核 false:不审核
     */
    public boolean isSupplierOrderAudit() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.S2BAUDIT.toString());
        request.setConfigType(ConfigType.ORDERAUDIT.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        return CollectionUtils.isNotEmpty(configList) && Constants.yes.equals(configList.get(0).getStatus());
    }

    /**
     * 客户审核是否需审核
     *
     * @return true:审核 false:不审核
     */
    public boolean isCustomerAudit() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.S2BAUDIT.toString());
        request.setConfigType(ConfigType.CUSTOMERAUDIT.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        return CollectionUtils.isNotEmpty(configList) && Constants.yes.equals(configList.get(0).getStatus());
    }

    /**
     * 增专资质审核开关
     *
     * @return true:审核 false:不审核
     */
    public boolean isTicketAudit() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.S2BAUDIT.toString());
        request.setConfigType(ConfigType.TICKETAUDIT.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        return CollectionUtils.isNotEmpty(configList) && Constants.yes.equals(configList.get(0).getStatus());
    }

    /**
     * 客户信息完善 开关
     *
     * @return 0 true:需要完善信息 1 false:不需要完善信息
     */
    public boolean isPerfectCustomerInfo() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.S2BAUDIT.toString());
        request.setConfigType(ConfigType.CUSTOMERINFOAUDIT.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        return CollectionUtils.isNotEmpty(configList) && Constants.yes.equals(configList.get(0).getStatus());
    }



    /**
     * 查询审核开关状态
     *
     * @return configs
     */
    public List<ConfigVO> listAuditConfigs() {
        List<Config> configs = configRepository.findByConfigKeyAndDelFlag(ConfigKey.S2BAUDIT.toString(), DeleteFlag.NO);

        List<ConfigVO> configVOList = new ArrayList<>();

        KsBeanUtil.copyList(configs, configVOList);
        return configVOList;
    }

    /**
     * 根据type和key更新status，如果是商品审核关闭，会同步关闭自营商品审核开关
     *
     * @param configKey  key
     * @param configType type
     * @param status     status
     */
    @Transactional
    public void updateStatusByTypeAndKey(ConfigKey configKey, ConfigType configType, Integer status) {
        // 如果是打开自营商品审核，前提是商品审核开关打开
        if (configKey == ConfigKey.S2BAUDIT && configType == ConfigType.BOSSGOODSAUDIT && status == 1) {
            // 查询商品审核开关状态
            Config goodsAudit = configRepository.findByConfigTypeAndDelFlag(ConfigType.SUPPLIERGOODSAUDIT.toValue(),
                    DeleteFlag.NO);
            if (null != goodsAudit && goodsAudit.getStatus() == 0) {
                throw new SbcRuntimeException(SettingErrorCode.AUDIT_GOODS_CLOSED);
            }
        }

        // 如果打开客户审核，前提是要打开客户信息完善开关
        if (configKey == ConfigKey.S2BAUDIT && configType == ConfigType.CUSTOMERAUDIT && status == 1) {
            //查询客户信息完善开关
            Config customerAudit = configRepository.findByConfigTypeAndDelFlag(ConfigType.CUSTOMERINFOAUDIT.toValue(),
                    DeleteFlag.NO);
            if (Objects.nonNull(customerAudit) && customerAudit.getStatus() == 0) {
                throw new SbcRuntimeException(SettingErrorCode.CUSTOMER_INFO_CLOSED);
            }
        }

        configRepository.updateStatusByTypeAndConfigKey(configType.toValue(), configKey.toString(), status, null);

        // 如果是商品审核关闭，同步关闭自营商品审核开关
        if (configKey == ConfigKey.S2BAUDIT && configType == ConfigType.SUPPLIERGOODSAUDIT && status == 0) {
            configRepository.updateStatusByTypeAndConfigKey(ConfigType.BOSSGOODSAUDIT.toValue(), ConfigKey.S2BAUDIT
                    .toString(), 0, null);
        }

        // 如果客户信息完善关闭，同步关闭客户审核开关
        if (configKey == ConfigKey.S2BAUDIT && configType == ConfigType.CUSTOMERINFOAUDIT && status == 0) {
            configRepository.updateStatusByTypeAndConfigKey(ConfigType.CUSTOMERAUDIT.toValue(), ConfigKey.S2BAUDIT
                    .toString(), 0, null);
        }
    }

    /**
     * 更新商品评价开关
     * @param configKey
     * @param configType
     * @param status
     * @return
     */
    @CacheEvict(value = "GOODS_EVALUATE_CONFIG",allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int modifyGoodsEvaluateSetting(ConfigKey configKey, ConfigType configType, Integer status){
        return configRepository.updateStatusByTypeAndConfigKey(configType.toValue(), configKey.toString(), status, null);
    }

    /**
     * 保存增专资质配置
     *
     * @param status
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int modifyInvoiceConfig(Integer status) {
        return configRepository.updateStatusByType(ConfigType.TICKETAUDIT.toValue(), status, LocalDateTime.now());
    }

    /**
     * 查询增专资质配置
     *
     * @return
     */
    public Config getInvoiceConfig() {
        return configRepository.findByConfigTypeAndDelFlag(ConfigType.TICKETAUDIT.toValue(), DeleteFlag.NO);
    }

    /**
     * 查询订单设置列表
     *
     * @return
     */
    public List<Config> listTradeConfig() {
        return configRepository.findByConfigKeyAndDelFlag(ConfigKey.ORDERSETTING.toString(), DeleteFlag.NO);
    }

    /**
     * 修改订单设置
     *
     * @param tradeConfigDTOList
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyTradeConfigs(List<TradeConfigDTO> tradeConfigDTOList) {
        tradeConfigDTOList.forEach(tradeSettingRequest -> configRepository
                .updateStatusByTypeAndConfigKey(tradeSettingRequest.getConfigType().toString(),
                        tradeSettingRequest.getConfigKey().toString(), tradeSettingRequest.getStatus(),
                        tradeSettingRequest.getContext()));
    }

    /**
     * 根据type获取订单设置
     *
     * @param configType
     * @return
     */
    public Config getTradeConfigByType(ConfigType configType) {
        return configRepository.findByConfigTypeAndDelFlag(configType.toValue(), DeleteFlag.NO);
    }

    /**
     * 查询订单代发货自动收货配置
     *
     * @return
     */
    public Config getOrderAutoReceiveConfig() {
        return configRepository.findByConfigTypeAndDelFlag(ConfigType.ORDER_SETTING_AUTO_RECEIVE.toString(),
                DeleteFlag.NO);
    }

    /**
     * 获取用户设置
     *
     * @return
     */
    public boolean isUserAudit() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.S2BAUDIT.toString());
        request.setConfigType(ConfigType.USERAUDIT.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        return CollectionUtils.isNotEmpty(configList) && Constants.yes.equals(configList.get(0).getStatus());
    }

    /**
     * 返回大小图和SPU,SKU展示维度设置
     * requestSource 0是pc 1是移动端
     */
    public GoodsDisplayConfigGetResponse getGoodsDisplay(int requestSource) {
        GoodsDisplayConfigGetResponse response = new GoodsDisplayConfigGetResponse();
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.S2BAUDIT.toString());
        if (requestSource == 0) {
            request.setConfigType(ConfigType.PC_GOODS_IMAGE_SWITCH.toValue());
        } else {
            request.setConfigType(ConfigType.MOBILE_GOODS_IMAGE_SWITCH.toValue());
        }
        List<Config> imageConfigList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        Optional<Config> imgConfigOptional = imageConfigList.stream().findFirst();
        if (imgConfigOptional.isPresent()) {
            Config config = imgConfigOptional.get();
            response.setImageShowType(config.getStatus());
        }
        if (requestSource == 0) {
            request.setConfigType(ConfigType.PC_GOODS_SPEC_SWITCH.toValue());
        } else {
            request.setConfigType(ConfigType.MOBILE_GOODS_SPEC_SWITCH.toValue());
        }
        List<Config> goodsConfigList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        Optional<Config> goodsConfigOptional = goodsConfigList.stream().findFirst();
        if (goodsConfigOptional.isPresent()) {
            Config config = goodsConfigOptional.get();
            response.setGoodsShowType(config.getStatus());
        }
        return response;
    }

    /**
     * 修改小程序分享配置
     *
     * @param request
     */
    @Transactional
    public void modifyShareLittleProgram(ConfigContextModifyByTypeAndKeyRequest request){
        configRepository.updateStatusByTypeAndConfigKey(request.getConfigType().toValue(), request.getConfigKey().toString(), null, request.getContext());
    }

    /**
     * 根据配置的键查询配置
     * @param configKey 配置的键
     * @return
     */
    public List<Config> findByConfigKey(String configKey) {
        return configRepository.findByConfigKeyAndDelFlag(configKey, DeleteFlag.NO);
    }

    /**
     * 修改订单设置
     *
     * @param configRequestList
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyConfigList(List<ConfigVO> configRequestList) {
        configRequestList.forEach(configRequest -> configRepository
                .updateStatusByTypeAndConfigKey(configRequest.getConfigType(),
                        configRequest.getConfigKey(), configRequest.getStatus(),
                        configRequest.getContext()));
    }

    /**
     * 是否开启商品审核
     *
     * @return true:开启 false:不开启
     */
    @Cacheable(value = "GOODS_EVALUATE_CONFIG")
    public boolean isGoodsEvaluate() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.GOODS_SETTING.toString());
        request.setConfigType(ConfigType.GOODS_EVALUATE_SETTING.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        if (CollectionUtils.isEmpty(configList)) {
            return false;
        }
        return Constants.yes.equals(configList.get(0).getStatus());
    }
}
