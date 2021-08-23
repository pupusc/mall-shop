package com.wanmi.ares.report.employee.service;

import com.wanmi.ares.interfaces.employee.EmployeeQueryService;
import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.employee.dao.EmployeeClientReportMapper;
import com.wanmi.ares.report.employee.dao.EmployeePerformanceReportMapper;
import com.wanmi.ares.report.employee.model.entity.EmployeeClientQuery;
import com.wanmi.ares.report.employee.model.entity.EmployeePerformanceQuery;
import com.wanmi.ares.report.employee.model.root.EmployeeClientReport;
import com.wanmi.ares.report.employee.model.root.EmployeePerformanceReport;
import com.wanmi.ares.report.trade.model.request.TradeCollect;
import com.wanmi.ares.request.employee.EmployeeClientQueryRequest;
import com.wanmi.ares.request.employee.EmployeePerformanceQueryRequest;
import com.wanmi.ares.source.service.EmployeeService;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.view.employee.EmployeeClientResponse;
import com.wanmi.ares.view.employee.EmployeeClientView;
import com.wanmi.ares.view.employee.EmployeePerformanceView;
import com.wanmi.ares.view.employee.EmployeePerormanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>业务员获客报表查询实现</p>
 * Created by of628-wenzhi on 2017-10-20-下午3:07.
 */
@Service
@Slf4j
public class EmployeeQueryServiceImpl implements EmployeeQueryService.Iface {

    @Resource
    private EmployeeClientReportMapper clientMapper;

    @Resource
    private EmployeePerformanceReportMapper performanceMapper;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private EmployeeReportGenerateService generateService;

    @Override
    public EmployeeClientResponse queryViewByClient(EmployeeClientQueryRequest request)  {
        EmployeeClientQuery query = new EmployeeClientQuery(request.getSort());
        query.setPageBegin((request.getPageNo() - 1) * request.getPageSize());
        query.setDateCycle(request.getDataCycle());
        EmployeeClientResponse response = new EmployeeClientResponse();
        List<EmployeeClientView> viewList = new ArrayList<>();
        response.setViewList(viewList);
        try {
            BeanUtils.copyProperties(request, response);
            BeanUtils.copyProperties(request, query);
            //根据查询关键字获取业务员id集合
            List<Map<String,Object>> employees = null;
            if (StringUtils.isNotBlank(request.getKeywords())) {
                employees = getEmployeesByName(request.getKeywords());
                if (employees.isEmpty()) {
                    return response;
                }
                List<String> ids = employees.stream().map(employee -> String.valueOf(employee.get("employee_id"))).collect(Collectors.toList());
                query.setEmployeeIds(ids);
            }

            //业务员ID单独查询
            if(CollectionUtils.isNotEmpty(request.getEmployeeIds())){
                query.setEmployeeIds(request.getEmployeeIds());
            }

            fillClientViewList(employees, response, query);
        } catch (Exception e) {
            log.error("Query employee-client report view failed,request={},", request, e);
        }

        return response;
    }

    @Override
    public EmployeePerormanceResponse queryViewByPerformance(EmployeePerformanceQueryRequest request) {
        EmployeePerformanceQuery query = new EmployeePerformanceQuery(request.getSort());
        query.setPageBegin((request.getPageNo() - 1) * request.getPageSize());
        query.setDateCycle(request.getDataCycle());
        EmployeePerormanceResponse response = new EmployeePerormanceResponse();
        List<EmployeePerformanceView> viewList = new ArrayList<>();
        response.setViewList(viewList);
        try {
            BeanUtils.copyProperties(request, response);
            BeanUtils.copyProperties(request, query);
            //根据查询关键字获取业务员id集合
            List<Map<String,Object>> employees = null;
            if (StringUtils.isNotBlank(request.getKeywords())) {
                employees = getEmployeesByName(request.getKeywords());
                if (employees.isEmpty()) {
                    return response;
                }
                query.setEmployeeIds(employees.stream().map(employee -> String.valueOf(employee.get("employee_id"))).collect(Collectors.toList()));
            }
            if(StringUtils.isNotBlank(request.getEmployeeId())){
                query.setEmployeeIds(Collections.singletonList(request.getEmployeeId()));
            }
            //报表查询
            fillPerformanceViewList(employees, response, query);
        } catch (Exception e) {
            log.error("Query employee-performance report view failed,request={},", request, e);
        }
        return response;
    }

    public List<EmployeeClientView> exportViewByClient(ExportQuery query) {
        List<EmployeeClientReport> reports = clientMapper.export(query);
        if (reports.isEmpty()) {
            return Collections.emptyList();
        }
        List<Map<String,Object>> employees = getEmployeesByIds(reports.stream().map(EmployeeClientReport::getEmployeeId).collect(
                Collectors.toList()));
        List<EmployeeClientView> viewList = new ArrayList<>();
        wraperClientViewList(employees, reports, viewList);
        return viewList;
    }

