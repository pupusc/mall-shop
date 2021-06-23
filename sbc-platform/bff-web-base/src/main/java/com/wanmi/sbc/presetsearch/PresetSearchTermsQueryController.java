package com.wanmi.sbc.presetsearch;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.presetsearch.PresetSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询预置搜索词
 */
@Api(tags = "PresetSearchTermsQueryController", description = "预置搜索词服务API")
@RestController
@RequestMapping("/preset_search_terms")
@Validated
public class PresetSearchTermsQueryController {

    @Autowired
    private PresetSearchTermsQueryProvider presetSearchTermsProvider;


    /**
     * 查询预置搜索词
     * @return
     */
    @ApiOperation(value = "查询预置搜索词")
    @PostMapping("/list")
    public BaseResponse<PresetSearchTermsQueryResponse> listPresetSearchTerms() {
        return presetSearchTermsProvider.listPresetSearchTerms();
    }
}
