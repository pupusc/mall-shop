package com.wanmi.sbc.setting.api.provider.systemresourcecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.systemresourcecate.*;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateByIdResponse;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateListResponse;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCatePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>平台素材资源分类查询服务Provider</p>
 *
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemResourceCateQueryProvider")
public interface SystemResourceCateQueryProvider {

    /**
     * 分页查询平台素材资源分类API
     *
     * @param systemResourceCatePageReq 分页请求参数和筛选对象 {@link SystemResourceCatePageRequest}
     * @return 平台素材资源分类分页列表信息 {@link SystemResourceCatePageResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresourcecate/page")
    BaseResponse<SystemResourceCatePageResponse> page(@RequestBody @Valid SystemResourceCatePageRequest
                                                              systemResourceCatePageReq);

    /**
     * 列表查询平台素材资源分类API
     *
     * @param systemResourceCateListReq 列表请求参数和筛选对象 {@link SystemResourceCateListRequest}
     * @return 平台素材资源分类的列表信息 {@link SystemResourceCateListResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresourcecate/list")
    BaseResponse<SystemResourceCateListResponse> list(@RequestBody @Valid SystemResourceCateListRequest
															  systemResourceCateListReq);

    /**
     * 单个查询平台素材资源分类API
     *
     * @param systemResourceCateByIdRequest 单个查询平台素材资源分类请求参数 {@link SystemResourceCateByIdRequest}
     * @return 平台素材资源分类详情 {@link SystemResourceCateByIdResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresourcecate/get-by-id")
    BaseResponse<SystemResourceCateByIdResponse> getById(@RequestBody @Valid SystemResourceCateByIdRequest
																 systemResourceCateByIdRequest);


    /**
     * 验证是否有子类
     *
     * @param systemResourceCateCheckChildRequest 验证是否有子类 {@link SystemResourceCateCheckChildRequest}
     * @return 验证是否有子类 {@link SystemResourceCateListResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresourcecate/check-child")
    BaseResponse<Integer> checkChild(@RequestBody @Valid SystemResourceCateCheckChildRequest
											 systemResourceCateCheckChildRequest);

    /**
     * 验证是否有素材
     *
     * @param systemResourceCateCheckResourceRequest 验证是否有素材 {@link SystemResourceCateCheckResourceRequest}
     * @return 验证是否有素材 {@link SystemResourceCateListResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/systemresourcecate/check-resource")
    BaseResponse<Integer> checkResource(@RequestBody @Valid SystemResourceCateCheckResourceRequest systemResourceCateCheckResourceRequest);


}

