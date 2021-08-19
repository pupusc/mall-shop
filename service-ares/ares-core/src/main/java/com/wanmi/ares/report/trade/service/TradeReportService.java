package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.report.customer.dao.ReplayStoreMapper;
import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.report.flow.service.FlowReportService;
import com.wanmi.ares.report.trade.dao.TradeDayMapper;
import com.wanmi.ares.report.trade.dao.TradeReportMapper;
import com.wanmi.ares.report.trade.model.reponse.TradeReponse;
import com.wanmi.ares.report.trade.model.request.TradeReportRequest;
import com.wanmi.ares.report.trade.model.root.TradeDay;
import com.wanmi.ares.report.trade.model.root.TradeReport;
import com.wanmi.ares.source.model.root.Store;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.ParseData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 交易统计service
 * Created by sunkun on 2017/9/26.
 */
@Service
public class TradeReportService {

    @Resource
    private FlowReportService flowReportService;

    @Resource
    private TradeReportMapper tradeReportMapper;

    @Resource
    private ReplayStoreMapper replayStoreMapper;

    @Resource
    private TradeDayService tradeDayService;

    @Resource
    private TradeSevenService tradeSevenService;

    @Resource
    private TradeThirtyService tradeThirtyService;

    @Resource
    private TradeMonthService tradeMonthService;

    @Resource
    private TradeDayMapper tradeDayMapper;

    /**
     * 生成交易日报表
     */
    public void generate(LocalDate date) {
        System.err.println("交易报表生成测试打印开始-------" + LocalDateTime.now());
        List<Store> storeList = replayStoreMapper.queryAllStoreByFlag(false);
        List<String> companyIds = new ArrayList<>();
        companyIds.add(Constants.companyId);
        companyIds.addAll(storeList.stream().map(Store::getCompanyInfoId).filter(Objects::nonNull).collect(Collectors.toList()));
//        companyIds.forEach(id -> {
//            TradeReport tradeReport = getTradeReportByData(id, date, date);
//            tradeReport.setDate(date);
//            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//            nativeSearchQueryBuilder.withIndices(EsConstants.ES_FLOW_PREFIX.concat(String.valueOf(date.format(DateTimeFormatter.ofPattern("yyyyMM")))));
//            nativeSearchQueryBuilder.withTypes(EsConstants.ES_FLOW_PREFIX.concat(String.valueOf(date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))));
//            QueryBuilder queryBuilder = QueryBuilders.matchQuery("id", id);
//            nativeSearchQueryBuilder.withQuery(queryBuilder);
//            //查询当天流量报表
//            List<FlowReport> flowReports = this.template.queryForList(nativeSearchQueryBuilder.build(), FlowReport.class);
//            tradeReport = getTradeReport1(tradeReport, flowReports, id);
//            //存储mysql
//            tradeReportMapper.deleteTrade(id + "-" + DateUtil.format(date, DateUtil.FMT_DATE_2));
//            tradeReportMapper.saveTrade(tradeReport);
//        });
        System.err.println("交易报表生成测试打印结束-------" + LocalDateTime.now());
    }

