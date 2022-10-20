package com.soybean.elastic.spu.service;

import com.soybean.common.req.CommonPageQueryReq;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.resp.EsSpuNewAggResp;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubAnchorRecomNew;
import com.soybean.elastic.spu.model.sub.SubBookLabelNew;
import com.wanmi.sbc.elastic.api.common.CommonEsSearchCriteriaBuilder;
import com.wanmi.sbc.setting.api.provider.weight.SearchWeightProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/4 11:10 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class AbstractEsSpuNewService {

    @Autowired
    protected SearchWeightProvider searchWeightProvider;

    @Autowired
    protected ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 二次封装请求的正常的对象信息
     * @param req
     * @return
     */
    protected BoolQueryBuilder packageEsSpuNewReq(CommonPageQueryReq req) {
        BoolQueryBuilder boolQb = CommonEsSearchCriteriaBuilder.getSpuNewCommonBuilder(req);

        boolQb.must(termQuery("delFlag", req.getDelFlag()));
        boolQb.must(termQuery("auditStatus", 1));
        boolQb.must(termQuery("addedFlag", 1));
        return boolQb;
    }

    /**
     * 聚合 列表信息
     * @param resultQueryPage
     * @return
     */
    protected EsSpuNewAggResp<List<EsSpuNewResp>> packageEsSpuNewAggResp(AggregatedPage<EsSpuNew> resultQueryPage) {
        List<String> resultLabels = new ArrayList<>();
        Nested labels = resultQueryPage.getAggregations().get("labels");
        Terms labelNames = labels.getAggregations().get("labelName");
        for (Terms.Bucket bucket : labelNames.getBuckets()) {
            resultLabels.add(bucket.getKeyAsString());
        }

        EsSpuNewAggResp<List<EsSpuNewResp>> esSpuNewAggResp = new EsSpuNewAggResp<>();
        esSpuNewAggResp.setLabels(resultLabels);
        esSpuNewAggResp.setResult(new CommonPageResp<>(resultQueryPage.getTotalElements(), this.packageEsSpuNewResp(resultQueryPage.getContent())));
        return esSpuNewAggResp;
    }

    /**
     * 打包商品
     * @param esSpuNewList
     * @return
     */
    protected List<EsSpuNewResp> packageEsSpuNewResp(List<EsSpuNew> esSpuNewList) {
        List<EsSpuNewResp> result = new ArrayList<>();
        for (EsSpuNew esSpuNew : esSpuNewList) {
            EsSpuNewResp esSpuResp = new EsSpuNewResp();
            BeanUtils.copyProperties(esSpuNew, esSpuResp);
//            esSpuResp.setSpuId(esSpuNew.getSpuId());
//            esSpuResp.setSpuName(esSpuNew.getSpuName());
//            esSpuResp.setSpuSubName(esSpuNew.getSpuSubName());
//            esSpuResp.setSpuCategory(esSpuNew.getSpuCategory());
//            esSpuResp.setSalesPrice(esSpuNew.getSalesPrice());
//            esSpuResp.setPic(esSpuNew.getPic());
//            esSpuResp.setUnBackgroundPic(esSpuNew.getUnBackgroundPic());

            List<EsSpuNewResp.SubAnchorRecom> subAnchorRecoms = new ArrayList<>();
            if (!CollectionUtils.isEmpty(esSpuNew.getAnchorRecoms())) {
                for (SubAnchorRecomNew anchorRecom : esSpuNew.getAnchorRecoms()) {
                    EsSpuNewResp.SubAnchorRecom subAnchorRecom = new EsSpuNewResp.SubAnchorRecom();
                    subAnchorRecom.setRecomId(anchorRecom.getRecomId());
                    subAnchorRecom.setRecomName(anchorRecom.getRecomName());
                    subAnchorRecoms.add(subAnchorRecom);
                }
            }
            esSpuResp.setAnchorRecoms(subAnchorRecoms);

            if (esSpuNew.getClassify() != null) {
                EsSpuNewResp.SubClassify subClassify = new EsSpuNewResp.SubClassify();
                subClassify.setFClassifyId(esSpuNew.getClassify().getFclassifyId());
                subClassify.setFClassifyName(esSpuNew.getClassify().getFclassifyName());
                subClassify.setClassifyId(esSpuNew.getClassify().getClassifyId());
                subClassify.setClassifyName(esSpuNew.getClassify().getClassifyName());
                esSpuResp.setClassify(subClassify);
            }

            if (esSpuNew.getBook() != null) {
                EsSpuNewResp.Book book = new EsSpuNewResp.Book();
                book.setIsbn(esSpuNew.getBook().getIsbn());
                book.setAuthorNames(esSpuNew.getBook().getAuthorNames());
                book.setScore(esSpuNew.getBook().getScore());
                book.setPublisher(esSpuNew.getBook().getPublisher());
                book.setFixPrice(esSpuNew.getBook().getFixPrice());

                if (!CollectionUtils.isEmpty(esSpuNew.getBook().getTags())) {
                    List<EsSpuNewResp.Book.SubBookLabel> subBookLabels = new ArrayList<>();
                    for (SubBookLabelNew tag : esSpuNew.getBook().getTags()) {
                        EsSpuNewResp.Book.SubBookLabel subBookLabel = new EsSpuNewResp.Book.SubBookLabel();
                        subBookLabel.setStagId(tag.getStagId());
                        subBookLabel.setStagName(tag.getStagName());
                        subBookLabel.setTagId(tag.getTagId());
                        subBookLabel.setTagName(tag.getTagName());
                        subBookLabels.add(subBookLabel);
                    }
                    book.setTags(subBookLabels);
                }
                esSpuResp.setBook(book);
            }
            result.add(esSpuResp);
        }
        return result;
    }
}
