package com.wanmi.ares.report.goods.service;

import com.wanmi.ares.enums.QueryDateCycle;
import com.wanmi.ares.report.goods.dao.GoodsTotalMapper;
import com.wanmi.ares.report.goods.dao.GoodsTotalRatioMapper;
import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.reponse.GoodsTotalResponse;
import com.wanmi.ares.report.goods.model.request.GoodsQueryRequest;
import com.wanmi.ares.report.goods.model.root.GoodsTotalRatioReport;
import com.wanmi.ares.source.service.StoreService;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ares.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Sku报表服务
 * Created by dyt on 2017/9/21.
 */
@Service
public class GoodsTotalService {


    @Autowired
    private GoodsTotalMapper goodsReportMapper;
    @Autowired
    private GoodsTotalRatioMapper goodsTotalRatioMapper;

    @Autowired
    private StoreService storeService;

    /**
     * 查询商品概览
     * @param request 参数
     * @return
     */
    public GoodsTotalResponse query(GoodsQueryRequest request) {
        GoodsTotalResponse total = new GoodsTotalResponse();
        LocalDate date = LocalDate.now();
        boolean isMonth = false;
        List<GoodsTotalResponse> goodsTotals;
        Integer month = request.getMonth();
        int selectType = request.getSelectType() == null ? QueryDateCycle.today.getValue() : request.getSelectType().getValue();
        GoodsQueryCriteria criteria = new GoodsQueryCriteria();
        if(StringUtils.isNotBlank(request.getCompanyId()) && (!Constants.bossId.equals(request.getCompanyId()))) {
            criteria.setCompanyId(request.getCompanyId());
        }else{
            criteria.setCompanyId("0");
        }
        if(month != null){//月份
            criteria.setTable("goods_total_ratio_month");
            criteria.setMonth(month);
        }else if(QueryDateCycle.yesterday.getValue() == selectType){//昨日
            criteria.setTable("goods_total_ratio_day");
            criteria.setDate( DateUtil.format(date.minusDays(1),DateUtil.FMT_DATE_1));
            goodsTotals = goodsReportMapper.findGoodsTotal(criteria);
            if (CollectionUtils.isNotEmpty(goodsTotals)) {
                total = goodsTotals.get(0);
            }
        }else if(QueryDateCycle.latest7Days.getValue() == selectType){//近7日
            criteria.setTable("goods_total_ratio_recent_seven");
        }else if(QueryDateCycle.latest30Days.getValue() == selectType){//近30日
            criteria.setTable("goods_total_ratio_recent_thirty");
        }else{//今日
            criteria.setTable("goods_total_ratio_day");
            criteria.setDate( DateUtil.format(date,DateUtil.FMT_DATE_1));
            goodsTotals = goodsReportMapper.findGoodsTotal(criteria);
            if (CollectionUtils.isNotEmpty(goodsTotals)) {
                total = goodsTotals.get(0);
            }
        }
        List<GoodsTotalRatioReport> ratioReports =  this.goodsTotalRatioMapper.queryGoodsTotalRatioReport(criteria);
        if(CollectionUtils.isNotEmpty(ratioReports)){
            total.setOrderConversion(ratioReports.get(0).getRatio());
        }
        return total;
    }

