package com.wanmi.ares.report.employee.service;

import com.wanmi.ares.report.customer.dao.CustomerMapper;
import com.wanmi.ares.report.employee.dao.EmployeeClientReportMapper;
import com.wanmi.ares.report.employee.dao.EmployeePerformanceReportMapper;
import com.wanmi.ares.report.employee.model.root.EmployeeClientReport;
import com.wanmi.ares.report.employee.model.root.EmployeePerformanceReport;
import com.wanmi.ares.report.trade.model.request.TradeCollect;
import com.wanmi.ares.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

/**
 * <p>基于业务员维度的报表操作Service</p>
 * Created by of628-wenzhi on 2017-09-26-下午1:59.
 */
@Service
@Slf4j
public class EmployeeReportGenerateService {

    @Resource
    private EmployeeClientReportMapper clientMapper;

    @Resource
    private EmployeePerformanceReportMapper performanceMapper;

    @Resource
    private CustomerMapper customerMapper;

    /**
     * 获客周期报表
     */
    public void clientPeriodReport() {

        LocalDate today = LocalDate.now();

        LocalDate day7 = today.minusDays(7);

        LocalDate day30 = today.minusDays(30);

        List<EmployeeClientReport> reports;

        try {
            //按最近7天统计
            reports = generateClient(today.minusDays(1), day7);
            store(reports, clientMapper::clear7Days, clientMapper::insert7days);
        } catch (Exception e) {
            log.error("The [employee-client] generated report exception of the last 7 days,\n", e);
        }

        try {
            //按最近30天统计
            reports = generateClient(today.minusDays(1), day30);
            store(reports, clientMapper::clear30Days, clientMapper::insert30Days);
        } catch (Exception e) {
            log.error("The [employee-client] generated report exception of the last 30 days,\n", e);
        }

        //月报表
        if (today.getDayOfMonth() == 1) {
            LocalDate firstDate = today.minusMonths(1);
            try {
                //总量统计到上月底，增量统计上月1个月的数据
                reports = generateClient(today.minusDays(1), firstDate);
                store(reports, clientMapper::clearMonth, clientMapper::insertMonth);
            } catch (Exception e) {
                log.error("The [employee-client] generated report exception of the last month,\n", e);
            }
        }
    }

    public void clientTodayReport() {
        List<EmployeeClientReport> reports;
        LocalDate today = LocalDate.now();
        try {
            //按今天统计
            reports = generateClient(today, today);
            store(reports, clientMapper::clearToday, clientMapper::insertToday);
        } catch (Exception e) {
            log.error("The [employee-client] generate report  exception today,\n", e);
        }
    }

    public void clearEmployeeReport(LocalDate date) {
        String pname = "p".concat(Integer.toString(date.getYear())).concat(Integer.toString(date.getMonth().getValue()));
        try {
            clientMapper.clearExpireByDay(pname);
        } catch (Exception e) {
            log.error("The [employee-client] clean day report expiration data  exception, date={}\n",
                    date,
                    e);
        }
        try {
            clientMapper.clearExpireByMonth(pname);
        } catch (Exception e) {
            log.error("The [employee-client] clean monthly report expiration data  exception date={}\n",
                    date,
                    e);
        }
        try {
            performanceMapper.clearExpireByDay(pname);
        } catch (Exception e) {
            log.error("The [employee-performance] clean day report expiration data  exception, date={}\n",
                    date,
                    e);
        }
        try {
            performanceMapper.clearExpireByMonth(pname);
        } catch (Exception e) {
            log.error("The [employee-performance] clean monthly report expiration data  exception date={}\n",
                    date,
                    e);
        }
    }

//    public void performancePeriodReport() {
//        LocalDate today = LocalDate.now();
//
//        String day7 = today.minusDays(7).toString();
//
//        String day30 = today.minusDays(30).toString();
//
//        List<EmployeePerformanceReport> reports;
//
//        try {
//            //按最近7天统计
//            reports = generatePerformance(today.toString(),day7,false);
//            reports.addAll(generatePerformance(today.toString(), day7,true));
//            store(reports, performanceMapper::clear7Days, performanceMapper::insert7days);
//        } catch (Exception e) {
//            log.error("The [employee-performance] generate report  exception of the last 7 days,\n", e);
//        }
//
//        try {
//            //按最近30天统计
//            reports = generatePerformance(today.toString(), day30,false);
//            reports.addAll(generatePerformance(today.toString(), day30,true));
//            store(reports, performanceMapper::clear30Days, performanceMapper::insert30Days);
//        } catch (Exception e) {
//            log.error("The [employee-performance] generate report  exception of the last 30 days,\n", e);
//        }
//
//        //月报表
//        if (today.getDayOfMonth() == 1) {
//            String firstDate = today.minusMonths(1).toString();
//            try {
//                //统计上月1个月的数据
//                reports = generatePerformance(today.toString(), firstDate,false);
//                reports.addAll(generatePerformance(today.toString(), firstDate,true));
//                store(reports, performanceMapper::clearMonth, performanceMapper::insertMonth);
//            } catch (Exception e) {
//                log.error("The [employee-client] generated report exception of the last month,\n", e);
//            }
//        }
//
//    }
//
//    public void performanceTodayReport() {
//        LocalDate today = LocalDate.now();
//        String tomorrow = today.plusDays(1).toString();
//        List<EmployeePerformanceReport> reports;
//
//        try {
//            //按今天统计
//            reports = generatePerformance(tomorrow, today.toString(),false);
//            reports.addAll(generatePerformance(tomorrow, today.toString(),true));
//            store(reports, performanceMapper::clearToday, performanceMapper::insertToday);
//        } catch (Exception e) {
//            log.error("The [employee-performance] generate report  exception today,\n", e);
//        }
//    }
//
//    public void testperformanceTodayReport(LocalDate today) {
//        String tomorrow = today.plusDays(1).toString();
//        List<EmployeePerformanceReport> reports;
//
//        try {
//            //按今天统计
//            reports = generatePerformance(tomorrow, today.toString(),false);
//            reports.addAll(generatePerformance(tomorrow, today.toString(),true));
//            reports.forEach(r->{
//                r.setTestCreateTime(today);
//            });
//            performanceMapper.testClearToday(today);
//            performanceMapper.testInsertToday(reports);
//        } catch (Exception e) {
//            log.error("The [employee-performance] generate report  exception today,\n", e);
//        }
//    }


