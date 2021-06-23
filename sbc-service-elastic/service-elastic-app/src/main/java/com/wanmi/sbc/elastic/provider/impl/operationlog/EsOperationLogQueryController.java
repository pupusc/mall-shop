package com.wanmi.sbc.elastic.provider.impl.operationlog;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.operationlog.EsOperationLogQueryProvider;
import com.wanmi.sbc.elastic.api.request.operationlog.EsOperationLogListRequest;
import com.wanmi.sbc.elastic.api.response.operationlog.EsOperationLogPageResponse;
import com.wanmi.sbc.elastic.operationlog.service.EsOperationLogQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author houshuai
 * @date 2020/12/16 10:26
 * @description <p> </p>
 */
@RestController
public class EsOperationLogQueryController implements EsOperationLogQueryProvider {

    @Autowired
    private EsOperationLogQueryService esOperationLogQueryService;

    @Override
    public BaseResponse<EsOperationLogPageResponse> queryOpLogByCriteria(EsOperationLogListRequest queryRequest) {

        return esOperationLogQueryService.queryOpLogByCriteria(queryRequest);
    }

    @Override
    public BaseResponse init(EsOperationLogListRequest queryRequest) {

        esOperationLogQueryService.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }
}
