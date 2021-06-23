package com.wanmi.sbc.setting.provider.impl.yunservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.systemconfig.SystemConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.yunservice.*;
import com.wanmi.sbc.setting.api.response.yunservice.*;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import com.wanmi.sbc.setting.systemconfig.model.root.SystemConfig;
import com.wanmi.sbc.setting.systemconfig.service.SystemConfigService;
import com.wanmi.sbc.setting.yunservice.YunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>系统配置表查询服务接口实现</p>
 *
 * @author yang
 * @date 2019-11-05 18:33:04
 */
@RestController
@Validated
public class YunServiceController implements YunServiceProvider {
    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private YunService yunService;

    @Override
    public BaseResponse<YunConfigListResponse> list(@RequestBody @Valid YunConfigListRequest yunConfigListRequest) {
        SystemConfigQueryRequest queryReq = new SystemConfigQueryRequest();
        KsBeanUtil.copyPropertiesThird(yunConfigListRequest, queryReq);
        List<SystemConfig> systemConfigList = systemConfigService.list(queryReq);
        List<SystemConfigVO> newList = systemConfigList.stream().map(entity -> systemConfigService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new YunConfigListResponse(newList));
    }

    @Override
    public BaseResponse<YunConfigResponse> getById(@RequestBody @Valid YunConfigByIdRequest yunConfigByIdRequest) {
        SystemConfig systemConfig = systemConfigService.getById(yunConfigByIdRequest.getId());
        YunConfigResponse yunConfigResponse = JSONObject.toJavaObject(JSON.parseObject(systemConfig.getContext()), YunConfigResponse.class);
        KsBeanUtil.copyPropertiesThird(systemConfig, yunConfigResponse);
        return BaseResponse.success(yunConfigResponse);
    }

    @Override
    public BaseResponse<YunAvailableConfigResponse> getAvailableYun() {
        SystemConfig systemConfig = systemConfigService.getAvailableYun();
        YunAvailableConfigResponse configResponse = new YunAvailableConfigResponse();
        KsBeanUtil.copyPropertiesThird(systemConfig, configResponse);
        return BaseResponse.success(configResponse);
    }

    @Override
    public BaseResponse<YunConfigResponse> modify(@RequestBody @Valid YunConfigModifyRequest yunConfigModifyRequest) {
        SystemConfig systemConfig = systemConfigService.getById(yunConfigModifyRequest.getId());
        Integer status = yunConfigModifyRequest.getStatus();
        // 启用，禁用其他云
        if (Objects.nonNull(status) && !status.equals(systemConfig.getStatus()) && status.equals(EnableStatus.ENABLE.toValue())) {
            YunConfigResponse yunConfig = JSONObject.toJavaObject(JSON.parseObject(systemConfig.getContext()), YunConfigResponse.class);
            if (Objects.isNull(yunConfig.getAccessKeySecret()) ||
                    Objects.isNull(yunConfig.getBucketName()) ||
                    Objects.isNull(yunConfig.getEndPoint()) ||
                    Objects.isNull(yunConfig.getAccessKeyId())) {
                throw new SbcRuntimeException("K-061003");
            }
            List<SystemConfig> systemConfigList = systemConfigService.list(SystemConfigQueryRequest.builder()
                    .configKey(ConfigKey.RESOURCESERVER.toString())
                    .delFlag(DeleteFlag.NO)
                    .build());
            systemConfigList.forEach(config -> {
                config.setStatus(EnableStatus.DISABLE.toValue());
                systemConfigService.modify(config);
            });
        }
        KsBeanUtil.copyProperties(yunConfigModifyRequest, systemConfig);
        YunConfigResponse yunConfigResponse = new YunConfigResponse();
        KsBeanUtil.copyPropertiesThird(systemConfigService.modify(systemConfig), yunConfigResponse);
        return BaseResponse.success(yunConfigResponse);
    }

    @Override
    public BaseResponse<YunUploadResourceResponse> uploadFile(@RequestBody @Valid YunUploadResourceRequest request) {
        return BaseResponse.success(yunService.uploadFile(request));
    }

    @Override
    public BaseResponse<YunGetResourceResponse> getFile(@RequestBody @Valid YunGetResourceRequest yunGetResourceRequest) {
        byte[] file = yunService.getFile(yunGetResourceRequest.getResourceKey());
        return BaseResponse.success(YunGetResourceResponse.builder().content(file).build());
    }

    @Override
    public BaseResponse<String> uploadFileExcel(@RequestBody @Valid YunUploadResourceRequest request) {
        String resourceUrl = yunService.uploadFileExcel(request);
        return BaseResponse.success(resourceUrl);
    }

    @Override
    public BaseResponse deleteFile(@RequestBody @Valid YunFileDeleteRequest yunFileDeleteRequest) {
        yunService.deleteResources(yunFileDeleteRequest.getResourceKeys());
        return BaseResponse.SUCCESSFUL();
    }
}