    /**
     * 获取交易报表概况
     *
     * @param request
     * @return
     */
//    public TradeReponse getOverview(TradeReportRequest request) {
//        request.setSortOrder(SortOrder.ASC);
//        List<TradeReport> tradeReports = tradeReportMapper.queryTrade(request);
//        tradeReports = isQueryTodayData(tradeReports, request);
//        if (CollectionUtils.isEmpty(tradeReports)) {
//            return null;
//        }
//        TradeReport tradeReport = countTrade(tradeReports);
//        TradeReponse tradeReponse = new TradeReponse();
//        BeanUtils.copyProperties(tradeReport, tradeReponse);
//        FlowReportRequest flowReportRequest = new FlowReportRequest();
//        flowReportRequest.setCompanyId(request.getCompanyId());
//        flowReportRequest.setBeginDate(request.getBeginDate());
//        flowReportRequest.setEndDate(request.getEndDate());
//        List<FlowReport> flowReports = flowReportService.getFlowList(flowReportRequest);
//        Set<String> userIds = new HashSet<>();
//        flowReports.forEach(info -> {
//            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//            nativeSearchQueryBuilder.withIndices(EsConstants.ES_FLOW_PREFIX.concat(DateUtil.format(info.getDate(), DateUtil.FMT_MONTH_1)));
//            nativeSearchQueryBuilder.withTypes(EsConstants.ES_FLOW_PREFIX.concat(DateUtil.format(info.getDate(), DateUtil.FMT_DATE_2)));
//            QueryBuilder queryBuilder1 = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("id", info.getId()))
//                    .must(QueryBuilders.termQuery("date", info.getDate()));
//            nativeSearchQueryBuilder.withQuery(queryBuilder1);
//            List<FlowReport> flowReportList = null;
//            if (this.template.indexExists(EsConstants.ES_FLOW_PREFIX.concat(DateUtil.format(info.getDate(), DateUtil.FMT_MONTH_1)))) {
//                flowReportList = this.template.queryForList(nativeSearchQueryBuilder.build(), FlowReport.class);
//            }
//            flowReportRequest.setBeginDate(info.getDate());
//            flowReportRequest.setEndDate(info.getDate());
//
//            if (CollectionUtils.isNotEmpty(flowReportList)) {
//                userIds.addAll(flowReportList.get(0).getUserIds());
//            }
//        });
//        tradeReponse.setTotalUv((long) userIds.size());
//        return tradeReponse;
//    }

    /**
     * 汇总店铺交易数据
     *
     * @param tradeReports
     * @param flowReports
     * @return
     */
//    public TradeReponse countStoreTrade(String id,LocalDate beginDate,LocalDate endDate, List<FlowReport> flowReports) {
//        TradeReport tradeReport = countTradeNotFlow(id,beginDate,endDate, flowReports);
//        Set<String> userIds = new HashSet<>();
//        TradeReponse tradeReponse = new TradeReponse();
//        BeanUtils.copyProperties(tradeReport, tradeReponse);
//        flowReports.stream().map(FlowReport::getUserIds).forEach(ids -> {
//            userIds.addAll(ids);
//        });
//        tradeReponse.setTotalUv((long) userIds.size());
//        return tradeReponse;
//    }


    /**
     * 获取店铺交易报表
     *
     * @param request
     * @return
     */
//    public Page<TradeReponse> getStoreTradePage(TradeReportRequest request) {
//        if (request.getSortName().equals("date")) {
//            request.setSortName("orderCount");
//        }
//        request.setPageNum(request.getPageNum() * request.getPageSize() - request.getPageSize());
//        //根据供应商分组排序查询交易报表
//        List<TradeReport> tradeReports = tradeReportMapper.queryStoreTradePage(request);
//        if (CollectionUtils.isEmpty(tradeReports) && DateUtil.format(request.getBeginDate(), DateUtil.FMT_DATE_1).equals(DateUtil.format(LocalDate.now(), DateUtil.FMT_DATE_1))) {
//            List<DataPool> pools = this.getData(request.getCompanyId(), request.getBeginDate(), request.getEndDate());
//            List<String> companyIds = pools.stream().map(DataPool::getCompanyId).distinct().collect(Collectors.toList());
//            companyIds.forEach(id -> {
//                TradeReport tradeReport = getTradeReportByData(id, request.getBeginDate(), request.getEndDate());
//                tradeReport.setId(id);
//                tradeReport.setDate(LocalDate.now());
//                tradeReports.add(tradeReport);
//            });
//        }
//        List<String> companIds = new ArrayList<>();
//        //筛选出当前页供应商id
//        companIds.addAll(tradeReports.stream().map(TradeReport::getId).filter(Objects::nonNull).collect(Collectors.toList()));
//        if (CollectionUtils.isEmpty(companIds)) {
//            return new PageImpl<TradeReponse>(new ArrayList<>(), request.getPageable(), 0);
//        }
//        //根据供应商id列表查询店铺
//        List<Store> stores = replayStoreMapper.queryByCompanyIds(companIds);
//        //查询当前页所有供应商流量
//        FlowReportRequest flowReportRequest = new FlowReportRequest();
//        flowReportRequest.setCompanyIds(companIds);
//        flowReportRequest.setBeginDate(request.getBeginDate());
//        flowReportRequest.setEndDate(request.getEndDate());
//        List<FlowReport> flowReports = flowReportService.getEsFlowListByCompanyIds(flowReportRequest);
//        //组装店铺交易列表数据
////        List<TradeReport> finalTradeReports = tradeReports;
//        List<TradeReponse> tradeReponses = companIds.stream().map(id -> {
//            List<TradeReport> finalTradeReports = tradeReports.stream().filter(tradeReport -> id.equals(tradeReport.getId())).collect(Collectors.toList());
//            finalTradeReports.get(0).setDate(request.getBeginDate());
//            finalTradeReports.get(finalTradeReports.size() - 1).setDate(request.getEndDate());
//            TradeReponse tradeReponse = this.countStoreTrade(id,request.getBeginDate(),request.getEndDate(),
//                    flowReports.stream().filter(flowReport -> id.equals(flowReport.getId())).collect(Collectors.toList()));
//            Store store = stores.stream().filter(s -> id.equals(s.getCompanyInfoId())).findFirst().orElse(null);
//            if (Objects.nonNull(store)) {
//                tradeReponse.setTitle(store.getStoreName());
//            }
//            return tradeReponse;
//        }).filter(Objects::nonNull).collect(Collectors.toList());
//        request.setPageNum(request.getPageNum() / request.getPageSize());
//        return new PageImpl<TradeReponse>(tradeReponses, request.getPageable(), tradeReportMapper.countTradePageByStore(request));
//    }