    List<EmployeePerformanceView> exportViewByPerformance(ExportQuery query) {
        boolean isBossFlag = "0".equals(query.getCompanyId());
        List<EmployeePerformanceReport> reports ;
        if(isBossFlag){
            reports = performanceMapper.collectBossEmployeeTrade(TradeCollect.builder()
                    .beginDate(DateUtil.parse2Date(query.getDateFrom(), DateUtil.FMT_DATE_1))
                    .endDate(DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1))
                    .build());
        }else {
            reports = performanceMapper.collectThirdEmployeeTrade(TradeCollect.builder()
                    .beginDate(DateUtil.parse2Date(query.getDateFrom(), DateUtil.FMT_DATE_1))
                    .endDate(DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1))
                    .build());
        }

        if(!isBossFlag) {
            reports = reports.stream().filter(i -> i.getCompanyId().equals(query.getCompanyId())).collect(Collectors.toList());
        }
        if (reports.isEmpty()) {
            return Collections.emptyList();
        }
        reports = reports.stream().sorted(Comparator.comparing(EmployeePerformanceReport::getAmount).reversed()).collect(Collectors.toList());
        List<Map<String,Object>> employees = getEmployeesByIds(reports.stream().map(EmployeePerformanceReport::getEmployeeId).collect(
                Collectors.toList()));
        List<EmployeePerformanceView> viewList = new ArrayList<>();
        wraperPerformanceViewList(employees, reports, viewList);
        return viewList;
    }


    private void fillPerformanceViewList(List<Map<String,Object>> employees, EmployeePerormanceResponse response, EmployeePerformanceQuery query) {

        List<EmployeePerformanceReport> reports = performanceMapper.queryList(query);
        if (reports.isEmpty()) {
            return;
        }
        response.setCount(performanceMapper.countOfQuery(query));
        List<EmployeePerformanceView> viewList = response.getViewList();

        if (CollectionUtils.isEmpty(employees)) {
            employees = getEmployeesByIds(reports.stream().map(EmployeePerformanceReport::getEmployeeId).collect(
                    Collectors.toList()));
        }

        wraperPerformanceViewList(employees, reports, viewList);

    }

    private void wraperPerformanceViewList(List<Map<String,Object>> employees, List<EmployeePerformanceReport> reports,
                                           List<EmployeePerformanceView> viewList) {
        Map<String, Map> map = employees.stream().collect(Collectors.toMap(employee-> String.valueOf(employee.get(
                "employee_id")),
                Function.identity()));
        reports.forEach(
                r -> {
                    EmployeePerformanceView view = new EmployeePerformanceView();
                    viewList.add(view);
                    BeanUtils.copyProperties(r, view);
                    view.setAmount(r.getAmount().doubleValue())
                            .setCustomerUnitPrice(r.getCustomerUnitPrice().doubleValue())
                            .setOrderUnitPrice(r.getOrderUnitPrice().doubleValue())
                            .setPayAmount(r.getPayAmount().doubleValue())
                            .setReturnAmount(r.getReturnAmount().doubleValue())
                    ;
                    Map<String,Object> employee = map.get(r.getEmployeeId());
                    if (employee != null && StringUtils.isNotBlank(String.valueOf(employee.get("employee_name")))) {
                        view.setEmployeeName(String.valueOf(employee.get("employee_name")));
                    } else {
                        view.setEmployeeName("无");
                    }
                }
        );
    }

    private void fillClientViewList(List<Map<String,Object>> employees, EmployeeClientResponse response, EmployeeClientQuery query) {
        List<EmployeeClientReport> reports = clientMapper.queryList(query);
        if (reports.isEmpty()) {
            return;
        }
        response.setCount(clientMapper.countOfQuery(query));
        List<EmployeeClientView> viewList = response.getViewList();

        if (CollectionUtils.isEmpty(employees)) {
            employees = getEmployeesByIds(reports.stream().map(EmployeeClientReport::getEmployeeId).collect(
                    Collectors.toList()));
        }

        wraperClientViewList(employees, reports, viewList);
    }

    private void wraperClientViewList(List<Map<String,Object>> employees, List<EmployeeClientReport> reports, List<EmployeeClientView> viewList) {
        Map<String, Map> map = employees.stream().collect(Collectors.toMap(employee-> String.valueOf(employee.get(
                "employee_id")),
                Function.identity()));
        reports.forEach(
                r -> {
                    EmployeeClientView view = new EmployeeClientView();
                    viewList.add(view);
                    BeanUtils.copyProperties(r, view);
                    Map<String,Object> employee = map.get(r.getEmployeeId());
                    if (employee != null && StringUtils.isNotBlank(String.valueOf(employee.get("employee_name")))) {
                        view.setEmployeeName(String.valueOf(employee.get("employee_name")));
                    } else {
                        view.setEmployeeName("无");
                    }
                }
        );
    }

    private List<Map<String,Object>> getEmployeesByIds(List<String> ids) {
        List<Map<String,Object>> list = employeeService.findByEmployeeIds(ids);
        return list == null ? Collections.emptyList() : list;
    }


    private List<Map<String,Object>> getEmployeesByName(String keywords) {
        List<Map<String,Object>> list = employeeService.findByKeyWords(keywords);
        return list == null ? Collections.emptyList() : list;
    }

}
