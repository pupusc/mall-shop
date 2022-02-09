package com.wanmi.ares.report.customer.service;


import com.wanmi.ares.base.PageRequest;
import com.wanmi.ares.base.SortType;
import com.wanmi.ares.enums.QueryDateCycle;
import com.wanmi.ares.exception.AresRuntimeException;
import com.wanmi.ares.interfaces.customer.CustomerReportQueryService;
import com.wanmi.ares.report.customer.dao.CustomerOrderReportMapper;
import com.wanmi.ares.report.customer.dao.CustomerReportTable;
import com.wanmi.ares.report.customer.dao.ReplayStoreMapper;
import com.wanmi.ares.report.customer.model.root.CustomerAreaReport;
import com.wanmi.ares.report.customer.model.root.CustomerLevelReport;
import com.wanmi.ares.report.customer.model.root.CustomerReport;
import com.wanmi.ares.request.customer.CustomerOrderQueryRequest;
import com.wanmi.ares.request.customer.CustomerQueryType;
import com.wanmi.ares.source.model.root.Store;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.view.customer.CustomerOrderPageView;
import com.wanmi.ares.view.customer.CustomerOrderView;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

/**
 * 会员报表查询
 */
@Service
public class CustomerReportThriftService implements CustomerReportQueryService.Iface {

    @Autowired
    private CustomerOrderReportMapper customerOrderReportMapper;

    @Resource
    private ReplayStoreMapper replayStoreMapper;

    /**
     * 这种方式待优化，但是可以防止注入风险
     */
    private static Dictionary<String, String> sortField =
            new Hashtable<String, String>();

    static {
        sortField.put("orderCount", "ORDER_NUM");
        sortField.put("amount", "ORDER_MONEY");
        sortField.put("skuCount", "ORDER_GOODS_NUM");
        sortField.put("payAmount", "PAY_MONEY");
        sortField.put("orderPerPrice", "ORDER_PER_PRICE");
        sortField.put("returnCount", "REFUND_NUM");
        sortField.put("returnAmount", "REFUND_MONEY");
        sortField.put("returnSkuCount", "REFUND_GOODS_NUM");
        sortField.put("userPerPrice", "USER_PER_PRICE_THIRTY");
        sortField.put("payOrderCount", "PAY_NUM");
    }


    /**
     * 查询会员参数报表
     *
     * @param request request
     * @return
     */
    @Override
    public CustomerOrderPageView queryCustomerOrders(CustomerOrderQueryRequest request)  {
        if (Objects.nonNull(request.getSortType())) {
            if (SortType.ASC.equals(request.getSortType())) {
                request.setSortTypeText("ASC");
            } else {
                request.setSortTypeText("DESC");
            }
        }
        if (Objects.nonNull(request.getSortField()) && Objects.nonNull(sortField.get(request.getSortField()))) {
            request.setSortField(sortField.get(request.getSortField()));
        } else {
            //默认下单金额降序
            request.setSortField(sortField.get("amount"));
            request.setSortTypeText("DESC");
        }

        if (CustomerQueryType.CUSTOMER.equals(request.getQueryType())) {
            return queryCustomerReport(request);
        }
        if (CustomerQueryType.CUSTOMER_LEVEL.equals(request.getQueryType())) {
            return queryCustomerLevelReport(request);
        }

        if (CustomerQueryType.CUSTOMER_AREA.equals(request.getQueryType())) {
            return queryCustomerAreaReport(request);
        }


        //throw new AresRuntimeException("查询会员报表参数错误");
        throw new SbcRuntimeException("查询会员报表参数错误");
    }


