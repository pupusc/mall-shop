package com.wanmi.sbc.elastic.sku.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsSearchResponse;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.spu.mapper.EsSpuMapper;
import com.wanmi.sbc.elastic.storeInformation.model.root.StoreInformation;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateChildCateIdsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ES商品信息数据源操作
 * Created by daiyitian on 2017/4/21.
 */
@Slf4j
@Service
public class EsSkuService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private ResultsMapper resultsMapper;

    @Autowired
    private EsSpuMapper esSpuMapper;

    /**
     * 修改更新商家名称信息
     *
     * @param storeList 店铺名称
     * @return
     */
    public Long updateCompanyName(List<StoreInformation> storeList) {
        final String fmt = "ctx._source.goodsInfo.storeName='%s'";
        Long resCount = 0L;
        if (storeList != null) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client)
                    .source(EsConstants.DOC_GOODS_INFO_TYPE, EsConstants.DOC_GOODS_INFO_TYPE);
            for (StoreInformation s : storeList) {
                String script = String.format(fmt, s.getStoreName());
                updateByQuery.filter(QueryBuilders.termQuery("goodsInfo.storeId", s.getStoreId()));
                updateByQuery.script(new Script(script));
                resCount += updateByQuery.execute().actionGet().getUpdated();
            }
        }
        return resCount;
    }


    /**
     * 最基础的分页查询
     * @param request
     * @return
     */
    private MicroServicePage<EsGoodsInfoVO> basePage(EsSkuPageRequest request) {
        EsSearchResponse esResponse = elasticsearchTemplate.query(EsSkuSearchCriteriaBuilder.getSearchCriteria(request),
                searchResponse -> EsSearchResponse.build(searchResponse, resultsMapper));
        return new MicroServicePage<>(esResponse.getData() == null ? Collections.emptyList() : esResponse.getData(), PageRequest.of(request.getPageNum(),
                request.getPageSize()), esResponse.getTotal());
    }
}
