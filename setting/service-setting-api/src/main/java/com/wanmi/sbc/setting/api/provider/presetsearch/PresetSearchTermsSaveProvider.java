package com.wanmi.sbc.setting.api.provider.presetsearch;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.presetsearch.PresetSearchTermsModifyRequest;
import com.wanmi.sbc.setting.api.request.presetsearch.PresetSearchTermsRequest;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>预置搜索词VO</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@FeignClient(value = "${application.setting.name}", contextId = "PresetSearchTermsSaveProvider")
public interface PresetSearchTermsSaveProvider {

    /**
     * 新增预置搜索API
     * @author weiwenhao
     * @param request 基本设置新增参数结构 {@link PresetSearchTermsRequest}
     * @return 新增的基本设置信息 {@link PresetSearchTermsRequest}
     */
    @PostMapping("/setting/${application.setting.version}/preset_search_terms/add")
    BaseResponse<PresetSearchTermsResponse> add(@RequestBody @Valid PresetSearchTermsRequest request);

    /**
     * 编辑预置搜索API
     * @author weiwenhao
     * @param request 基本设置新增参数结构 {@link PresetSearchTermsRequest}
     * @return 新增的基本设置信息 {@link PresetSearchTermsRequest}
     */
    @PostMapping("/setting/${application.setting.version}/preset_search_terms/modify")
    BaseResponse<PresetSearchTermsResponse> modify(@RequestBody @Valid PresetSearchTermsModifyRequest request);

}