    /**
     * 查询交易报表列表数据
     *
     * @param request
     * @return
     */
    public List<TradeReponse> getList(TradeReportRequest request) {
        List<TradeDay> tradeDays = tradeDayMapper.listTradeDay(request);
        List<TradeReport> tradeReports = tradeDays.stream().map(ParseData::parseDataReport).collect(Collectors.toList());
//        List<TradeReport> tradeReports = tradeReportMapper.queryTrade(request);
//        tradeReports = isQueryTodayData(tradeReports, request);
        List<TradeReponse> tradeReponses = new ArrayList<>();
        if (CollectionUtils.isEmpty(tradeReports)) {
            return new ArrayList<>();
        }
//        if (request.getIsWeek()) {
//            tradeReponses = getWeekList(tradeReports);
//        } else {
//            for (TradeReport info : tradeReports) {
//                TradeReponse tradeReponse = new TradeReponse();
//                BeanUtils.copyProperties(info, tradeReponse);
//                tradeReponse.setTitle(info.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).concat(DateUtil.getWeekStr(info.getDate())));
//                tradeReponse.setTime(info.getDate());
//                tradeReponses.add(tradeReponse);
//            }
//        }
        for (TradeReport info : tradeReports) {
            TradeReponse tradeReponse = new TradeReponse();
            BeanUtils.copyProperties(info, tradeReponse);
            tradeReponse.setTitle(info.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).concat(DateUtil.getWeekStr(info.getDate())));
            tradeReponse.setTime(info.getDate());
            tradeReponses.add(tradeReponse);
        }
        return tradeReponses;
    }

//    private List<TradeReport> isQueryTodayData(List<TradeReport> list, TradeReportRequest request) {
//        if (list.size() == 0 && DateUtil.format(request.getBeginDate(), DateUtil.FMT_DATE_1).equals(DateUtil.format(LocalDate.now(), DateUtil.FMT_DATE_1))) {
//            FlowReportRequest flowReportRequest = new FlowReportRequest();
//            BeanUtils.copyProperties(request, flowReportRequest);
//            TradeReport tradeReport = getTradeReportByData(request.getCompanyId(), request.getBeginDate(), request.getEndDate());
//            tradeReport.setDate(request.getBeginDate());
//            flowReportRequest.setSortName("date");
//            List<FlowReport> flowReportList = flowReportService.getEsFlowList(flowReportRequest);
//            tradeReport = getTradeReport1(tradeReport, flowReportList, request.getCompanyId());
//            list.add(tradeReport);
//        }
//        return list;
//    }

    /**
     * 获取交易周报表
     *
     * @param tradeReportList
     * @return
     */
