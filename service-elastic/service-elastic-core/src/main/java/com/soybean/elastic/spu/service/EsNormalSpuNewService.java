package com.soybean.elastic.spu.service;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.req.EsSortSpuNewQueryProviderReq;
import com.soybean.elastic.api.req.EsSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.constant.ConstantMultiMatchField;
import com.soybean.elastic.spu.model.EsSpuNew;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * Description: 内部搜索商品请使用此对象信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/4 11:16 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class EsNormalSpuNewService extends AbstractEsSpuNewService{

    /**
     * 拼装请求信息
     * @param req
     * @return
     */
    private BoolQueryBuilder packageQueryCondition(EsSpuNewQueryProviderReq req) {
        BoolQueryBuilder boolQueryBuilder = super.packageEsSpuNewReq(req);
        //根据spuId查询
        if (CollectionUtils.isNotEmpty(req.getSpuIds())) {
            if (req.getSpuIds().size() > 1) {
                boolQueryBuilder.must(termsQuery(ConstantMultiMatchField.FIELD_SPU_SPUId, req.getSpuIds()));
            } else {
                boolQueryBuilder.must(termQuery(ConstantMultiMatchField.FIELD_SPU_SPUId, req.getSpuIds().get(0)));
            }
        }
        //必须的unSpuId
        if (CollectionUtils.isNotEmpty(req.getUnSpuIds())) {
            if (req.getUnSpuIds().size() > 1) {
                boolQueryBuilder.mustNot(termsQuery(ConstantMultiMatchField.FIELD_SPU_SPUId, req.getUnSpuIds()));
            } else {
                boolQueryBuilder.mustNot(termQuery(ConstantMultiMatchField.FIELD_SPU_SPUId, req.getUnSpuIds().get(0)));
            }
        }
        return boolQueryBuilder;
    }

    /**
     * 设置排序
     */
    private List<FieldSortBuilder> packageSort(EsSortSpuNewQueryProviderReq sortQueryProviderReq){
        return new ArrayList<>();
    }


    /**
     * 普通搜索
     * @param req
     * @return
     */
    public CommonPageResp<List<EsSpuNewResp>> listNormalEsSpuNew(EsSpuNewQueryProviderReq req) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_SPU_NEW);

        //分页 从0开始
        req.setPageNum(Math.max((req.getPageNum() - 1), 0));
        builder.withPageable(PageRequest.of(req.getPageNum(), req.getPageSize()));


        //查询 条件
        builder.withQuery(this.packageQueryCondition(req));
        //排序
        for (FieldSortBuilder fieldSortBuilder : this.packageSort(req)) {
            builder.withSort(fieldSortBuilder);
        }



        NativeSearchQuery build = builder.build();
        log.info("--->>> EsSpuNewNormalService.listEsSpuNew DSL: {}", build.getQuery().toString());
        AggregatedPage<EsSpuNew> resultQueryPage = elasticsearchTemplate.queryForPage(build, EsSpuNew.class);
        return new CommonPageResp<>(resultQueryPage.getTotalElements(), this.packageEsSpuNewResp(resultQueryPage.getContent()));
    }
}
