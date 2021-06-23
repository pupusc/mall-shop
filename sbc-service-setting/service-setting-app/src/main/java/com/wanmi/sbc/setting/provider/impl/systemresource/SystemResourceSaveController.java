package com.wanmi.sbc.setting.provider.impl.systemresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.systemresource.SystemResourceSaveProvider;
import com.wanmi.sbc.setting.api.request.systemresource.*;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceAddResponse;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceEditResponse;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceModifyResponse;
import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import com.wanmi.sbc.setting.systemresource.model.root.SystemResource;
import com.wanmi.sbc.setting.systemresource.service.SystemResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>平台素材资源保存服务接口实现</p>
 *
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@RestController
@Validated
public class SystemResourceSaveController implements SystemResourceSaveProvider {

    @Autowired
    private SystemResourceService systemResourceService;

    @Override
    public BaseResponse<SystemResourceAddResponse> add(@RequestBody @Valid SystemResourceAddRequest systemResourceAddRequest) {
        SystemResource systemResource = new SystemResource();
        KsBeanUtil.copyPropertiesThird(systemResourceAddRequest, systemResource);
        return BaseResponse.success(new SystemResourceAddResponse(
                systemResourceService.wrapperVo(systemResourceService.add(systemResource))));
    }

    @Override
    public BaseResponse<SystemResourceModifyResponse> modify(@RequestBody @Valid SystemResourceModifyRequest systemResourceModifyRequest) {
        SystemResource systemResource = new SystemResource();
        KsBeanUtil.copyPropertiesThird(systemResourceModifyRequest, systemResource);
        return BaseResponse.success(new SystemResourceModifyResponse(
                systemResourceService.wrapperVo(systemResourceService.modify(systemResource))));
    }


    @Override
    public BaseResponse<SystemResourceEditResponse> move(@RequestBody @Valid SystemResourceMoveRequest
                                     moveRequest) {
        systemResourceService.updateCateByIds(moveRequest.getCateId(), moveRequest.getResourceIds());
        List<SystemResourceVO> systemResourceVOList = systemResourceService.findByIdList(moveRequest.getResourceIds());
        return BaseResponse.success(SystemResourceEditResponse.builder().systemResourceVOList(systemResourceVOList).build());
    }


    @Override
    public BaseResponse deleteById(@RequestBody @Valid SystemResourceDelByIdRequest systemResourceDelByIdRequest) {
        systemResourceService.deleteById(systemResourceDelByIdRequest.getResourceId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<SystemResourceEditResponse> deleteByIdList(@RequestBody @Valid SystemResourceDelByIdListRequest systemResourceDelByIdListRequest) {
        List<SystemResourceVO> delete = systemResourceService.delete(systemResourceDelByIdListRequest.getResourceIds());
        SystemResourceEditResponse res = SystemResourceEditResponse.builder().systemResourceVOList(delete).build();
        return BaseResponse.success(res);
    }

}