//    public List<TradeReponse> getWeekList(List<TradeReport> tradeReportList) {
//        List<TradeReponse> tradeReponseList = new ArrayList<>();
//        List<TradeReport> tempList = new ArrayList<>();
//        List<LocalDate> dataList = DateUtil.getDateDiff(tradeReportList.get(0).getDate(), tradeReportList.get(tradeReportList.size() - 1).getDate());
//        dataList.forEach(date -> {
//            List<TradeReport> newTradeReports = tradeReportList.stream().filter(info -> info.getDate().equals(date)).collect(Collectors.toList());
//            if (7 - date.getDayOfWeek().getValue() > 0) {
//                if (CollectionUtils.isNotEmpty(newTradeReports)) {
//                    tempList.add(newTradeReports.get(0));
//                }
//            } else {
//                if (CollectionUtils.isNotEmpty(newTradeReports)) {
//                    tempList.add(newTradeReports.get(0));
//                }
//                if (CollectionUtils.isNotEmpty(tempList)) {
//                    TradeReport tradeReport = countTrade(tempList);
//                    TradeReponse tradeReponse = new TradeReponse();
//                    BeanUtils.copyProperties(tradeReport, tradeReponse);
//                    tradeReponse.setTitle(tempList.get(0).getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "-"
//                            + tempList.get(tempList.size() - 1).getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
//                    tradeReponseList.add(tradeReponse);
//                    tempList.clear();
//                }
//            }
//        });
//        if (CollectionUtils.isNotEmpty(tempList)) {
//            TradeReport tradeReport = countTrade(tempList);
//            TradeReponse tradeReponse = new TradeReponse();
//            BeanUtils.copyProperties(tradeReport, tradeReponse);
//            tradeReponse.setTitle(tempList.get(0).getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "-"
//                    + tempList.get(tempList.size() - 1).getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
//            tradeReponseList.add(tradeReponse);
//        }
//        return tradeReponseList;
//    }

    /**
     * 统计交易报表
     *
     * @param list
     * @return
     */
//    private TradeReport countTrade(List<TradeReport> list) {
//        TradeReport first = list.get(0);
//        TradeReport last = list.get(list.size() - 1);
//        TradeReport tradeReport1 = getTradeReportByData(first.getId(), first.getDate(), last.getDate());
//        FlowReportRequest flowReportRequest = new FlowReportRequest();
//        flowReportRequest.setCompanyId(first.getId());
//        flowReportRequest.setBeginDate(first.getDate());
//        flowReportRequest.setEndDate(last.getDate());
//        List<FlowReport> flowReports = flowReportService.getFlowList(flowReportRequest);
//        TradeReport tradeReport = getTradeReport1(tradeReport1, flowReports, first.getId());
//        return tradeReport;
//    }

    /**
     * 统计交易报表
     *
     * @param list
     * @return
     */
//    private TradeReport countTradeNotFlow(String id,LocalDate beginDate,LocalDate endDate, List<FlowReport> flowReports) {
////        TradeReport first = list.get(0);
////        TradeReport last = list.get(list.size() - 1);
//        TradeReport tradeReport1 = getTradeReportByData(id, beginDate, endDate);
//        FlowReportRequest flowReportRequest = new FlowReportRequest();
//        flowReportRequest.setCompanyId(id);
//        flowReportRequest.setBeginDate(beginDate);
//        flowReportRequest.setEndDate(endDate);
//        TradeReport tradeReport = getTradeReport1(tradeReport1, flowReports, id);
//        return tradeReport;
//    }


    /**
     * 组装交易报表
     *
     * @param dataPoolList
     * @param flowReportList
     * @param companyId
     * @return
     */
