package com.wanmi.sbc.elastic.provider.impl.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.groupon.EsGrouponActivityProvider;
import com.wanmi.sbc.elastic.api.request.groupon.*;
import com.wanmi.sbc.elastic.groupon.service.EsGrouponActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/12 18:56
 * @description <p> </p>
 */
@RestController
@Validated
public class EsGrouponActivityController implements EsGrouponActivityProvider {

    @Autowired
    private EsGrouponActivityService esGrouponActivityService;

    @Override
    public BaseResponse add(@RequestBody @Valid EsGrouponActivityAddReqquest request) {
        esGrouponActivityService.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid EsGrouponActivityDelByIdRequest request) {
        esGrouponActivityService.deleteById(request.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse init(@RequestBody @Valid EsGrouponActivityPageRequest request) {

        esGrouponActivityService.init(request);

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse batchStickyMarketing(@RequestBody @Valid EsGrouponActivityBatchStickyRequest request) {
        esGrouponActivityService.batchStickyMarketing(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse batchRefuseMarketing(@RequestBody @Valid EsGrouponActivityRefuseRequest request) {
        esGrouponActivityService.batchRefuseMarketing(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse batchCheckMarketing(@RequestBody @Valid EsGrouponActivityBatchCheckRequest request) {
        esGrouponActivityService.batchCheckMarketing(request);
        return BaseResponse.SUCCESSFUL();
    }
}
