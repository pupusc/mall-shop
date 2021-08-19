package com.wanmi.sbc.setting.api.provider.systemresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.systemresource.*;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceAddResponse;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceEditResponse;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>平台素材资源保存服务Provider</p>
 *
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemResourceSaveProvider")
public interface SystemResourceSaveProvider {

    /**
     * 新增平台素材资源API
     *
     * @param systemResourceAddRequest 平台素材资源新增参数结构 {@link SystemResourceAddRequest}
     * @return 新增的平台素材资源信息 {@link SystemResourceAddResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresource/add")
    BaseResponse<SystemResourceAddResponse> add(@RequestBody @Valid SystemResourceAddRequest systemResourceAddRequest);

    /**
     * 修改平台素材资源API
     *
     * @param systemResourceModifyRequest 平台素材资源修改参数结构 {@link SystemResourceModifyRequest}
     * @return 修改的平台素材资源信息 {@link SystemResourceModifyResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresource/modify")
    BaseResponse<SystemResourceModifyResponse> modify(@RequestBody @Valid SystemResourceModifyRequest
                                                              systemResourceModifyRequest);

    /**
     * 移动平台素材资源API
     *
     * @param moveRequest 平台素材资源修改参数结构 {@link SystemResourceMoveRequest}
     * @return 修改的平台素材资源信息 {@link SystemResourceModifyResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresource/move")
    BaseResponse<SystemResourceEditResponse> move(@RequestBody @Valid SystemResourceMoveRequest
                                moveRequest);

    /**
     * 单个删除平台素材资源API
     *
     * @param systemResourceDelByIdRequest 单个删除参数结构 {@link SystemResourceDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresource/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid SystemResourceDelByIdRequest systemResourceDelByIdRequest);

    /**
     * 批量删除平台素材资源API
     *
     * @param systemResourceDelByIdListRequest 批量删除参数结构 {@link SystemResourceDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresource/delete-by-id-list")
    BaseResponse<SystemResourceEditResponse> deleteByIdList(@RequestBody @Valid SystemResourceDelByIdListRequest systemResourceDelByIdListRequest);

}