//    private TradeReport getTradeReport(List<DataPool> dataPoolList, List<FlowReport> flowReportList, String companyId) {
//        DecimalFormat df = new DecimalFormat("#.00");
//        //创建订单列表
//        List<DataPool> orderList = dataPoolList.stream().filter(info -> info.getType() == DataSourceType.CREATE && info.getCompanyId().equals(companyId)).collect(Collectors.toList());
//        //支付订单列表
//        List<DataPool> payOrderList = dataPoolList.stream().filter(info -> info.getType() == DataSourceType.PAY && info.getCompanyId().equals(companyId)).collect(Collectors.toList());
//        //退单列表
//        List<DataPool> returnOrderList = dataPoolList.stream().filter(info -> info.getType() == DataSourceType.RETURN && info.getCompanyId().equals(companyId)).collect(Collectors.toList());
//        TradeReport tradeReport = new TradeReport();
//        tradeReport.setOrderCount((long) orderList.size());
//        tradeReport.setOrderNum(orderList.stream().map(DataPool::getCustomerId).distinct().count());
//        if (orderList != null && orderList.size() > 0) {
//            tradeReport.setOrderCount((long) orderList.size());
//            tradeReport.setOrderNum(orderList.stream().map(DataPool::getBuyer).map(Customer::getId).distinct().count());
//            tradeReport.setOrderAmt(this.getAmt(orderList));
//        }
//        if (payOrderList != null && payOrderList.size() > 0) {
//            tradeReport.setPayOrderCount((long) payOrderList.size());
//            tradeReport.setPayOrderNum(payOrderList.stream().map(DataPool::getBuyer).map(Customer::getId).distinct().count());
//            tradeReport.setPayOrderAmt(this.getAmt(payOrderList));
//        }
//        if (returnOrderList != null && returnOrderList.size() > 0) {
//            tradeReport.setReturnOrderCount((long) returnOrderList.size());
//            tradeReport.setReturnOrderNum(returnOrderList.stream().map(DataPool::getBuyer).map(Customer::getId).distinct().count());
//            tradeReport.setReturnOrderAmt(this.getAmt(returnOrderList));
//        }
//        //下单金额/下单人数
//        tradeReport.setCustomerUnitPrice(tradeReport.getOrderNum() == 0 ? new BigDecimal("0") : tradeReport.getOrderAmt().divide(new BigDecimal(tradeReport.getOrderNum()), 2));
//        //下单金额/下单笔数
//        tradeReport.setEveryUnitPrice(tradeReport.getOrderCount() == 0 ? new BigDecimal("0") : tradeReport.getOrderAmt().divide(new BigDecimal(tradeReport.getOrderCount()), 2));
//        Set<String> totalUvIds = new HashSet<>();
//        flowReportList.forEach(info -> {
//            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//            nativeSearchQueryBuilder.withIndices(EsConstants.ES_FLOW_PREFIX.concat(DateUtil.format(info.getDate(), DateUtil.FMT_MONTH_1)));
//            nativeSearchQueryBuilder.withTypes(EsConstants.ES_FLOW_PREFIX.concat(DateUtil.format(info.getDate(), DateUtil.FMT_DATE_2)));
//            QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("id", info.getId()))
//                    .must(QueryBuilders.termQuery("date", info.getDate()));
//            nativeSearchQueryBuilder.withQuery(queryBuilder);
//            List<FlowReport> flowReports = this.template.queryForList(nativeSearchQueryBuilder.build(), FlowReport.class);
//            if (CollectionUtils.isNotEmpty(flowReports)) {
//                totalUvIds.addAll(flowReports.get(0).getUserIds());
//            }
//        });
//        //下单转化率：统计时间内，下单人数/访客数UV
//        if (totalUvIds.size() == 0 && tradeReport.getOrderNum() > 0) {
//            tradeReport.setOrderConversionRate(new BigDecimal(1));
//        } else {
//            tradeReport.setOrderConversionRate(totalUvIds.size() == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(tradeReport.getOrderNum() / totalUvIds.size()));
//        }
//        //付款转化率：统计时间内，付款人数/下单人数
//        if (tradeReport.getOrderNum() == 0 && tradeReport.getPayOrderNum() > 0) {
//            tradeReport.setPayOrderConversionRate(new BigDecimal(1));
//        } else {
//            tradeReport.setPayOrderConversionRate(tradeReport.getOrderNum() == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(tradeReport.getPayOrderNum() / tradeReport.getOrderNum()));
//        }
//        //全店转化率：统计时间内，付款人数/访客数UV
//        if (totalUvIds.size() == 0 && tradeReport.getPayOrderNum() > 0) {
//            tradeReport.setWholeStoreConversionRate(new BigDecimal(1));
//        } else {
//            tradeReport.setWholeStoreConversionRate(totalUvIds.size() == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(tradeReport.getPayOrderNum() / totalUvIds.size()));
//        }
//        tradeReport.setId(companyId);
//        return tradeReport;
//    }

    /**
     * 组装交易报表
     *
     * @param tradeReport
     * @param flowReportList
     * @param companyId
     * @return
     */
    private TradeReport getTradeReport1(TradeReport tradeReport, List<FlowReport> flowReportList, String companyId) {
        DecimalFormat df = new DecimalFormat("#.00");
        //下单金额/下单人数
        tradeReport.setCustomerUnitPrice(tradeReport.getOrderNum() == 0 ? new BigDecimal("0") : tradeReport.getOrderAmt().divide(new BigDecimal(tradeReport.getOrderNum()), 2, RoundingMode.HALF_UP));
        //下单金额/下单笔数
        tradeReport.setEveryUnitPrice(tradeReport.getOrderCount() == 0 ? new BigDecimal("0") : tradeReport.getOrderAmt().divide(new BigDecimal(tradeReport.getOrderCount()), 2, RoundingMode.HALF_UP));
        Set<String> totalUvIds = new HashSet<>();
//        flowReportList.forEach(info -> {
//            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//            nativeSearchQueryBuilder.withIndices(EsConstants.ES_FLOW_PREFIX.concat(DateUtil.format(info.getDate(), DateUtil.FMT_MONTH_1)));
//            nativeSearchQueryBuilder.withTypes(EsConstants.ES_FLOW_PREFIX.concat(DateUtil.format(info.getDate(), DateUtil.FMT_DATE_2)));
//            QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("id", info.getId()))
//                    .must(QueryBuilders.termQuery("date", info.getDate()));
//            nativeSearchQueryBuilder.withQuery(queryBuilder);
//            List<FlowReport> flowReports = null;
//            if (this.template.indexExists(EsConstants.ES_FLOW_PREFIX.concat(DateUtil.format(info.getDate(), DateUtil.FMT_MONTH_1)))) {
//                flowReports = this.template.queryForList(nativeSearchQueryBuilder.build(), FlowReport.class);
//            }
//            if (CollectionUtils.isNotEmpty(flowReports)) {
//                totalUvIds.addAll(flowReports.get(0).getUserIds());
//            }
//        });
        //下单转化率：统计时间内，下单人数/访客数UV
        if (tradeReport.getOrderNum() > 0) {
            tradeReport.setOrderConversionRate(new BigDecimal(100));
        } else {
            tradeReport.setOrderConversionRate(BigDecimal.ZERO);
            if (tradeReport.getOrderConversionRate().compareTo(new BigDecimal(100)) > 0) {
                tradeReport.setOrderConversionRate(new BigDecimal(100));
            }
        }
        //付款转化率：统计时间内，付款人数/下单人数
        if (tradeReport.getOrderNum() == 0 && tradeReport.getPayOrderNum() > 0) {
            tradeReport.setPayOrderConversionRate(new BigDecimal(100));
        } else {
            tradeReport.setPayOrderConversionRate(tradeReport.getOrderNum() == 0 ? BigDecimal.ZERO : new BigDecimal(tradeReport.getPayOrderNum() * 100).divide(new BigDecimal(tradeReport.getOrderNum()), 2, RoundingMode.HALF_UP));
            if (tradeReport.getPayOrderConversionRate().compareTo(new BigDecimal(100)) > 0) {
                tradeReport.setPayOrderConversionRate(new BigDecimal(100));
            }
        }
        //全店转化率：统计时间内，付款人数/访客数UV
        if (tradeReport.getPayOrderNum() > 0) {
            tradeReport.setWholeStoreConversionRate(new BigDecimal(100));
        } else {
            tradeReport.setWholeStoreConversionRate(BigDecimal.ZERO);
            if (tradeReport.getWholeStoreConversionRate().compareTo(new BigDecimal(100)) > 0) {
                tradeReport.setWholeStoreConversionRate(new BigDecimal(100));
            }
        }
        tradeReport.setId(companyId);
        return tradeReport;
    }

    /**
     * 查询数据池数据
     *
     * @param companyId
     * @param beginDate
     * @param endDate
     * @return
     */
