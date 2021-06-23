package com.wanmi.sbc.setting.provider.impl.systemresource;

import com.wanmi.osd.OsdClient;
import com.wanmi.osd.bean.OsdClientParam;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.systemresource.SystemResourceQueryProvider;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceByIdRequest;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceListRequest;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourcePageRequest;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceQueryRequest;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceByIdResponse;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceListResponse;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourcePageResponse;
import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import com.wanmi.sbc.setting.systemconfig.model.root.SystemConfig;
import com.wanmi.sbc.setting.systemconfig.service.SystemConfigService;
import com.wanmi.sbc.setting.systemresource.model.root.SystemResource;
import com.wanmi.sbc.setting.systemresource.service.SystemResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>平台素材资源查询服务接口实现</p>
 *
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@RestController
@Validated
public class SystemResourceQueryController implements SystemResourceQueryProvider {
    @Autowired
    private SystemResourceService systemResourceService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public BaseResponse<SystemResourcePageResponse> page(@RequestBody @Valid SystemResourcePageRequest systemResourcePageReq) {
        SystemResourceQueryRequest queryReq = new SystemResourceQueryRequest();
        KsBeanUtil.copyPropertiesThird(systemResourcePageReq, queryReq);
        queryReq.setDelFlag(DeleteFlag.NO);
        queryReq.putSort("createTime", SortType.DESC.toValue());
        queryReq.putSort("resourceId", SortType.ASC.toValue());
        // 查询可用云服务
        SystemConfig availableYun = systemConfigService.getAvailableYun();
        OsdClientParam osdClientParam = OsdClientParam.builder()
                .configType(availableYun.getConfigType())
                .context(availableYun.getContext())
                .build();
        Page<SystemResource> systemResourcePage = systemResourceService.page(queryReq);
        Page<SystemResourceVO> newPage = systemResourcePage.map(entity -> {
                    //获取url
                    String resourceUrl = OsdClient.instance().getResourceUrl(osdClientParam, entity.getArtworkUrl());
                    entity.setArtworkUrl(resourceUrl);
                    return systemResourceService.wrapperVo(entity);
                }
        );
        MicroServicePage<SystemResourceVO> microPage = new MicroServicePage<>(newPage, systemResourcePageReq.getPageable());
        SystemResourcePageResponse finalRes = new SystemResourcePageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<SystemResourceListResponse> list(@RequestBody @Valid SystemResourceListRequest systemResourceListReq) {
        SystemResourceQueryRequest queryReq = new SystemResourceQueryRequest();
        KsBeanUtil.copyPropertiesThird(systemResourceListReq, queryReq);
        List<SystemResource> systemResourceList = systemResourceService.list(queryReq);
        List<SystemResourceVO> newList = systemResourceList.stream().map(entity -> systemResourceService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new SystemResourceListResponse(newList));
    }

    @Override
    public BaseResponse<SystemResourceByIdResponse> getById(@RequestBody @Valid SystemResourceByIdRequest systemResourceByIdRequest) {
        SystemResource systemResource = systemResourceService.getById(systemResourceByIdRequest.getResourceId());
        return BaseResponse.success(new SystemResourceByIdResponse(systemResourceService.wrapperVo(systemResource)));
    }

}

