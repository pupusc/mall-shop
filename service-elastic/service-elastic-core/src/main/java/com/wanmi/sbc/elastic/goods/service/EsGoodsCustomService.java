package com.wanmi.sbc.elastic.goods.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.common.CommonEsSearchCriteriaBuilder;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 8:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Service
public class EsGoodsCustomService {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ResultsMapper resultsMapper;


    /**
     * 获取 goods 列表
     * @param request
     * @return
     */
    @SuppressWarnings("checkstyle:NoWhitespaceBefore")
    public MicroServicePage<EsGoodsVO> listEsGoodsNormal(EsGoodsCustomQueryProviderRequest request) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_GOODS_TYPE);

        int pageNum = request.getPageNum();
        builder.withPageable(PageRequest.of(pageNum, request.getPageSize()));
        builder.withQuery(this.packageWhere(request));
        //排序
        if (!CollectionUtils.isEmpty(request.getSortBuilderList())) {
            for (SortCustomBuilder sortBuilder : request.getSortBuilderList()) {
                builder.withSort(new FieldSortBuilder(sortBuilder.getFieldName()).order(sortBuilder.getSortOrder()));
            }
        }
        //脚本排序
        if (request.getScriptSort() != null) {
            Script script = new Script(request.getScriptSort());
            ScriptSortBuilder sortBuilder = SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.DESC);
            builder.withSort(sortBuilder);
        }

        NativeSearchQuery build = builder.build();
        log.info("--->>> EsGoodsCustomService.listEsGoodsNormal DSL: {}", build.getQuery().toString());
        MicroServicePage<EsGoodsVO> query = elasticsearchTemplate.query(build, new ResultsExtractor<MicroServicePage<EsGoodsVO>>() {
            @Override
            public MicroServicePage<EsGoodsVO> extract(SearchResponse searchResponse) {
                MicroServicePage<EsGoodsVO> microServicePage = new MicroServicePage<>();
                //此设计有问题，如果一次查询数据太大，这里返回的数据量可能会撑爆 请求
                List<EsGoodsVO> dataResult = resultsMapper.mapResults(searchResponse, EsGoodsVO.class, null).getContent();

                MicroServicePage<EsGoodsVO> microServiceResult = new MicroServicePage<>();
                microServicePage.setNumber(request.getPageNum());
                microServicePage.setSize(request.getPageSize());
                microServiceResult.setContent(dataResult);
                microServiceResult.setTotal(searchResponse.getHits().getTotalHits());
                return microServiceResult;
            }
        });
        log.info("--->> result: {}", JSON.toJSONString(query));
        return query;
    }


    /**
     * 基础条件
     */
    private BoolQueryBuilder packageBaseWhere(boolean isUseBase, EsGoodsCustomQueryProviderRequest request) {
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = CommonEsSearchCriteriaBuilder.getSpuCommonSearchCriterialBuilder(request);
        if (isUseBase) {
            //只返回有效规格
            boolQueryBuilder.must(termQuery("goodsInfos.delFlag", DeleteFlag.NO.toValue()));
            // 已上架
            boolQueryBuilder.must(termQuery("addedFlag", AddedFlag.YES.toValue()));
            boolQueryBuilder.must(termQuery("goodsInfos.addedFlag", AddedFlag.YES.toValue()));
            // 已经审核
            boolQueryBuilder.must(termQuery("auditStatus", AuditStatus.CHECKED.toValue()));

            //店铺开启状态
            boolQueryBuilder.must(termQuery("storeState", StoreState.OPENING.toValue()));

            //可售状态
            boolQueryBuilder.must(termQuery("vendibilityStatus", 1));
            boolQueryBuilder.must(termQuery("goodsInfos.vendibilityStatus", 1));

        }
        return boolQueryBuilder;
    }

    /**
     * 封装查询商品请求信息
     * @param request
     * @return
     */
    private BoolQueryBuilder packageWhere(EsGoodsCustomQueryProviderRequest request) {

        BoolQueryBuilder boolQueryBuilder = this.packageBaseWhere(true, request);
//
//        if (!StringUtils.isEmpty(request.getNotChannelType())) {
//            boolQueryBuilder.must(termQuery("goodsChannelTypeList", request.getNotChannelType()));
//        }
        if (!CollectionUtils.isEmpty(request.getGoodIdList())) {
            boolQueryBuilder.must(termsQuery("id", request.getGoodIdList()));
        }

        if (!CollectionUtils.isEmpty(request.getUnGoodIdList())) {
            boolQueryBuilder.mustNot(termsQuery("id", request.getUnGoodIdList()));
        }

        /**
         * 知识顾问
         */
        if (request.getCpsSpecial() != null) {
            boolQueryBuilder.must(termQuery("goodsInfos.cpsSpecial", request.getCpsSpecial()));
            boolQueryBuilder.must(termQuery("cpsSpecial", request.getCpsSpecial()));
        }

        /**
         * 评分大于Score
         */
        if (request.getScore() != null) {
            boolQueryBuilder.must(rangeQuery("goodsExtProps.score").gte(request.getScore()));
        }
        /**
         * 价格大于EsSortPrice
         */
        if (request.getEsSortPrice() != null) {
            boolQueryBuilder.must(rangeQuery("sortPrice").gte(request.getEsSortPrice()));
        }
        /**
         * 主播推荐
         */
        if (!StringUtils.isEmpty(request.getAnchorPushs())) {
            boolQueryBuilder.must(termQuery("anchorPushs", request.getAnchorPushs()));
        }
        /**
         * 不展示无库存 库存大于0
         */
        if (request.getHasShowUnStock() != null && !request.getHasShowUnStock()) {
            boolQueryBuilder.must(rangeQuery("stock").gt(0));
            boolQueryBuilder.must(rangeQuery("goodsInfos.stock").gt(0));
        }
        // 大于或等于 上架时间
        if (request.getAfterAddedTime() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("addedTimeNew")
                    .gte(DateUtil.format(request.getAfterAddedTime(), DateUtil.FMT_TIME_4))
                    .lte(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));
        }
        // 大于或等于 搜索条件:创建时间开始
        if (request.getCreateTimeBegin() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").gte(DateUtil.format(request.getCreateTimeBegin(), DateUtil.FMT_TIME_4)));
        }
        // 小于或等于 搜索条件:创建时间截止
        if (request.getCreateTimeEnd() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").lte(DateUtil.format(request.getCreateTimeBegin(), DateUtil.FMT_TIME_4)));
        }

        if (!StringUtils.isEmpty(request.getScriptSpecialOffer())) {
            boolQueryBuilder.filter(QueryBuilders.scriptQuery(new Script("(doc['goodsInfos.marketPrice'].value / doc['goodsExtProps.price'].value) < " + request.getScriptSpecialOffer())));
        }

        // 1 表示图书
        if (request.getBookFlag() != null && request.getBookFlag() == 1) {
            boolQueryBuilder.must(termQuery("goodsCate.bookFlag", request.getBookFlag()));
        }

        //不显示分类
        if (!CollectionUtils.isEmpty(request.getUnClassifyIdList())) {
            boolQueryBuilder.mustNot(QueryBuilders.termsQuery("classify.id", request.getUnClassifyIdList()));
        }

        //这个必须的放到最后，因为是或者操作
        if (!CollectionUtils.isEmpty(request.getClassifyIdList())) {
            BoolQueryBuilder shouldBuilder = new BoolQueryBuilder();
            shouldBuilder.should(QueryBuilders.boolQuery().filter(boolQueryBuilder).filter(QueryBuilders.termsQuery("storeCateIds", request.getClassifyIdList())));
            shouldBuilder.should(QueryBuilders.boolQuery().filter(boolQueryBuilder).filter(QueryBuilders.termsQuery("classify.id", request.getClassifyIdList())));
            return shouldBuilder;
        }

        return boolQueryBuilder;
    }

}
