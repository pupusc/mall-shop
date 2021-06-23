package com.wanmi.sbc.setting.provider.impl;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.OperationLogQueryProvider;
import com.wanmi.sbc.setting.api.request.OperationLogListRequest;
import com.wanmi.sbc.setting.api.response.OperationLogListResponse;
import com.wanmi.sbc.setting.api.response.OperationLogPageResponse;
import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import com.wanmi.sbc.setting.log.OperationLog;
import com.wanmi.sbc.setting.log.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OperationLogQueryController implements OperationLogQueryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogQueryController.class);

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public BaseResponse<OperationLogListResponse> list(@RequestBody OperationLogListRequest queryRequest) {
        OperationLogListResponse response = new OperationLogListResponse();
        List<OperationLog> logList = operationLogService.query(queryRequest);

        List<OperationLogVO> voList = new ArrayList<>();

        KsBeanUtil.copyList(logList, voList);

        response.setLogVOList(voList);

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<OperationLogPageResponse> queryOpLogByCriteria(@RequestBody OperationLogListRequest queryRequest) {
        Page<OperationLog> operationLogPage = operationLogService.queryOpLogByCriteria(queryRequest);

        Page<OperationLogVO> page = operationLogPage.map(source -> {
            OperationLogVO target = new OperationLogVO();
            KsBeanUtil.copyProperties(source, target);
            return target;
        });
        MicroServicePage<OperationLogVO> microServicePage = new MicroServicePage<>(page, queryRequest.getPageable());

        OperationLogPageResponse pageResponse = new OperationLogPageResponse();
        pageResponse.setOpLogPage(microServicePage);
        return BaseResponse.success(pageResponse);
    }

    @Override
    public BaseResponse<OperationLogListResponse> exportOpLogByCriteria(@RequestBody OperationLogListRequest queryRequest) {
        List<OperationLog> operationLogs = operationLogService.exportOpLogByCriteria(queryRequest);

        List<OperationLogVO> operationLogVoList = Lists.newArrayList();
        KsBeanUtil.copyList(operationLogs, operationLogVoList);

        OperationLogListResponse response = new OperationLogListResponse();
        response.setLogVOList(operationLogVoList);

        LOGGER.info("/setting/operation-log/exportOpLogByCriteria response:{}", response);
        return BaseResponse.success(response);
    }
}
