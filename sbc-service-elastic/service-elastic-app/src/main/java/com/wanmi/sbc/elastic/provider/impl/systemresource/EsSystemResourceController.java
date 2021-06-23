package com.wanmi.sbc.elastic.provider.impl.systemresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.systemresource.EsSystemResourceProvider;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourcePageRequest;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourceSaveRequest;
import com.wanmi.sbc.elastic.systemresource.service.EsSystemResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/14 14:56
 * @description <p> </p>
 */
@RestController
public class EsSystemResourceController implements EsSystemResourceProvider {

    @Autowired
    private EsSystemResourceService esSystemResourceService;

    @Override
    public BaseResponse add(@RequestBody @Valid EsSystemResourceSaveRequest request) {
        esSystemResourceService.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse init(EsSystemResourcePageRequest request) {
        esSystemResourceService.init(request);
        return BaseResponse.SUCCESSFUL();
    }
}
