package com.wanmi.sbc.setting.config;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.ConfigContextModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.ConfigUpdateRequest;
import com.wanmi.sbc.setting.api.request.systemconfig.ConfigModifyRopRequest;
import com.wanmi.sbc.setting.api.request.systemconfig.LogisticsSaveRopRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.setting.util.SpecificationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by feitingting on 2019/11/6.
 */

@Service
/**
 * 系统配置类
 */
public class ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    /**
     * 根据key查询配置信息
     *
     * @param configKey
     * @param delFlag
     * @return
     */
    public List<Config> findByConfigKeyAndDelFlag(String configKey, DeleteFlag delFlag) {
        //只做基本的查询，不带出具体的context信息
        List<Config> configs = configRepository.findByConfigKeyAndDelFlag(configKey, delFlag);
        if ("liveSwitch".equals(configKey)&&CollectionUtils.isEmpty(configs)){
            Config config =new Config();
            config.setConfigKey("liveSwitch");
            config.setConfigType("liveSwitch");
            config.setStatus(0);
            config.setCreateTime(LocalDateTime.now());
            config.setDelFlag(DeleteFlag.NO);
            config.setConfigName("小程序直播");
            configRepository.save(config);
            configs.add(config);
            return  configs;
        }
        return configs.stream().map(config -> {
            config.setContext(null);
            return config;
        }).collect(Collectors.toList());

    }


    @Transactional(rollbackFor = Exception.class)
    public void save(LogisticsSaveRopRequest request) {
        //id必须存在，否则无法更新
        if (request.getConfigId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //查询存不存在更新
        Config oldConfig = configRepository.findById(request.getConfigId()).orElse(null);
        if (Objects.isNull(oldConfig)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //快递100配置--先转Json字符串
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deliveryKey", request.getDeliveryKey());
        jsonObject.put("customerKey", request.getCustomerKey());
        jsonObject.put("callBackUrl", request.getCallBackUrl());
        jsonObject.put("subscribeStatus", request.getSubscribeStatus());
        //将转化后的结果作为context信息拷贝到oldConfig上, 其他的不变
        oldConfig.setContext(jsonObject.toJSONString());
        oldConfig.setUpdateTime(LocalDateTime.now());
        oldConfig.setStatus(request.getStatus());
        configRepository.save(oldConfig);
    }


    public SystemConfigTypeResponse findByConfigTypeAndDelFlag(String configType, DeleteFlag deleteFlag) {
        Config config = configRepository.findByConfigTypeAndDelFlag(configType, deleteFlag);
        SystemConfigTypeResponse response = new SystemConfigTypeResponse();
        response.setConfig(wrapperVo(config));
        return response;
    }


    public LogisticsRopResponse findKuaiDiConfig(String configType, DeleteFlag deleteFlag) {
        LogisticsRopResponse response = new LogisticsRopResponse();
        Config config = configRepository.findByConfigTypeAndDelFlag(configType, deleteFlag);
        if (Objects.nonNull(config)) {
            JSONObject configJson = JSONObject.parseObject(config.getContext());
            String deliveryKey = configJson.getString("deliveryKey");
            String customerKey = configJson.getString("customerKey");
            response = LogisticsRopResponse.builder().configId(config.getId())
                    .callBackUrl(configJson.getString("callBackUrl"))
                    .subscribeStatus(NumberUtils.toInt(configJson.getString("subscribeStatus")))
                    .status(config.getStatus())
                    .customerKey(customerKey)
                    .deliveryKey(deliveryKey).build();
        }
        return response;
    }

    /**
     * 订单列表展示设置
     *
     * @return 0:订单精简 1:订单明细
     */
    @Transactional
    public Integer queryOrderListShowType() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setConfigKey(ConfigKey.ORDER_LIST_SHOW_TYPE.toString());
        request.setConfigType(ConfigType.ORDER_LIST_SHOW_TYPE.toValue());
        List<Config> configList = configRepository.findAll(SpecificationUtil.getWhereCriteria(request));
        if (CollectionUtils.isEmpty(configList)) {
            Config config = new Config();
            config.setConfigKey(ConfigKey.ORDER_LIST_SHOW_TYPE.toString());
            config.setConfigType(ConfigType.ORDER_LIST_SHOW_TYPE.toValue());
            config.setStatus(0);
            config.setDelFlag(DeleteFlag.NO);
            config.setConfigName("订单列表展示设置");
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());
            configRepository.save(config);
            return config.getStatus();
        }
        return configList.get(0).getStatus();
    }

    /**
     * 订单列表展示设置更新
     * @param status 状态
     */
    @Transactional
    public void modifyOrderListShowType(Integer status){
        configRepository.updateStatusByType(ConfigType.ORDER_LIST_SHOW_TYPE.toValue(), status, LocalDateTime.now());
    }


    public ConfigVO wrapperVo(Config config) {
        if (config != null) {
            ConfigVO configVO = new ConfigVO();
            KsBeanUtil.copyPropertiesThird(config, configVO);
            return configVO;
        }
        return null;
    }

    /**
     * 修改直播开关
     * @param request
     */
    public void update(ConfigUpdateRequest request) {

        configRepository.updateStatusConfigKey(request.getConfigKey(),request.getStatus());
    }
}
