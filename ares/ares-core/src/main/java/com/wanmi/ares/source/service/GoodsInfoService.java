package com.wanmi.ares.source.service;

import com.wanmi.ares.report.goods.dao.GoodsInfoSpecDetailRelMapper;
import com.wanmi.ares.report.goods.dao.SkuMapper;
import com.wanmi.ares.request.GoodsInfoQueryRequest;
import com.wanmi.ares.source.model.root.GoodsInfo;
import com.wanmi.ares.source.model.root.GoodsInfoSpecDetailRel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品sku基础信息service
 * Created by sunkun on 2017/9/22.
 */
@Slf4j
@Service
public class GoodsInfoService {

//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    @Autowired
//    private BasicDataElasticService basicDataElasticService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private GoodsInfoSpecDetailRelMapper goodsInfoSpecDetailRelMapper;


//    public void delete(List<String> skuIds) throws Exception{
//        if(CollectionUtils.isNotEmpty(skuIds)){
//            basicDataElasticService.doDelete(skuIds, GoodsInfo.class);
//        }
//    }
//
//    public void deleteBySpu(List<String> spuIds) throws Exception{
//        GoodsInfoQueryRequest request = new GoodsInfoQueryRequest();
//        request.setGoodsIds(spuIds);
//        List<GoodsInfo> skus = this.queryAll(request);
//        if(CollectionUtils.isNotEmpty(skus)){
//            basicDataElasticService.doDelete(skus.stream().map(GoodsInfo::getId).collect(Collectors.toList()), GoodsInfo.class);
//        }
//    }
//
//    /**
//     * 批量上下架商品
//     * @param requests
//     */
//    public void added(List<GoodsInfoRequest> requests) throws Exception {
//        if (CollectionUtils.isNotEmpty(requests)) {
//            List<UpdateQuery> updateQueries = requests.stream().map(request -> {
//                UpdateRequest updateRequest = new UpdateRequest();
//                try {
//                    updateRequest
//                            .doc(XContentFactory.jsonBuilder()
//                                    .startObject()
//                                    .field("addedTime", request.getAddedTime())
//                                    .field("addedFlag", request.isAddedFlag())
//                                    .field("saleDate", request.getSaleDate())
//                                    .endObject());
//                    return new UpdateQueryBuilder()
//                            .withClass(GoodsInfo.class)
//                            .withId(request.getId())
//                            .withUpdateRequest(updateRequest)
//                            .build();
//                } catch (IOException e) {
//                    log.error("Activemq sku execute method [sku.added] error, param={}", request, e);
//                }
//                return null;
//            }).filter(updateQuery -> updateQuery != null).collect(Collectors.toList());
//
//            if(CollectionUtils.isNotEmpty(updateQueries)) {
//                elasticsearchTemplate.bulkUpdate(updateQueries);
//            }
//        }
//    }

    /**
     * 查询所有
     * @return 所有sku
     */
//    public List<GoodsInfo> queryAll(GoodsInfoQueryRequest request){
//        request.setPageSize(10000L);
//        List<GoodsInfo> infos = new ArrayList<>();
//        Page<GoodsInfo> infoPage = this.query(request);
//        infos.addAll(infoPage.getContent());
//        if(infoPage.getTotalPages() > 1){
//            for(long page = 1; page < infoPage.getTotalPages();page++){
//                request.setPageNum(page);
//                infos.addAll(this.query(request).getContent());
//            }
//        }
//        return infos;
//    }

    /**
     * 分页查询
     * @return 所有sku
     */
//    public Page<GoodsInfo> query(GoodsInfoQueryRequest request){
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX_BASIC);
//        builder.withTypes(EsConstants.ES_TYPE_SKU);
//        builder.withPageable(request.getPageable());
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        //批量编号
//        if (CollectionUtils.isNotEmpty(request.getGoodsInfoIds())){
//            bq.must(termsQuery("id", request.getGoodsInfoIds()));
//        }
//
//        if (CollectionUtils.isNotEmpty(request.getGoodsIds())){
//            bq.must(termsQuery("goodsId", request.getGoodsIds()));
//        }
//
//        //品牌精确查询
//        if (request.getBrandId() != null){
//            bq.must(termQuery("brandId", request.getBrandId()));
//        }
//
//        //批量编号
//        if (CollectionUtils.isNotEmpty(request.getGoodsCateIds())){
//            bq.must(termsQuery("cateIds", request.getGoodsCateIds()));
//        }
//
//        //SKU编码或名称模糊查询
//        if (StringUtils.isNotBlank(request.getKeyWord())) {
//            BoolQueryBuilder bq1 = QueryBuilders.boolQuery();
//            bq1.should(matchPhrasePrefixQuery("goodsInfoNo", request.getKeyWord().trim()));
//            bq1.should(matchPhrasePrefixQuery("goodsInfoName", request.getKeyWord().trim()));
//            bq.must(bq1);
//        }
//        builder.withQuery(bq);
//        return elasticsearchTemplate.queryForPage(builder.build(), GoodsInfo.class);
//    }

    public List<GoodsInfo> queryGoodsInfo(GoodsInfoQueryRequest request){
        return this.skuMapper.queryGoodsInfo(request);
    }

    public List<GoodsInfo> queryGoodsInfoDetail(GoodsInfoQueryRequest request){
        List<GoodsInfo> list = queryGoodsInfo(request);
        if(list != null && !list.isEmpty()){
            List<String> ids = list.stream().map(GoodsInfo::getId).collect(Collectors.toList());
            List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRels = this.goodsInfoSpecDetailRelMapper.queryDetailNameGroupByGoodsId(ids,0);
            Map<String,String> detailNameMap = new HashMap<>();
            if(goodsInfoSpecDetailRels!=null && !goodsInfoSpecDetailRels.isEmpty()){
                detailNameMap = goodsInfoSpecDetailRels.stream().collect(Collectors.toMap(GoodsInfoSpecDetailRel::getGoodsInfoId,GoodsInfoSpecDetailRel::getDetailName));
                for(GoodsInfo goodsInfo : list){
                    goodsInfo.setDetailName(detailNameMap.get(goodsInfo.getId()));
                }
            }
        }
        return list;
    }

    public List<GoodsInfo> queryGoodsInfoDetail(List<GoodsInfo> list){
        List<String> ids = list.stream().map(GoodsInfo::getId).collect(Collectors.toList());
        List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRels = this.goodsInfoSpecDetailRelMapper.queryDetailNameGroupByGoodsId(ids,0);
        Map<String,String> detailNameMap = new HashMap<>();
        if(goodsInfoSpecDetailRels!=null && !goodsInfoSpecDetailRels.isEmpty()){
            detailNameMap = goodsInfoSpecDetailRels.stream().collect(Collectors.toMap(GoodsInfoSpecDetailRel::getGoodsInfoId,GoodsInfoSpecDetailRel::getDetailName));
            for(GoodsInfo goodsInfo : list){
                goodsInfo.setDetailName(detailNameMap.get(goodsInfo.getId()));
            }
        }
        return list;
    }

}
