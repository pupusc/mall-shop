package com.wanmi.sbc.setting.provider.impl.systemresourcecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.systemresourcecate.SystemResourceCateQueryProvider;
import com.wanmi.sbc.setting.api.request.systemresourcecate.*;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateByIdResponse;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateListResponse;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCatePageResponse;
import com.wanmi.sbc.setting.bean.vo.SystemResourceCateVO;
import com.wanmi.sbc.setting.systemresourcecate.model.root.SystemResourceCate;
import com.wanmi.sbc.setting.systemresourcecate.service.SystemResourceCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>平台素材资源分类查询服务接口实现</p>
 *
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@RestController
@Validated
public class SystemResourceCateQueryController implements SystemResourceCateQueryProvider {
    @Autowired
    private SystemResourceCateService systemResourceCateService;

    @Override
    public BaseResponse<SystemResourceCatePageResponse> page(@RequestBody @Valid SystemResourceCatePageRequest
																		 systemResourceCatePageReq) {
        SystemResourceCateQueryRequest queryReq = new SystemResourceCateQueryRequest();
        KsBeanUtil.copyPropertiesThird(systemResourceCatePageReq, queryReq);
        Page<SystemResourceCate> systemResourceCatePage = systemResourceCateService.page(queryReq);
        Page<SystemResourceCateVO> newPage = systemResourceCatePage.map(entity -> systemResourceCateService.wrapperVo
				(entity));
        MicroServicePage<SystemResourceCateVO> microPage = new MicroServicePage<>(newPage, systemResourceCatePageReq
				.getPageable());
        SystemResourceCatePageResponse finalRes = new SystemResourceCatePageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<SystemResourceCateListResponse> list(@RequestBody @Valid SystemResourceCateListRequest
																		 systemResourceCateListReq) {
        SystemResourceCateQueryRequest queryReq = new SystemResourceCateQueryRequest();
        KsBeanUtil.copyPropertiesThird(systemResourceCateListReq, queryReq);
        queryReq.putSort("cateId", SortType.ASC.toValue());
        queryReq.putSort("createTime", SortType.DESC.toValue());
        queryReq.setDelFlag(DeleteFlag.NO);
        List<SystemResourceCate> systemResourceCateList = systemResourceCateService.list(queryReq);


        List<SystemResourceCateVO> newList = systemResourceCateList.stream().map(entity -> systemResourceCateService
				.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new SystemResourceCateListResponse(newList));
    }

    @Override
    public BaseResponse<SystemResourceCateByIdResponse> getById(@RequestBody @Valid SystemResourceCateByIdRequest
																			systemResourceCateByIdRequest) {
        SystemResourceCate systemResourceCate = systemResourceCateService.getById(systemResourceCateByIdRequest
				.getCateId());
        return BaseResponse.success(new SystemResourceCateByIdResponse(systemResourceCateService.wrapperVo
				(systemResourceCate)));
    }

    @Override
    public BaseResponse<Integer> checkChild(@RequestBody @Valid SystemResourceCateCheckChildRequest
                                                    request) {
        return BaseResponse.success(systemResourceCateService.checkChild(request.getCateId()));
    }


    @Override
    public BaseResponse<Integer> checkResource(@RequestBody @Valid SystemResourceCateCheckResourceRequest
                                                    request) {
        return BaseResponse.success(systemResourceCateService.checkResource(request.getCateId()));
    }

}