    /**
     * 查询客户订单报表
     *
     * @param request request
     * @return CustomerOrderPageView
     */
    private CustomerOrderPageView queryCustomerReport(CustomerOrderQueryRequest request) {
        List<CustomerReport> customerReportList;
        CustomerOrderPageView customerOrderPageView = new CustomerOrderPageView();
        request.setCompanyId(request.getCompanyId());

        String tableName = "";
        String daylyDate = null;
        if (QueryDateCycle.yesterday.equals(request.getDateCycle()) || QueryDateCycle.today.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_DAY.toString();
            daylyDate = QueryDateCycle.yesterday.equals(request.getDateCycle()) ?
                    DateUtil.format(LocalDateTime.now().minusDays(1), DateUtil.FMT_DATE_1) :
                    DateUtil.format(LocalDateTime.now(), DateUtil.FMT_DATE_1);
        }
        if (QueryDateCycle.latest7Days.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_RECENT_SEVEN.toString();
        }

        if (QueryDateCycle.latest30Days.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_RECENT_THIRTY.toString();
        }

        //当有月报表的时候
        if (StringUtils.isNotEmpty(request.getMonth())) {
            tableName = CustomerReportTable.CUSTOMER_MONTH.toString();
            String month = request.getMonth().replace("-", "");
            daylyDate = DateUtil.format(DateUtil.pareeByMonthFirst(month), DateUtil.FMT_DATE_1);
        }

        if (StringUtils.isEmpty(tableName)) {
            //throw new AresRuntimeException("时间参数有错误");
            throw new SbcRuntimeException("时间参数有错误");
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setStart((request.getPageNum() - 1) * pageRequest.getPageSize());
        Store store = replayStoreMapper.queryByCompanyInfoId(Long.valueOf(request.getCompanyId()));
        if(request.getCompanyId().equals(Constants.bossId)||store.getCompanyType()==0){
            request.setCompanyId(Constants.bossId);
        }
        Integer total = customerOrderReportMapper.countCustomerReport(request, tableName, daylyDate);

        if (validCount(request, customerOrderPageView, total)) return customerOrderPageView;

        customerReportList = customerOrderReportMapper.queryCustomerReport(request, pageRequest, tableName, daylyDate);
        List<CustomerOrderView> customerOrderViewList = customerReportList.stream().map(customerReport -> {
            CustomerOrderView customerOrderView = new CustomerOrderView();
            customerOrderView.setAmount(customerReport.getAmount().doubleValue())
                    .setOrderPerPrice(customerReport.getOrderPerPrice().doubleValue())
                    .setPayAmount(customerReport.getPayAmount().doubleValue())
                    .setReturnAmount(customerReport.getReturnAmount().doubleValue())
                    .setReturnCount(customerReport.getReturnCount())
                    .setSkuCount(customerReport.getSkuCount())
                    .setCompanyId(customerReport.getCompanyId())
                    .setId(customerReport.getId())
                    .setOrderCount(customerReport.getOrderCount())
                    .setReturnSkuCount(customerReport.getReturnSkuCount())
                    .setAccount(customerReport.getAccount())
                    .setPayOrderCount(Objects.nonNull(customerReport.getPayOrderCount())? customerReport.getPayOrderCount(): 0)
            ;

            customerOrderView.setCustomerName(customerReport.getName());
            return customerOrderView;
        }).collect(Collectors.toList());
        customerOrderPageView.setCurrent(request.getPageNum());
        customerOrderPageView.setCustomerOrderViewList(customerOrderViewList);
        customerOrderPageView.setTotal(Long.valueOf(total));

        return customerOrderPageView;
    }

    private boolean validCount(CustomerOrderQueryRequest request, CustomerOrderPageView customerOrderPageView, Integer total) {
        if (total < 1) {
            customerOrderPageView.setCustomerOrderViewList(EMPTY_LIST);
            customerOrderPageView.setTotal(0L);
            customerOrderPageView.setCurrent(request.getPageNum());
            return true;
        }
        return false;
    }


    private CustomerOrderPageView queryCustomerLevelReport(CustomerOrderQueryRequest request) {
        List<CustomerLevelReport> customerLevelReports = Lists.newArrayList();
        CustomerOrderPageView customerOrderPageView = new CustomerOrderPageView();

        String tableName = "";
        String daylyDate = null;
        if (QueryDateCycle.yesterday.equals(request.getDateCycle()) || QueryDateCycle.today.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_LEVEL_DAY.toString();
            daylyDate = QueryDateCycle.yesterday.equals(request.getDateCycle()) ?
                    DateUtil.format(LocalDateTime.now().minusDays(1), DateUtil.FMT_DATE_1) :
                    DateUtil.format(LocalDateTime.now(), DateUtil.FMT_DATE_1);
        }
        if (QueryDateCycle.latest7Days.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_LEVEL_RECENT_SEVEN.toString();
        }

        if (QueryDateCycle.latest30Days.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_LEVEL_RECENT_THIRTY.toString();
        }

        //当有月报表的时候
        if (StringUtils.isNotEmpty(request.getMonth())) {
            tableName = CustomerReportTable.CUSTOMER_LEVEL_MONTH.toString();
            String month = request.getMonth().replace("-", "");
            daylyDate = DateUtil.format(DateUtil.pareeByMonthFirst(month), DateUtil.FMT_DATE_1);
        }

        if (StringUtils.isEmpty(tableName)) {
            //throw new AresRuntimeException("时间参数有错误");
            throw new SbcRuntimeException("时间参数有错误");
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setStart((request.getPageNum() - 1) * pageRequest.getPageSize());

        Store store = replayStoreMapper.queryByCompanyInfoId(Long.valueOf(request.getCompanyId()));
        if(store.getCompanyType()==0){
            request.setCompanyId(Constants.bossId);
        }
        Integer total = customerOrderReportMapper.countCustomerLevelReport(request, tableName, daylyDate);
        if(!request.getCompanyId().equals("0")){
            total = customerOrderReportMapper.countCustomerStoreLevelReport(request, tableName, daylyDate);
        }
        if (validCount(request, customerOrderPageView, total)) return customerOrderPageView;

        customerLevelReports = customerOrderReportMapper.queryCustomerLevelReport(request, pageRequest, tableName, daylyDate);
        List<CustomerOrderView> customerOrderViewList = customerLevelReports.stream().map(customerReport -> {
            CustomerOrderView customerOrderView = new CustomerOrderView();
            customerOrderView.setLevelName(customerReport.getName())
                    .setAmount(customerReport.getAmount().doubleValue())
                    .setOrderPerPrice(Objects.isNull(customerReport.getOrderPerPrice()) ? 0.00 : customerReport.getOrderPerPrice().doubleValue())
                    .setPayAmount(customerReport.getPayAmount().doubleValue())
                    .setReturnAmount(customerReport.getReturnAmount().doubleValue())
                    .setReturnCount(customerReport.getReturnCount())
                    .setSkuCount(customerReport.getSkuCount())
                    .setCompanyId(customerReport.getCompanyId())
                    .setId(customerReport.getId())
                    .setOrderCount(customerReport.getOrderCount())
                    .setReturnSkuCount(customerReport.getReturnSkuCount())
                    .setUserPerPrice(Objects.nonNull(customerReport.getUserPerPrice()) ?customerReport.getUserPerPrice().doubleValue() : 0)
                    .setPayOrderCount(Objects.nonNull(customerReport.getPayOrderCount())? customerReport.getPayOrderCount(): 0)
            ;

            return customerOrderView;
        }).collect(Collectors.toList());
        customerOrderPageView.setCurrent(request.getPageNum());
        customerOrderPageView.setCustomerOrderViewList(customerOrderViewList);
        customerOrderPageView.setTotal(Long.valueOf(total));

        return customerOrderPageView;
    }

    /**
     * 查询地区会员报表
     *
     * @param request request
     * @return CustomerOrderPageView
     */
    private CustomerOrderPageView queryCustomerAreaReport(CustomerOrderQueryRequest request) {
        List<CustomerAreaReport> customerAreaReports = Lists.newArrayList();
        CustomerOrderPageView customerOrderPageView = new CustomerOrderPageView();

        String tableName = "";
        String daylyDate = null;
        if (QueryDateCycle.yesterday.equals(request.getDateCycle()) || QueryDateCycle.today.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_REGION_DAY.toString();
            daylyDate = QueryDateCycle.yesterday.equals(request.getDateCycle()) ?
                    DateUtil.format(LocalDateTime.now().minusDays(1), DateUtil.FMT_DATE_1) :
                    DateUtil.format(LocalDateTime.now(), DateUtil.FMT_DATE_1);
        }
        if (QueryDateCycle.latest7Days.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_REGION_RECENT_SEVEN.toString();
        }

        if (QueryDateCycle.latest30Days.equals(request.getDateCycle())) {
            tableName = CustomerReportTable.CUSTOMER_REGION_RECENT_THIRTY.toString();
        }
        //当有月报表的时候
        if (StringUtils.isNotEmpty(request.getMonth())) {
            tableName = CustomerReportTable.CUSTOMER_REGION_MONTH.toString();
            String month = request.getMonth().replace("-", "");
            daylyDate = DateUtil.format(DateUtil.pareeByMonthFirst(month), DateUtil.FMT_DATE_1);
        }

        if (StringUtils.isEmpty(tableName)) {
            //throw new AresRuntimeException("时间参数有错误");
            throw new SbcRuntimeException("时间参数有错误");
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setStart((request.getPageNum() - 1) * pageRequest.getPageSize());

        //对cityList 解析处理
        if (StringUtils.isEmpty(request.getQueryText())) {
            request.cityList = null;
        } else {
            request.cityList = Lists.newArrayList(request.getQueryText().split(","));
        }

        Store store = replayStoreMapper.queryByCompanyInfoId(Long.valueOf(request.getCompanyId()));
        if(request.getCompanyId().equals(Constants.bossId)||store.getCompanyType()==0){
            request.setCompanyId(Constants.bossId);
        }
        Integer total = customerOrderReportMapper.countCustomerAreaReport(request, tableName, daylyDate);

        if (validCount(request, customerOrderPageView, total)) return customerOrderPageView;


        customerAreaReports = customerOrderReportMapper.queryCustomerAreaReport(request, pageRequest, tableName, daylyDate);
        List<CustomerOrderView> customerOrderViewList = customerAreaReports.stream().map(customerAreaReport -> {
            CustomerOrderView customerOrderView = new CustomerOrderView();
            customerOrderView.setAmount(customerAreaReport.getAmount().doubleValue())
                    .setOrderPerPrice(Objects.isNull(customerAreaReport.getOrderPerPrice()) ? 0.00 : customerAreaReport.getOrderPerPrice().doubleValue())
                    .setPayAmount(customerAreaReport.getPayAmount().doubleValue())
                    .setReturnAmount(customerAreaReport.getReturnAmount().doubleValue())
                    .setReturnCount(customerAreaReport.getReturnCount())
                    .setSkuCount(customerAreaReport.getSkuCount())
                    .setCompanyId(customerAreaReport.getCompanyId())
                    .setId(customerAreaReport.getId())
                    .setOrderCount(customerAreaReport.getOrderCount())
                    .setReturnSkuCount(customerAreaReport.getReturnSkuCount())
                    .setUserPerPrice(customerAreaReport.getUserPerPrice().doubleValue())
                    .setPayOrderCount(Objects.nonNull(customerAreaReport.getPayOrderCount())? customerAreaReport.getPayOrderCount(): 0)
            ;
            customerOrderView.setCityId(customerAreaReport.getCityId());
            return customerOrderView;
        }).collect(Collectors.toList());
        customerOrderPageView.setCurrent(request.getPageNum());
        customerOrderPageView.setCustomerOrderViewList(customerOrderViewList);
        customerOrderPageView.setTotal(Long.valueOf(total));
        return customerOrderPageView;
    }
}
