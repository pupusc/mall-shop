package com.wanmi.sbc.setting.provider.impl.storeresource;

import com.wanmi.osd.OsdClient;
import com.wanmi.osd.bean.OsdClientParam;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.storeresource.StoreResourceQueryProvider;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceByIdRequest;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceListRequest;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourcePageRequest;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceQueryRequest;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourceByIdResponse;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourceListResponse;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourcePageResponse;
import com.wanmi.sbc.setting.bean.vo.StoreResourceVO;
import com.wanmi.sbc.setting.storeresource.model.root.StoreResource;
import com.wanmi.sbc.setting.storeresource.service.StoreResourceService;
import com.wanmi.sbc.setting.systemconfig.model.root.SystemConfig;
import com.wanmi.sbc.setting.systemconfig.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺资源库查询服务接口实现</p>
 *
 * @author lq
 * @date 2019-11-05 16:12:49
 */
@RestController
@Validated
public class StoreResourceQueryController implements StoreResourceQueryProvider {
    @Autowired
    private StoreResourceService storeResourceService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public BaseResponse<StoreResourcePageResponse> page(@RequestBody @Valid StoreResourcePageRequest storeResourcePageReq) {
        StoreResourceQueryRequest queryReq = new StoreResourceQueryRequest();
        KsBeanUtil.copyPropertiesThird(storeResourcePageReq, queryReq);
        queryReq.setDelFlag(DeleteFlag.NO);
        queryReq.putSort("createTime", SortType.DESC.toValue());
        queryReq.putSort("resourceId", SortType.ASC.toValue());
        // 查询可用云服务
        SystemConfig availableYun = systemConfigService.getAvailableYun();
        OsdClientParam osdClientParam = OsdClientParam.builder()
                .configType(availableYun.getConfigType())
                .context(availableYun.getContext())
                .build();
        Page<StoreResource> storeResourcePage = storeResourceService.page(queryReq);
        Page<StoreResourceVO> newPage = storeResourcePage.map(entity -> {
                    //获取url
                    String resourceUrl = OsdClient.instance().getResourceUrl(osdClientParam, entity.getArtworkUrl());
                    entity.setArtworkUrl(resourceUrl);
                    return storeResourceService.wrapperVo(entity);
                }
        );
        MicroServicePage<StoreResourceVO> microPage = new MicroServicePage<>(newPage, storeResourcePageReq.getPageable());
        StoreResourcePageResponse finalRes = new StoreResourcePageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<StoreResourceListResponse> list(@RequestBody @Valid StoreResourceListRequest storeResourceListReq) {
        StoreResourceQueryRequest queryReq = new StoreResourceQueryRequest();
        KsBeanUtil.copyPropertiesThird(storeResourceListReq, queryReq);
        List<StoreResource> storeResourceList = storeResourceService.list(queryReq);
        List<StoreResourceVO> newList = storeResourceList.stream().map(entity -> storeResourceService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new StoreResourceListResponse(newList));
    }

    @Override
    public BaseResponse<StoreResourceByIdResponse> getById(@RequestBody @Valid StoreResourceByIdRequest storeResourceByIdRequest) {
        StoreResource storeResource = storeResourceService.getById(storeResourceByIdRequest.getResourceId());
        return BaseResponse.success(new StoreResourceByIdResponse(storeResourceService.wrapperVo(storeResource)));
    }

}