    /**
     * 全量聚合分析商品概览
     * @param addedEndTime 上下架分组块结束时间
     * @return
     */
//    private List<GoodsTotalResponse> aggsAll(String addedEndTime, String companyId){
//        return elasticsearchTemplate.query(
//                    commonAggsBuilder(companyId, GoodsTotalAggsResponse.getAggregationByAll(addedEndTime)).build(),
//                    GoodsTotalAggsResponse::buildByAll
//                ).getGoodsTotals();
//    }
//
//    /**
//     * 按商户分组，聚合商品统计
//     * @param addedEndTime 上下架分组块结束时间
//     * @return
//     */
//    public List<GoodsTotalResponse> aggsGroupCompany(String addedEndTime){
//        return elasticsearchTemplate.query(
//                    commonAggsBuilder(null, Collections.singletonList(GoodsTotalAggsResponse.getAggregationByCompanyGroup(addedEndTime))).build(),
//                    GoodsTotalAggsResponse::buildByCompanyGroup
//                ).getGoodsTotals();
//    }
//
//    /**
//     * 公共性私有方法
//     * @param companyId 公司信息ID
//     * @param agg 聚合块参数
//     * @return
//     */
//    private NativeSearchQueryBuilder commonAggsBuilder(String companyId, List<AbstractAggregationBuilder> agg){
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX_BASIC);
//        builder.withTypes(EsConstants.ES_TYPE_SKU);
//        builder.withSearchType(SearchType.COUNT);
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        bq.must(termQuery("delFlag", false));
//        if(StringUtils.isNotBlank(companyId) && (!Constants.bossId.equals(companyId))){
//            bq.must(termQuery("companyId", companyId));
//        }
//        builder.withQuery(bq);
//        agg.forEach(builder::addAggregation);
//        return builder;
//    }
//
//    /**
//     * 获取销售中商品数
//     * @param companyIds 多个公司信息ID
//     * @return
//     */
//    public Map<String, Long> getSaleTotalGroupCompany(Collection<String> companyIds, String date){
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX_BASIC);
//        builder.withTypes(EsConstants.ES_TYPE_SKU);
//        builder.withSearchType(SearchType.COUNT);
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        bq.must(termQuery("delFlag", false));
//        bq.must(termQuery("addedFlag", true));
//        bq.must(termQuery("auditStatus", CheckStatus.CHECKED.toValue()));
//        if(CollectionUtils.isNotEmpty(companyIds)){
//            bq.must(termsQuery("companyId", companyIds));
//        }
//        //销售日期小于或等于{参数时间}
//        if(StringUtils.isNotBlank(date)){
//            bq.must(rangeQuery("saleDate").lte(date));
//        }
//        builder.withQuery(bq);
//        builder.addAggregation(GoodsSaleTotalAggsResponse.getAggregationByCompanyGroup());
//        return elasticsearchTemplate.query(builder.build(), GoodsSaleTotalAggsResponse::buildByCompanyGroup).getSaleTotals();
//    }
//
//    /**
//     * 获取销售中商品数
//     * @param companyIds 多个公司信息ID
//     * @return
//     */
//    public Map<String, Long> getSaleTotal(Collection<String> companyIds){
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX_BASIC);
//        builder.withTypes(EsConstants.ES_TYPE_SKU);
//        builder.withSearchType(SearchType.COUNT);
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        bq.must(termQuery("delFlag", false));
//        bq.must(termQuery("addedFlag", true));
//        bq.must(termQuery("auditStatus", CheckStatus.CHECKED.toValue()));
//        if(CollectionUtils.isNotEmpty(companyIds)){
//            bq.must(termsQuery("companyId", companyIds));
//        }
//        builder.withQuery(bq);
//        return elasticsearchTemplate.query(builder.build(), GoodsSaleTotalAggsResponse::buildByAll).getSaleTotals();
//    }
//
//
//    /**
//     * 获取转化率
//     * @param begTime 开始时间
//     * @param endTime 结束时间
//     * @param companyId 公司信息ID
//     * @param isMonth 是否月份
//     * @return
//     */
//    private BigDecimal getConversion(LocalDate begTime, LocalDate endTime, String companyId, boolean isMonth){
//        String[] indexs = DateUtil.getEsIndexName(EsConstants.ES_FLOW_PREFIX, begTime, endTime);
//        //过滤掉不存在的index
//        List<String> indexArr = Arrays.stream(indexs).filter(elasticsearchTemplate::indexExists).collect(Collectors.toList());
//        if(CollectionUtils.isEmpty(indexArr)){
//            return BigDecimal.ZERO;
//        }
//
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        //计算UV浏览量
//        builder.withIndices(indexArr.toArray(new String[indexArr.size()]));
//        if (!isMonth) {
//            builder.withTypes(DateUtil.getEsTypeName(EsConstants.ES_FLOW_PREFIX, begTime, endTime));
//        }
//        builder.withSearchType(SearchType.COUNT);
//
//        if (StringUtils.isNotBlank(companyId) && (!Constants.bossId.equals(companyId))) {
//            bq.must(termQuery("id", companyId));
//        }
//        builder.withQuery(bq);
//        builder.addAggregation(AggregationBuilders.cardinality("uvCount").field("skuTotalUvUserIds").precisionThreshold(EsConstants.PRECISION_THRESHOLD));
//        builder.addAggregation(AggregationBuilders.terms("userGroup").size(0).field("skuTotalUvUserIds"));
//        GoodsUvAggsResponse uvAggsResponse = elasticsearchTemplate.query(builder.build(), GoodsUvAggsResponse::buildUvCount);
//        Long uvCount = uvAggsResponse.getUvCount();
//        if(uvCount == 0){
//            return BigDecimal.ZERO;
//        }
//        //通过商品详情浏览过来的用户集合
//        Set<String> viewCustomer = uvAggsResponse.getUvCustomers();
//
//        //获取去重的客户
//        builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX);
//        builder.withTypes(EsConstants.ES_TYPE_POOL);
//        builder.withSearchType(SearchType.COUNT);
//        bq = QueryBuilders.boolQuery();
//        bq.must(rangeQuery("time").gte(DateUtil.format(begTime, DateUtil.FMT_DATE_1)).lte(DateUtil.format(endTime, DateUtil.FMT_DATE_1)));
//        bq.must(termQuery("type", DataSourceType.CREATE.toValue()));
//        if(StringUtils.isNotBlank(companyId) && (!Constants.bossId.equals(companyId))){
//            bq.must(termQuery("companyId", companyId));
//        }
//        builder.withQuery(bq);
//        builder.addAggregation(AggregationBuilders.terms("userGroup").size(0).field("customerId"));
//        Set<String> orderCustomer = elasticsearchTemplate.query(builder.build(), GoodsUvAggsResponse::buildUvCount).getUvCustomers();
//        //过滤出从商品浏览的客户进行统计
//        Long uvOrderCount = orderCustomer.stream().filter(viewCustomer::contains).count();
//
//        //((UV下单数/UV浏览量)[精确至4位,四舍五入]*100)[精确至2位,四舍五入]
//        return BigDecimal.valueOf(uvOrderCount).divide(BigDecimal.valueOf(uvCount), 4 ,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
//    }
}
