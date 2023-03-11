package com.wanmi.sbc.setting.provider.impl.presetsearch;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.presetsearch.PresetSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import com.wanmi.sbc.setting.presetsearch.service.PresetSearchTermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新增预置搜索词
 */
@RestController
public class PresetSearchTermsQueryController implements PresetSearchTermsQueryProvider {

    @Autowired
    private PresetSearchTermsService presetSearchTermsService;

    @Override
    public BaseResponse<PresetSearchTermsQueryResponse> listPresetSearchTerms() {
        return BaseResponse.success(presetSearchTermsService.listPresetSearchTerms());
    }

    @Override
    public BaseResponse<PresetSearchTermsQueryResponse> listPresetSearchTermsV2() {
        return BaseResponse.success(presetSearchTermsService.listPresetSearchTermsV2());
    }
}