//    private List<DataPool> getData(String companyId, LocalDate beginDate, LocalDate endDate) {
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        QueryBuilder queryBuilder = null;
//        if("0".equals(companyId)){
//            queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate));
//        }else{
//            queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate))
//                    .must(QueryBuilders.matchQuery("companyId", companyId));
//        }
//        nativeSearchQueryBuilder.withQuery(queryBuilder);
//        nativeSearchQueryBuilder.withIndices(EsConstants.ES_INDEX);
//        nativeSearchQueryBuilder.withTypes(EsConstants.ES_TYPE_POOL);
//        int pageSize = (int) this.template.count(nativeSearchQueryBuilder.build());
//        if (pageSize == 0) {
//            return new ArrayList<DataPool>();
//        }
//        nativeSearchQueryBuilder.withPageable(new PageRequest(0, pageSize));
//        //查询当天数据池数据
//        List<DataPool> list = this.template.queryForList(nativeSearchQueryBuilder.build(), DataPool.class);
//        return list;
//    }

//    private TradeReport getTradeReportByData(String companyId, LocalDate beginDate, LocalDate endDate) {
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        QueryBuilder queryBuilder = null;
//        if ("0".equals(companyId)) {
//            queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate));
//        } else {
//            queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate))
//                    .must(QueryBuilders.matchQuery("companyId", companyId));
//        }
//        nativeSearchQueryBuilder.withQuery(queryBuilder);
//        nativeSearchQueryBuilder.withIndices(EsConstants.ES_INDEX);
//        nativeSearchQueryBuilder.withTypes(EsConstants.ES_TYPE_POOL);
//        nativeSearchQueryBuilder.withSearchType(SearchType.COUNT);
//        AbstractAggregationBuilder abstractAggregationBuilder = AggregationBuilders.terms("orderType").field("type").size(0)
//                .subAggregation(AggregationBuilders.sum("orderAmtSum").field("orderAmt")).size(0)
//                .subAggregation(AggregationBuilders.sum("realAmtSum").field("realAmt")).size(0)
//                .subAggregation(AggregationBuilders.cardinality("num").field("customerId")).size(0);
//        nativeSearchQueryBuilder.addAggregation(abstractAggregationBuilder);
//        TradeAggsResponse tradeAggsResponse = this.template.query(nativeSearchQueryBuilder.build(), TradeAggsResponse::build);
//        return tradeAggsResponse.getTradeReport();
//    }

    /**
     * 查询交易报表分页数据
     *
     * @param request
     * @return
     */
