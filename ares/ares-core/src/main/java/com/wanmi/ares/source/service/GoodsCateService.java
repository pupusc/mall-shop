package com.wanmi.ares.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品sku基础信息service
 * Created by sunkun on 2017/9/22.
 */
@Service
public class GoodsCateService {


    @Autowired
    private GoodsInfoService goodsInfoService;


    /**
     * 批量删除
     * @param cateIds
     */
//    public void delete(List<Long> cateIds) throws Exception{
//        if(CollectionUtils.isEmpty(cateIds)){
//            return;
//        }
//
//        basicDataElasticService.doDelete(cateIds.parallelStream().filter(c -> c!=null).map(String::valueOf).collect(Collectors.toList()), GoodsCate.class);
//        //查询全部
//        GoodsInfoQueryRequest goodsInfoQueryRequest = new GoodsInfoQueryRequest();
//        goodsInfoQueryRequest.setGoodsCateIds(cateIds);
//        List<GoodsInfo> goodsInfos = goodsInfoService.queryAll(goodsInfoQueryRequest);
//
//        if(CollectionUtils.isNotEmpty(goodsInfos)) {
//            // 兼容b2b,s2b
//            List<GoodsCate> defaultGoodsCate = getDefaultCate();
//            //默认分类为空，则不执行(s2b无默认分类,则不执行)
//            if(CollectionUtils.isEmpty(defaultGoodsCate)){
//                return;
//            }
//
//            long defCateId = NumberUtils.toLong(defaultGoodsCate.get(0).getId());
//            UpdateRequest updateRequest = new UpdateRequest();
//            updateRequest
//                    .doc(XContentFactory.jsonBuilder()
//                            .startObject()
//                            .field("leafCateId", defCateId)
//                            .field("cateIds", Arrays.asList(defaultGoodsCate.get(0).getParentId(),defCateId))
//                            .endObject());
//            List<UpdateQuery> updateQueries = goodsInfos.stream().map(goodsInfo -> new UpdateQueryBuilder()
//                    .withClass(GoodsInfo.class)
//                    .withId(goodsInfo.getId())
//                    .withUpdateRequest(updateRequest)
//                    .build()).collect(Collectors.toList());
//            elasticsearchTemplate.bulkUpdate(updateQueries);
//        }
//    }
//
//    /**
//     * 获取默认分类
//     * @return
//     */
//    private List<GoodsCate> getDefaultCate(){
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX_BASIC);
//        builder.withTypes(EsConstants.ES_TYPE_SKU_CATE);
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        bq.must(termQuery("default", true));
//        bq.must(termQuery("delFlag", false));
//        builder.withQuery(bq);
//        return elasticsearchTemplate.queryForList(builder.build(), GoodsCate.class);
//    }
//
//    /**
//     * 分页查询
//     * @return 所有sku
//     */
//    public Page<GoodsCate> queryPage(GoodsCateQueryRequest request){
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX_BASIC);
//        builder.withTypes(EsConstants.ES_TYPE_SKU_CATE);
//        builder.withPageable(request.getPageable());
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        //批量编号
//        if (CollectionUtils.isNotEmpty(request.getIds())){
//            bq.must(termsQuery("id", request.getIds()));
//        }
//        //批量编号
//        if (CollectionUtils.isNotEmpty(request.getParentIds())){
//            bq.must(termsQuery("parentId", request.getParentIds()));
//        }
//        if (request.getDelFlag() != null){
//            bq.must(termQuery("delFlag", request.getDelFlag().booleanValue()));
//        }
//        builder.withQuery(bq);
//        return elasticsearchTemplate.queryForPage(builder.build(), GoodsCate.class);
//    }
//
//    /**
//     * 查询返回list
//     * @return 所有sku
//     */
//    public List<GoodsCate> query(GoodsCateQueryRequest request){
//        return this.queryPage(request).getContent();
//    }
//
//    /**
//     * 查询所有
//     * @return 所有sku
//     */
//    public List<GoodsCate> queryAll(GoodsCateQueryRequest request){
//        request.setPageSize(10000L);
//        List<GoodsCate> infos = new ArrayList<>();
//        Page<GoodsCate> infoPage = this.queryPage(request);
//        infos.addAll(infoPage.getContent());
//        if(infoPage.getTotalPages() > 1){
//            for(long page = 1; page < infoPage.getTotalPages();page++){
//                request.setPageNum(page);
//                infos.addAll(this.query(request));
//            }
//        }
//        return infos;
//    }

}
