package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCommissionQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCommissionExportRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCommissionPageRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCommissionExportResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCommissionPageResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionCommissionForPageVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributorLevel;
import com.wanmi.sbc.customer.distribution.service.DistributionCommissionService;
import com.wanmi.sbc.customer.distribution.service.DistributorLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by feitingting on 2019/2/21.
 */
@RestController
public class DistributionCommissionController implements DistributionCommissionQueryProvider {

    @Autowired
    private DistributionCommissionService distributionCommissionService;

    @Autowired
    private DistributorLevelService distributorLevelService;


    @Override
    public BaseResponse<DistributionCommissionPageResponse> findDistributionCommissionPage(@RequestBody @Valid DistributionCommissionPageRequest request) {
        DistributionCommissionPageResponse response = distributionCommissionService.page(request);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<DistributionCommissionExportResponse> findDistributionCommissionExport(@RequestBody @Valid DistributionCommissionExportRequest request) {
        DistributionCommissionPageRequest queryReq = new DistributionCommissionPageRequest();
        KsBeanUtil.copyPropertiesThird(request, queryReq);

        List<DistributionCommissionForPageVO> dataRecords = new ArrayList<>();
        // 分批次查询，避免出现hibernate事务超时 共查询1000条
        for (int i = 0; i < 10; i++) {
            queryReq.setPageNum(i);
            queryReq.setPageSize(100);

            DistributionCommissionPageResponse response = distributionCommissionService.page(queryReq);
            List<DistributionCommissionForPageVO> data = response.getRecordList();
            dataRecords.addAll(data);
            if (data.size() < 100) {
                break;
            }
        }
        List<DistributorLevel> distributorLevelList =  distributorLevelService.findAll();
        Map<String,String> map = distributorLevelList.stream().collect(Collectors.toMap(DistributorLevel::getDistributorLevelId,DistributorLevel::getDistributorLevelName));
        dataRecords = dataRecords.stream().map(distributionCommissionForPageVO -> {
            distributionCommissionForPageVO.setDistributorLevelName(map.get(distributionCommissionForPageVO.getDistributorLevelId()));
            return distributionCommissionForPageVO;
        }).collect(Collectors.toList());
        return BaseResponse.success(new DistributionCommissionExportResponse(dataRecords));
    }

    @Override
    public BaseResponse initStatisticsCache(){
       Boolean map = distributionCommissionService.initStatisticsCache();
        return BaseResponse.success(map);
    }
}