//    public Page<TradeReport> getPage(TradeReportRequest request) {
//        request.setPageNum(request.getPageNum() * request.getPageSize() - request.getPageSize());
//        List<TradeReport> list = tradeReportMapper.queryTradePage(request);
//        list = isQueryTodayData(list, request);
//        request.setPageNum(request.getPageNum() / request.getPageSize());
//        return new PageImpl<TradeReport>(list, request.getPageable(), tradeReportMapper.queryTradeCount(request));
//    }


    /**
     * 组装交易报表数据
     *
     * @param list
     * @return
     */
//    private void saveTradeReport(List<DataPool> list, LocalDate time) {
//        DecimalFormat df = new DecimalFormat("#.00");
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        nativeSearchQueryBuilder.withIndices(EsConstants.ES_FLOW_PREFIX.concat(String.valueOf(time.format(DateTimeFormatter.ofPattern("yyyyMM")))));
//        nativeSearchQueryBuilder.withTypes(EsConstants.ES_FLOW_PREFIX.concat(String.valueOf(time.format(DateTimeFormatter.ofPattern("yyyyMMdd")))));
//
//        if (list == null || list.size() == 0) {
//            return;
//        }
//        List<String> companyIds = list.stream().map(DataPool::getCompanyId).distinct().collect(Collectors.toList());
//        IndexQuery indexQuery = new IndexQuery();
//        indexQuery.setIndexName(EsConstants.ES_TRADE_PREFIX.concat(String.valueOf(time.format(DateTimeFormatter.ofPattern("yyyyMM")))));
//        indexQuery.setType(EsConstants.ES_TRADE_PREFIX.concat(String.valueOf(time.format(DateTimeFormatter.ofPattern("yyyyMMdd")))));
//        companyIds.forEach(id -> {
//            QueryBuilder queryBuilder = QueryBuilders.matchQuery("id", id);
//            nativeSearchQueryBuilder.withQuery(queryBuilder);
//            //查询当天流量报表
//            List<FlowReport> flowReports = this.template.queryForList(nativeSearchQueryBuilder.build(), FlowReport.class);
//            TradeReport tradeReport = getTradeReport(list, flowReports, id);
//
//            tradeReport.setId(id);
//            tradeReport.setDate(time);
//            indexQuery.setId(tradeReport.getId());
//            indexQuery.setObject(tradeReport);
//            this.template.delete(EsConstants.ES_TRADE_PREFIX.concat(String.valueOf(time.format(DateTimeFormatter.ofPattern("yyyyMM")))), EsConstants.ES_TRADE_PREFIX.concat(String.valueOf(time.format(DateTimeFormatter.ofPattern("yyyyMMdd")))), id);
//            this.template.index(indexQuery);
//            tradeReportMapper.deleteTrade("1-" + DateUtil.format(tradeReport.getDate(), DateUtil.FMT_DATE_2));
//            tradeReportMapper.saveTrade(tradeReport);
//        });
//    }

