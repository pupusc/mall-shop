package com.wanmi.sbc.searchterms.presetsearch;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.presetsearch.PresetSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.provider.presetsearch.PresetSearchTermsSaveProvider;
import com.wanmi.sbc.setting.api.request.presetsearch.PresetSearchTermsListRequest;
import com.wanmi.sbc.setting.api.request.presetsearch.PresetSearchTermsModifyRequest;
import com.wanmi.sbc.setting.api.request.presetsearch.PresetSearchTermsRequest;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsListResponse;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @menu 搜索功能
 * @tag feature_d_v0
 * @status done
 */
@Api(tags = "PresetSearchTermsSaveController", description = "预置搜索词服务API")
@RestController
@ApiModel
@RequestMapping(value = "/preset_search_terms")
public class PresetSearchTermsSaveController {


    @Autowired
    private PresetSearchTermsSaveProvider searchTermsSaveProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private PresetSearchTermsQueryProvider presetSearchTermsProvider;


    @ApiOperation(value = "新增预置搜索词")
    @PostMapping("/add")
    public BaseResponse<PresetSearchTermsResponse> add(@RequestBody @Valid PresetSearchTermsRequest request) {
        operateLogMQUtil.convertAndSend("预置搜索词","新增预置热门搜索词","新增热门搜索词："+request.getPresetSearchKeyword());
        return searchTermsSaveProvider.add(request);
    }

    @ApiOperation(value = "新增预置搜索词")
    @PostMapping("/addBatch")
    public BaseResponse<PresetSearchTermsListResponse> addBatch(@RequestBody @Valid PresetSearchTermsListRequest request) {
        operateLogMQUtil.convertAndSend("预置搜索词","新增预置热门搜索词","新增热门搜索词："+request.toString());
        return searchTermsSaveProvider.addBatch(request);
    }

    /**
     * @description 编辑预置搜索词
     * @menu  搜索功能
     * @tag feature_d_v0.09
     * @status done
     */
    @ApiOperation(value = "编辑预置搜索词")
    @PostMapping("/modify")
    BaseResponse<PresetSearchTermsResponse> modify(@RequestBody @Valid List<PresetSearchTermsModifyRequest> requests){
        for (PresetSearchTermsModifyRequest request:requests) {
            operateLogMQUtil.convertAndSend("预置搜索词","编辑预置热门搜索词","编辑热门搜索词："+request.getPresetSearchKeyword());
            searchTermsSaveProvider.modify(request);
        }
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * @description 查询预置搜索词
     * @menu  搜索功能
     * @tag feature_d_v0.09
     * @status done
     */
    @ApiOperation(value = "查询预置搜索词")
    @PostMapping("/list")
    public BaseResponse<PresetSearchTermsQueryResponse> listPresetSearchTerms() {
        return presetSearchTermsProvider.listPresetSearchTerms();
    }

    /**
     * @description 查询预置搜索词
     * @menu  搜索功能
     * @tag feature_d_v0.09
     * @status done
     */
    @ApiOperation(value = "查询预置搜索词")
    @PostMapping("/listV2")
    public BaseResponse<PresetSearchTermsQueryResponse> listPresetSearchTermsV2() {
        return presetSearchTermsProvider.listPresetSearchTerms();
    }


}
