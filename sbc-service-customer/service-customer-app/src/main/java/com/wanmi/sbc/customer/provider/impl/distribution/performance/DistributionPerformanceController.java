package com.wanmi.sbc.customer.provider.impl.distribution.performance;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.performance.DistributionPerformanceProvider;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionMonthPerformanceListEnteringRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceCleanByTargetRequest;
import com.wanmi.sbc.customer.api.request.distribution.performance.DistributionPerformanceEnteringRequest;
import com.wanmi.sbc.customer.bean.dto.DistributionMonthPerformanceDTO;
import com.wanmi.sbc.customer.bean.dto.DistributionPerformanceDTO;
import com.wanmi.sbc.customer.distribution.performance.model.root.DistributionPerformanceDay;
import com.wanmi.sbc.customer.distribution.performance.model.root.DistributionPerformanceMonth;
import com.wanmi.sbc.customer.distribution.performance.service.DistributionPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>分销业绩业务操作实现Controller</p>
 * Created by of628-wenzhi on 2019-04-18-17:56.
 */
@RestController
@Validated
public class DistributionPerformanceController implements DistributionPerformanceProvider {

    @Autowired
    private DistributionPerformanceService performanceService;

    @Override
    public BaseResponse cleanByTarget(@RequestBody @Valid DistributionPerformanceCleanByTargetRequest request) {
        performanceService.cleanByYearAndMonth(request.getYear(), request.getMonth());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse enteringPerformance(@RequestBody @Valid DistributionPerformanceEnteringRequest request) {
        DistributionPerformanceDay data = wraperFromDTO2Data(request.getData());
        performanceService.add(data);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse enteringMonthPerformanceList(@RequestBody @Valid DistributionMonthPerformanceListEnteringRequest
                                                             request) {
        List<DistributionPerformanceMonth> data =
                request.getDataList().stream().map(this::wraperFromDTO2Data).collect(Collectors.toList());
        performanceService.add(data);
        return BaseResponse.SUCCESSFUL();
    }

    private DistributionPerformanceDay wraperFromDTO2Data(DistributionPerformanceDTO dto) {
        DistributionPerformanceDay data = new DistributionPerformanceDay();
        KsBeanUtil.copyPropertiesThird(dto, data);
        return data;
    }

    private DistributionPerformanceMonth wraperFromDTO2Data(DistributionMonthPerformanceDTO dto) {
        DistributionPerformanceMonth data = new DistributionPerformanceMonth();
        KsBeanUtil.copyPropertiesThird(dto, data);
        data.setCreateTime(LocalDateTime.now());
        return data;
    }
}