//    private BigDecimal getAmt(List<DataPool> list) {
//        BigDecimal amt = new BigDecimal("0");
//        for (DataPool d : list) {
//            switch (d.getType()) {
//                case CREATE:
//                    amt = amt.add(d.getOrderAmt());
//                    break;
//                default:
//                    amt = amt.add(d.getRealAmt());
//                    break;
//            }
//        }
//        return amt;
//    }

    /**
     * 清理
     *
     * @param date
     */
//    public void clearTradeReport(LocalDate date) {
//        tradeReportMapper.clearTradeReport(DateUtil.format(date, DateUtil.FMT_DATE_1));
//    }


    /**
     * 清理,按月清
     */
//    private void clear() {
//        LocalDate endDate = LocalDate.now().minusYears(1).minusMonths(1);//往前推一年的时间
//        LocalDate begDate = endDate.minusMonths(5);//再往前推5个月，避免因某种原因漏删索引
//        String[] indexNames = DateUtil.getEsIndexName(EsConstants.ES_TRADE_PREFIX, begDate, endDate);
//        if (indexNames.length > 0) {
//            for (String index : indexNames) {
//                template.deleteIndex(index);
//            }
//        }
//    }
    public void generateData(String param, LocalDate now) {
        if (StringUtils.isEmpty(param)) {
            return;
        }

        if (param.contains("0")) {
            today(now);
        }
        if (param.contains("1")) {
            yesterday(now);
        }
        if (param.contains("2")) {
            seven(now);
        }
        if (param.contains("3")) {
            thirty(now);
        }
        if (param.contains("4")) {
            month(now);
        }
    }

    public void generateData1(LocalDate localDate) {
        today(localDate);
    }

    public void yesterday(LocalDate date) {
        tradeDayService.reduceYesterdayTradeBase(date);
    }

    public void today(LocalDate date) {
        tradeDayService.reduceTradeBase(date);
    }

    public void seven(LocalDate date) {
        tradeSevenService.reduceTradeBase(date);
    }

    public void thirty(LocalDate date) {
        tradeThirtyService.reduceTradeBase(date);
    }

    public void month(LocalDate date) {
        tradeMonthService.reduceTradeBase(date);
    }
}
