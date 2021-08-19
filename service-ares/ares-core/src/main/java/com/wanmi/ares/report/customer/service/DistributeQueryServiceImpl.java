package com.wanmi.ares.report.customer.service;

import com.wanmi.ares.interfaces.customer.CustomerDistrQueryService;
import com.wanmi.ares.report.customer.dao.AreaDistributeReportMapper;
import com.wanmi.ares.report.customer.dao.CustomerMapper;
import com.wanmi.ares.report.customer.dao.LevelDistributeReportMapper;
import com.wanmi.ares.report.customer.dao.ReplayStoreMapper;
import com.wanmi.ares.report.customer.model.root.AreaDistrReport;
import com.wanmi.ares.report.customer.model.root.LevelDistrReport;
import com.wanmi.ares.request.customer.CustomerDistrQueryRequest;
import com.wanmi.ares.source.model.root.CustomerLevel;
import com.wanmi.ares.source.model.root.ReplayStoreLevel;
import com.wanmi.ares.source.model.root.Store;
import com.wanmi.ares.source.service.CustomerLevelService;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.view.customer.CustomerAreaDistrResponse;
import com.wanmi.ares.view.customer.CustomerAreaDistrView;
import com.wanmi.ares.view.customer.CustomerLevelDistrResponse;
import com.wanmi.ares.view.customer.CustomerLevelDistrView;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>客户分布报表查询Service</p>
 * Created by of628-wenzhi on 2017-10-17-上午10:13.
 */
@Service
public class DistributeQueryServiceImpl implements CustomerDistrQueryService.Iface {

    @Resource
    private LevelDistributeReportMapper levelDistrMapper;

    @Resource
    private AreaDistributeReportMapper areaDistrMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private CustomerLevelService customerLevelService;

    @Resource
    private ReplayStoreMapper replayStoreMapper;


    @Override
    public CustomerLevelDistrResponse queryLevelDistrView(CustomerDistrQueryRequest request) {
        LocalDate date;
        if (StringUtils.isNotBlank(request.getMonth())) {
            date = DateUtil.parseByMonth(request.getMonth());
        } else {
            date = DateUtil.parseByDateCycle(request.getDateCycle());
        }
        if (request.getCompanyId().equals("0") || queryStoreInfo(Long.valueOf(request.getCompanyId())).getCompanyType() == 0) {
            request.setCompanyId(Constants.bossId);
        }
        List<LevelDistrReport> list = levelDistrMapper.query(request.getCompanyId(), date);
        CustomerLevelDistrResponse response = new CustomerLevelDistrResponse();
        response.setCompanyId(request.getCompanyId());
        if (CollectionUtils.isNotEmpty(list)) {
            if (request.getCompanyId().equals("0")) {
                response.setViewList(fillLevelViewList(list));
            } else {
                response.setViewList(fillStoreLevelViewList(list));
            }
            response.setTotal(list.get(0).getTotal());
        } else {
            response.setViewList(Collections.emptyList());
        }
        return response;
    }

    @Override
    public CustomerAreaDistrResponse queryAreaDistrView(CustomerDistrQueryRequest request) {
        LocalDate date;
        if (StringUtils.isNotBlank(request.getMonth())) {
            date = DateUtil.parseByMonth(request.getMonth());
        } else {
            date = DateUtil.parseByDateCycle(request.getDateCycle());
        }
        if (request.getCompanyId().equals("0") || queryStoreInfo(Long.valueOf(request.getCompanyId())).getCompanyType() == 0) {
            request.setCompanyId("0");
        }
        List<AreaDistrReport> list = areaDistrMapper.query(request.getCompanyId(), date);
        CustomerAreaDistrResponse response = new CustomerAreaDistrResponse();
        List<CustomerAreaDistrView> viewList = new ArrayList<>();
        response.setViewList(viewList);
        response.setCompanyId(request.getCompanyId());
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(i -> viewList.add(new CustomerAreaDistrView(i.getCityId().toString(), i.getNum())));
        }
        return response;
    }

    @Override
    public int totalCount(CustomerDistrQueryRequest request) {
        LocalDate date;
        if (StringUtils.isNotBlank(request.getMonth())) {
            date = DateUtil.parseByMonth(request.getMonth());
        } else {
            date = DateUtil.parseByDateCycle(request.getDateCycle());
        }
        if ("0".equals(request.getCompanyId())) {
            return customerMapper.queryTotal(date);
        } else {
            return customerMapper.queryTotalByCompany(date, request.getCompanyId());
        }
    }

    private Store queryStoreInfo(Long companyInfoId) {
        return replayStoreMapper.queryByCompanyInfoId(companyInfoId);
    }


    private List<CustomerLevelDistrView> fillLevelViewList(List<LevelDistrReport> list) {
        List<CustomerLevelDistrView> viewList = new ArrayList<>();
        List<String> ids = list.stream().map(i -> i.getLevelId() + "").collect(Collectors.toList());
        List<CustomerLevel> levelList = customerLevelService.queryCustomerLevelByIds(ids);
        if (CollectionUtils.isNotEmpty(levelList)) {
            Map<String, CustomerLevel> map = levelList.stream().collect(Collectors.toMap(CustomerLevel::getId, Function.identity()));
            list.forEach(l -> {
                        CustomerLevelDistrView view = new CustomerLevelDistrView();
                        view.setNum(l.getNum())
                                .setCentage(l.getCentage())
                                .setLevelId(l.getLevelId() + "");
                        CustomerLevel level = map.get(l.getLevelId() + "");
                        if (level != null) {
                            if (StringUtils.isNotBlank(level.getName())) {
                                view.setLevelName(level.getName());
                            } else {
                                view.setLevelName("无");
                            }
                        } else {
                            view.setLevelName("无");
                        }

                        viewList.add(view);
                    }
            );
        }
        return viewList;
    }

    /**
     * @return java.util.List<com.wanmi.ares.view.customer.CustomerLevelDistrView>
     * @Author lvzhenwei
     * @Description 店铺等级查看
     * @Date 11:23 2019/9/20
     * @Param [list]
     **/
    private List<CustomerLevelDistrView> fillStoreLevelViewList(List<LevelDistrReport> list) {
        List<CustomerLevelDistrView> viewList = new ArrayList<>();
        List<String> ids = list.stream().map(i -> i.getLevelId() + "").collect(Collectors.toList());
        List<ReplayStoreLevel> levelList = customerLevelService.queryStoreCustomerLevelByIds(ids);
        if (CollectionUtils.isNotEmpty(levelList)) {
            Map<Long, ReplayStoreLevel> map = levelList.stream().collect(Collectors.toMap(ReplayStoreLevel::getStoreLevelId, Function.identity()));
            list.forEach(l -> {
                        CustomerLevelDistrView view = new CustomerLevelDistrView();
                        view.setNum(l.getNum())
                                .setCentage(l.getCentage())
                                .setLevelId(l.getLevelId() + "");
                        ReplayStoreLevel level = map.get(l.getLevelId());
                        if (level != null) {
                            if (StringUtils.isNotBlank(level.getLevelName())) {
                                view.setLevelName(level.getLevelName());
                            } else {
                                view.setLevelName("无");
                            }
                        } else {
                            view.setLevelName("无");
                        }

                        viewList.add(view);
                    }
            );
        } else {
            list.forEach(l -> {
                        CustomerLevelDistrView view = new CustomerLevelDistrView();
                        view.setNum(l.getNum())
                                .setCentage(l.getCentage())
                                .setLevelId(l.getLevelId() + "");
                        view.setLevelName("无");
                        viewList.add(view);
                    }
            );
        }
        return viewList;
    }

}
