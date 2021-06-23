package com.wanmi.sbc.customer.distribution.service;

import com.wanmi.sbc.customer.api.request.distribution.DistributionCommissionPageRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCommissionPageResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionCommissionForPageVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.DistributorLevel;
import com.wanmi.sbc.customer.distribution.repository.DistributionCustomerRepository;
import com.wanmi.sbc.customer.model.root.DistributionCommissionStatistics;
import com.wanmi.sbc.customer.redis.DistributionCommissionRedisService;
import com.wanmi.sbc.customer.util.QueryConditionsUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by feitingting on 2019/2/21.
 */
@Service
public class DistributionCommissionService {

    @Autowired
    private  DistributionCustomerRepository distributionCustomerRepository;

    @Autowired
    private DistributionCommissionRedisService distributionCommissionRedisService;

    @Autowired
    private DistributorLevelService distributorLevelService;

    public DistributionCommissionPageResponse page(DistributionCommissionPageRequest request) {
        Page<DistributionCustomer> distributionCustomers = distributionCustomerRepository.findAll(QueryConditionsUtil
                .getWhereCriteria(request), request.getPageRequest());
       return pageHelper(distributionCustomers, request.getPageNum());
    }


    private DistributionCommissionPageResponse pageHelper(Page<DistributionCustomer> distributionCustomers, int pageNum) {
        DistributionCommissionPageResponse distributionCommissionPageResponse = new DistributionCommissionPageResponse();
        distributionCommissionPageResponse.setCurrentPage(pageNum);
        //结果为空
        if (CollectionUtils.isEmpty(distributionCustomers.getContent())) {
            distributionCommissionPageResponse.setRecordList(Collections.emptyList());
            distributionCommissionPageResponse.setTotal(0L);
            return distributionCommissionPageResponse;
        }

        List<String> distributorLevelIds = distributionCustomers.getContent().stream().map(DistributionCustomer::getDistributorLevelId).collect(Collectors.toList());
        List<DistributorLevel> distributorLevelList = distributorLevelService.findByDistributorLevelIdIn(distributorLevelIds);
        final Map<String,String> map = distributorLevelList.stream().collect(Collectors.toMap(DistributorLevel::getDistributorLevelId,DistributorLevel::getDistributorLevelName));

        List<DistributionCommissionForPageVO> distributionCommissionForPageVOS = distributionCustomers.getContent().stream().map
                (distributionCustomer -> {
                    DistributionCommissionForPageVO distributionCommissionForPageVO = new DistributionCommissionForPageVO();
                    //对象拷贝
                    BeanUtils.copyProperties(distributionCustomer, distributionCommissionForPageVO);
                    if (MapUtils.isNotEmpty(map)){
                        distributionCommissionForPageVO.setDistributorLevelName(map.get(distributionCommissionForPageVO.getDistributorLevelId()));
                    }

                    return distributionCommissionForPageVO;
                }).collect(Collectors.toList());

        distributionCommissionPageResponse.setTotal(distributionCustomers.getTotalElements());
        distributionCommissionPageResponse.setRecordList(distributionCommissionForPageVOS);
        return distributionCommissionPageResponse;
    }


    public DistributionCommissionStatistics statistics() {
        return distributionCustomerRepository.statistics();
    }


    /**
     * 初始化分销佣金统计缓存
     *
     * @return
     */
    public Boolean initStatisticsCache() {
        DistributionCommissionStatistics distributionCommissionStatistics = statistics();
        return distributionCommissionRedisService.initDistributionCommissionStatistics(
                distributionCommissionStatistics.getCommissionTotal().doubleValue(),
                distributionCommissionStatistics.getCommission().doubleValue(),
                distributionCommissionStatistics.getRewardCash().doubleValue(),
                distributionCommissionStatistics.getCommissionNotRecorded().doubleValue(),
                distributionCommissionStatistics.getRewardCashNotRecorded().doubleValue());
    }
}
