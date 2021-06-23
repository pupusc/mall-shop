package com.wanmi.ares.report.customer.service;

import com.wanmi.ares.report.customer.dao.AreaDistributeReportMapper;
import com.wanmi.ares.report.customer.dao.LevelDistributeReportMapper;
import com.wanmi.ares.report.customer.model.request.CustomerAreaDistributeRequest;
import com.wanmi.ares.report.customer.model.request.CustomerLevelDistributeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @ClassName CustomerLevelAndAreaStatisticsService
 * @Description 客户统计--客户概况按等级统计service
 * @Author lvzhenwei
 * @Date 2019/9/20 17:39
 **/
@Service
public class CustomerLevelAndAreaStatisticsService {

    @Resource
    private AreaDistributeReportMapper areaDistributeReportMapper;

    @Resource
    private LevelDistributeReportMapper levelDistributeReportMapper;

    public void generateData(String type){
        LocalDate targetDate = LocalDate.now();
        //判断是否是执行当天的定时任务
        if("1".equals(type)){//今天数据
            generateThirdShopCustomerAreaDistribute(targetDate);
            generateThirdShopCustomerLevelDistribute(targetDate);
        } else {//昨天统计数据
            targetDate = LocalDate.now().minusDays(1);
            generateThirdShopCustomerAreaDistribute(targetDate);
            generateThirdShopCustomerLevelDistribute(targetDate);
        }

    }

    /**
     * @Author lvzhenwei
     * @Description 生成客户统计客户概况--按客户地区分布数据
     * @Date 11:06 2019/9/23
     * @Param []
     * @return void
     **/
    @Transactional
    public void generateThirdShopCustomerAreaDistribute(LocalDate targetDate){
        CustomerAreaDistributeRequest areaRequest = new CustomerAreaDistributeRequest();
        areaRequest.setTargetDate(targetDate);
        areaDistributeReportMapper.generateBossCustomerAreaDistribute(areaRequest);
        areaDistributeReportMapper.generateThirdShopCustomerAreaDistribute(areaRequest);
    }

    /**
     * @Author lvzhenwei
     * @Description 生成客户统计客户概况--按客等级区分布数据
     * @Date 11:06 2019/9/23
     * @Param []
     * @return void
     **/
    @Transactional
    public void generateThirdShopCustomerLevelDistribute(LocalDate targetDate){
        CustomerLevelDistributeRequest request = new CustomerLevelDistributeRequest();
        request.setTargetDate(targetDate);
        levelDistributeReportMapper.generateBossCustomerLevelDistribute(request);
        levelDistributeReportMapper.generateThirdShopCustomerLevelDistribute(request);
    }
}
