package com.wanmi.sbc.setting.provider.impl.systemconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.systemconfig.SystemConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigTypeResponse;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigService;
import com.wanmi.sbc.setting.systemconfig.model.root.SystemConfig;
import com.wanmi.sbc.setting.systemconfig.service.SystemConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by feitingting on 2019/11/6.
 */
@RestController
public class SystemConfigQueryController implements SystemConfigQueryProvider {

    @Autowired
    ConfigService configService;
    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public BaseResponse<SystemConfigResponse> findByConfigKeyAndDelFlag(@RequestBody @Valid ConfigQueryRequest request){
        List<Config> configList = configService.findByConfigKeyAndDelFlag(request.getConfigKey(), DeleteFlag.NO);
        if (CollectionUtils.isEmpty(configList)){
            return BaseResponse.success(new SystemConfigResponse());
        }
        List<ConfigVO> configVOList = KsBeanUtil.convert(configList, ConfigVO.class);
        return BaseResponse.success(new SystemConfigResponse(configVOList, null));
    }

    @Override
    public BaseResponse<SystemConfigTypeResponse> findByConfigTypeAndDelFlag(@RequestBody @Valid ConfigQueryRequest request) {
        return BaseResponse.success(configService.findByConfigTypeAndDelFlag(request.getConfigType(),DeleteFlag.NO));
    }

    @Override
    public BaseResponse<LogisticsRopResponse> findKuaiDiConfig(@RequestBody ConfigQueryRequest request){
        return BaseResponse.success(configService.findKuaiDiConfig(request.getConfigType(),DeleteFlag.NO));
    }

    @Override
    public BaseResponse<SystemConfigResponse> list(@Valid SystemConfigQueryRequest request) {
        List<SystemConfig> systemConfigList = systemConfigService.list(request);
        List<SystemConfigVO> systemConfigVOList =
                systemConfigList.stream().map(systemConfig -> systemConfigService.wrapperVo(systemConfig)).collect(Collectors.toList());
        return BaseResponse.success(new SystemConfigResponse(null, systemConfigVOList));
    }

}

