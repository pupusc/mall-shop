package com.wanmi.sbc.elastic.goods.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
    public MicroServicePage<EsGoodsVO> listEsGoodsNormal(EsGoodsCustomQueryProviderRequest request){

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_GOODS_TYPE);
        int pageNum = request.getPageNum() > 0 ? request.getPageNum() - 1 : request.getPageNum();
        builder.withPageable(PageRequest.of(pageNum, request.getPageSize()));
        builder.withQuery(this.packageWhere(request));
        //排序
        if (!CollectionUtils.isEmpty(request.getSortBuilderList())) {
            for (SortBuilder sortBuilder : request.getSortBuilderList()) {
                builder.withSort(sortBuilder);
            }
        }
        NativeSearchQuery build = builder.build();
        log.info("--->>> EsGoodsCustomService.listEsGoodsNormal DSL: {}", build.getQuery().toString());
        return elasticsearchTemplate.query(build, new ResultsExtractor<MicroServicePage<EsGoodsVO>>() {
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
    }


    /**
     * 基础条件
     */
    private BoolQueryBuilder packageBaseWhere(boolean isUseBase) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (isUseBase) {
            //只返回有效规格
            boolQueryBuilder.must(termQuery("goodsInfos.delFlag", DeleteFlag.YES.toValue()));
            //只是返回有效标签
            boolQueryBuilder.must(termQuery("goodsLabelList.delFlag", DeleteFlag.YES.toValue()));
            // 已上架
            boolQueryBuilder.must(termQuery("addedFlag", AddedFlag.YES.toValue()));
            boolQueryBuilder.must(termQuery("goodsInfos.addedFlag", AddedFlag.YES.toValue()));
            // 已经审核
            boolQueryBuilder.must(termQuery("auditStatus", AuditStatus.CHECKED.toValue()));
            //当前字段没有索引，所以要注释掉
//            boolQueryBuilder.must(termQuery("goodsInfos.auditStatus", AuditStatus.CHECKED.toValue()));

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

        BoolQueryBuilder boolQueryBuilder = this.packageBaseWhere(true);
        if (!CollectionUtils.isEmpty(request.getGoodIdList())) {
//            if (request.getGoodIdList().size() == 1) {
//                boolQueryBuilder.must(termQuery("id", request.getGoodIdList().toArray()[0]));
//            } else {
                boolQueryBuilder.must(termsQuery("id", request.getGoodIdList()));
//            }

        }

        if (!CollectionUtils.isEmpty(request.getUnGoodIdList())) {
//            if (request.getUnGoodIdList().size() == 1) {
//                boolQueryBuilder.mustNot(termQuery("id", request.getUnGoodIdList().toArray()[0]));
//            } else {
                boolQueryBuilder.mustNot(termsQuery("id", request.getUnGoodIdList()));
//            }

        }

        /**
         * 知识顾问
         */
        if (request.getCpsSpecial() != null) {
            boolQueryBuilder.must(termQuery("goodsInfos.cpsSpecial", request.getCpsSpecial()));
            boolQueryBuilder.must(termQuery("cpsSpecial", request.getCpsSpecial()));
        }
        return boolQueryBuilder;
    }

}
