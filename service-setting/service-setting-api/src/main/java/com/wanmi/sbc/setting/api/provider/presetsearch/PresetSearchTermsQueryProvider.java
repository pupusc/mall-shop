package com.wanmi.sbc.setting.api.provider.presetsearch;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.preserotation.PresetSearchRotationQueryResponse;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>预置搜索词VO</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@FeignClient(value = "${application.setting.name}", contextId = "PresetSearchTermsQueryProvider")
public interface PresetSearchTermsQueryProvider {

    /**
     * 查询预置搜索词
     */
    @PostMapping("/setting/${application.setting.version}/preset_search_terms/list")
    BaseResponse<PresetSearchTermsQueryResponse> listPresetSearchTerms();

    @PostMapping("/setting/${application.setting.version}/preset_search_terms/listV2")
    BaseResponse<PresetSearchTermsQueryResponse> listPresetSearchTermsV2();

    @PostMapping("/setting/${application.setting.version}/preset_search_rotation/list")
    BaseResponse<PresetSearchRotationQueryResponse> listPresetSearchRotation();

}
