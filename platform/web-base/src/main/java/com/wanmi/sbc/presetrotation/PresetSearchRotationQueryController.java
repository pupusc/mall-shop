package com.wanmi.sbc.presetrotation;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.presetsearch.PresetSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.response.preserotation.PresetSearchRotationQueryResponse;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询轮播词
 */
@Api(tags = "PresetSearchRotationQueryController", description = "预置搜索词服务API")
@RestController
@RequestMapping("/preset_search_rotation")
@Validated
public class PresetSearchRotationQueryController {

    @Autowired
    private PresetSearchTermsQueryProvider presetSearchTermsProvider;


    /**
     * 前端 查询轮播词
     * @menu 搜索功能
     * @return
     */
    @ApiOperation(value = "查询轮播词")
    @PostMapping("/list")
    public BaseResponse<PresetSearchRotationQueryResponse> search() {
        return presetSearchTermsProvider.listPresetSearchRotation();
    }
}