    private <T extends Collection> void store(T t, IntSupplier clear, Function<T, Integer> insertor) {
        clear.getAsInt();
        if (CollectionUtils.isEmpty(t)) {
            return;
        }
        insertor.apply(t);
    }

//    public List<EmployeePerformanceReport> generatePerformance(String toDate, String period, boolean isBossFlag) {
//        if(!template.indexExists(EsConstants.ES_INDEX)){
//            template.createIndex(EsConstants.ES_INDEX);
//        }
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(
//                QueryBuilders.rangeQuery("time").gte(period).lt(toDate)
//        );
//        if(!isBossFlag){
//            boolQueryBuilder.filter(
//                    QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("employeeCompanyId","0"))
//            ).filter(
//                    QueryBuilders.scriptQuery(new Script("doc['companyId'].value==doc['employeeCompanyId'].value"))
//            );
//        }else{
//            boolQueryBuilder.filter(
//                    QueryBuilders.boolQuery().must(QueryBuilders.termQuery("employeeCompanyId","0"))
//            );
//        }
//        builder
//                .withIndices(EsConstants.ES_INDEX)
//                .withTypes(EsConstants.ES_TYPE_POOL)
//                .withSearchType(SearchType.COUNT)
//                .withQuery(
//                        boolQueryBuilder
//                )
//                .addAggregation(
//                        AggregationBuilders.terms("company").field("employeeCompanyId").size(0)
//                                .subAggregation(
//                                        AggregationBuilders.nested("buyer_aggs").path("buyer").subAggregation(
//                                                AggregationBuilders.terms("employee").field("buyer.employeeId").size(0).subAggregation(
//                                                        AggregationBuilders.reverseNested("tradeAggs").subAggregation(
//                                                                AggregationBuilders.terms("trade").field("type").size(0).subAggregation(
//                                                                        AggregationBuilders.sum("sum_trade_amt").field("orderAmt")
//                                                                ).subAggregation(
//                                                                        AggregationBuilders.sum("sum_real_amt").field("realAmt")
//                                                                ).subAggregation(
//                                                                        AggregationBuilders.nested("buyer_counts_aggs").path("buyer")
//                                                                                .subAggregation(
//                                                                                        AggregationBuilders.cardinality("distinct_counts")
//                                                                                                .field("buyer.id").precisionThreshold(3000)
//                                                                                )
//                                                                )
//                                                        )
//                                                )
//                                        )
//                                )
//                );
//        return template.query(builder.build(), this::parsePerformanceResult);
//    }

    private List<EmployeeClientReport> generateClient(LocalDate dateTo, LocalDate dateFrom) {
        List<EmployeeClientReport> total = customerMapper.queryTotalByEmployee(dateTo);
        List<EmployeeClientReport> newly = customerMapper.queryNewlyByEmployee(dateFrom, dateTo);
        Map<String, EmployeeClientReport> map = newly.stream().collect(Collectors.toMap(EmployeeClientReport::getEmployeeId, Function.identity()));
        total.forEach(
                i -> {
                    EmployeeClientReport report = map.remove(i.getEmployeeId());
                    if (report != null) {
                        i.setNewlyNum(report.getNewlyNum());
                    }
                }
        );
        total.addAll(map.values());
        return total;
    }

//    private List<EmployeeClientReport> parseClientResult(SearchResponse searchResponse) {
//        StringTerms companyTerms = searchResponse.getAggregations().get("company");
//        List<EmployeeClientReport> list = new ArrayList<>();
//        companyTerms.getBuckets().forEach(
//                c -> {
//                    String companyId = c.getKey().toString();
//                    StringTerms empTerms = c.getAggregations().get("employee");
//                    empTerms.getBuckets().forEach(
//                            e -> {
//                                EmployeeClientReport data = new EmployeeClientReport();
//                                InternalFilter totalFilter = e.getAggregations().get("total");
//                                InternalFilter newlyNumFilter = e.getAggregations().get("newlyNum");
//                                data.setCompanyId(companyId);
//                                data.setEmployeeId(e.getKey().toString());
//                                data.setTotal(totalFilter.getDocCount());
//                                data.setNewlyNum(newlyNumFilter.getDocCount());
//                                list.add(data);
//                            }
//                    );
//                }
//        );
//        return list;
//    }
//
//    private List<EmployeePerformanceReport> parsePerformanceResult(SearchResponse searchResponse) {
//        StringTerms companyTerms = searchResponse.getAggregations().get("company");
//        List<EmployeePerformanceReport> list = new ArrayList<>();
//        companyTerms.getBuckets().forEach(
//                c -> {
//                    InternalNested buyer_nested = c.getAggregations().get("buyer_aggs");
//                    StringTerms employeeTerms = buyer_nested.getAggregations().get("employee");
//                    employeeTerms.getBuckets().forEach(
//                            e -> {
//                                final EmployeePerformanceReport report = new EmployeePerformanceReport();
//                                list.add(report);
//                                report.setCompanyId(c.getKeyAsString());
//                                report.setEmployeeId(e.getKey().toString());
//                                StringTerms tradeTerms = ((InternalReverseNested) e.getAggregations().get("tradeAggs"))
//                                        .getAggregations().get("trade");
//                                tradeTerms.getBuckets().forEach(t -> {
//                                    DataSourceType type = DataSourceType.valueOf(t.getKeyAsString());
//                                    //单数
//                                    long orderCount = t.getDocCount();
//                                    Map<String, Aggregation> l = t.getAggregations().getAsMap();
//                                    InternalCardinality countCardinality = ((InternalNested) l.get("buyer_counts_aggs"))
//                                            .getAggregations().get("distinct_counts");
//                                    //客户数
//                                    long customerCount = countCardinality.getValue();
//                                    double db;
//
//                                    switch (type) {
//                                        case CREATE:
//                                            //订单
//                                            db = ((InternalSum) (l.get("sum_trade_amt"))).getValue();
//                                            BigDecimal amt = BigDecimal.valueOf(db).setScale(2, RoundingMode.HALF_UP);
//                                            report.setAmount(amt);
//                                            report.setOrderCount(orderCount);
//                                            report.setCustomerCount(customerCount);
//                                            break;
//                                        case RETURN:
//                                            //退单
//                                            db = ((InternalSum) l.get("sum_real_amt")).getValue();
//                                            BigDecimal returnAmt = BigDecimal.valueOf(db).setScale(2, RoundingMode.HALF_UP);
//                                            report.setReturnAmount(returnAmt);
//                                            report.setReturnCount(orderCount);
//                                            report.setReturnCustomerCount(customerCount);
//                                            break;
//                                        case PAY:
//                                            //支付
//                                            db = ((InternalSum) l.get("sum_real_amt")).getValue();
//                                            BigDecimal payAmt = new BigDecimal(db).setScale(2, RoundingMode.HALF_UP);
//                                            report.setPayAmount(payAmt);
//                                            report.setPayCount(orderCount);
//                                            report.setPayCustomerCount(customerCount);
//                                            break;
//                                    }
//
//                                });
//                            }
//                    );
//                }
//        );
//        return list;
//
//    }

    public void generateData(String param,LocalDate now) {
        if(StringUtils.isEmpty(param)){
            return ;
        }
        if(param.contains("0")){
            generateDay(now);
        }
        if(param.contains("1")){
            generateYesterday(now);
        }
        if(param.contains("2")){
            generateSeven(now);
        }
        if(param.contains("3")){
            generateThirty(now);
        }
        if(param.contains("4")){
            generateMonth(now);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void generateYesterday(LocalDate now){
        LocalDate yesterday = now.minusDays(1);
        //清空昨日报表
        performanceMapper.deleteTodayDay(yesterday);
        //生成昨日报表
        List<EmployeePerformanceReport> employeePerformanceReports = performanceMapper.collectThirdEmployeeTrade(DateUtil.computeDateIntervalDay(yesterday));
        employeePerformanceReports.addAll(performanceMapper.collectBossEmployeeTrade(DateUtil.computeDateIntervalDay(yesterday)));
        if (CollectionUtils.isEmpty(employeePerformanceReports)) {
            return;
        }

        employeePerformanceReports = employeePerformanceReports.stream()
                .filter(f -> f.getCompanyId() != null)
                .map(i-> {i.setTargetDate(yesterday);return i;})
                .collect(Collectors.toList());

        performanceMapper.insertDay(employeePerformanceReports);
    }

    @Transactional(rollbackFor = Exception.class)
    public void generateDay(LocalDate now){
        //清空今日报表
        performanceMapper.deleteTodayDay(now);
        //生成今天报表
        List<EmployeePerformanceReport> employeePerformanceReports = performanceMapper.collectThirdEmployeeTrade(DateUtil.computeDateIntervalDay(now));
        employeePerformanceReports.addAll(performanceMapper.collectBossEmployeeTrade(DateUtil.computeDateIntervalDay(now)));
        if (CollectionUtils.isEmpty(employeePerformanceReports)) {
            return;
        }

        employeePerformanceReports = employeePerformanceReports.stream()
                .filter(f -> f.getCompanyId() != null)
                .collect(Collectors.toList());

        performanceMapper.insertToday(employeePerformanceReports);
    }

    @Transactional(rollbackFor = Exception.class)
    public void generateSeven(LocalDate now){
        //清空最近日天报表
        performanceMapper.clear7Days();
        //生成最近七天报表
        List<EmployeePerformanceReport> employeePerformanceReports = performanceMapper.collectThirdEmployeeTrade(DateUtil.computeDateIntervalSeven(now));
        employeePerformanceReports.addAll(performanceMapper.collectBossEmployeeTrade(DateUtil.computeDateIntervalSeven(now)));
        if (CollectionUtils.isEmpty(employeePerformanceReports)) {
            return;
        }

        employeePerformanceReports = employeePerformanceReports.stream()
                .filter(f -> f.getCompanyId() != null)
                .collect(Collectors.toList());

        performanceMapper.insert7days(employeePerformanceReports);
    }

    @Transactional(rollbackFor = Exception.class)
    public void generateThirty(LocalDate now){

        //清空最近30日天报表
        performanceMapper.clear30Days();
        TradeCollect tradeCollect = DateUtil.computeDateIntervalThirtyDay(now);
        //生成最近30天报表
        List<EmployeePerformanceReport> employeePerformanceReports = performanceMapper.collectThirdEmployeeTrade(tradeCollect);
        employeePerformanceReports.addAll(performanceMapper.collectBossEmployeeTrade(tradeCollect));
        if (CollectionUtils.isEmpty(employeePerformanceReports)) {
            return;
        }

        employeePerformanceReports = employeePerformanceReports.stream()
                .filter(f -> f.getCompanyId() != null)
                .collect(Collectors.toList());

        performanceMapper.insert30Days(employeePerformanceReports);
    }

    @Transactional(rollbackFor = Exception.class)
    public void generateMonth(LocalDate now){
        //生成统计数据
        for (int i = 1; i < 7; i++) {
            //删除过期数据
            String oldYearMonth = DateUtil.yearMonth(now.minusMonths(i));
            performanceMapper.deleteRecentMonth(oldYearMonth);
            TradeCollect tradeCollect = DateUtil.computeDateIntervalMonthDay(i, now);
            List<EmployeePerformanceReport> employeePerformanceReports = performanceMapper.collectThirdEmployeeTrade(tradeCollect);
            employeePerformanceReports.addAll(performanceMapper.collectBossEmployeeTrade(tradeCollect));
            if (CollectionUtils.isEmpty(employeePerformanceReports)) {
                return;
            }
            employeePerformanceReports = employeePerformanceReports.stream().filter(f -> f.getCompanyId() != null).map(e -> {
                e.setTargetDate(tradeCollect.getEndDate());
                return e;}).collect(Collectors.toList());
            performanceMapper.insertMonth(employeePerformanceReports);
        }
    }
}

