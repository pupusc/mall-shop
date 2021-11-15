package com.wanmi.sbc.elastic.goods.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.goods.*;
import com.wanmi.sbc.elastic.api.response.goods.*;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsLabelNestVO;
import com.wanmi.sbc.elastic.goods.model.root.EsGoods;
import com.wanmi.sbc.elastic.goods.model.root.EsGoodsInfo;
import com.wanmi.sbc.elastic.goods.model.root.GoodsLevelPriceNest;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsSpecQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.classify.ClassifyProviderRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByConditionRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsPropDetailRelByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceListBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsSpecListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByStoreCateIdAndIsHaveSelfRequest;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateListByConditionResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifySimpleProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsByConditionResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListByStoreCateIdAndIsHaveSelfResponse;
import com.wanmi.sbc.goods.bean.dto.BatchEnterPrisePriceDTO;
import com.wanmi.sbc.goods.bean.dto.DistributionGoodsInfoModifyDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * ES商品信息数据源操作
 * Created by daiyitian on 2017/4/21.
 */
@Slf4j
@Service
public class EsGoodsInfoElasticService {

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsIntervalPriceQueryProvider goodsIntervalPriceQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private GoodsSpecQueryProvider goodsSpecQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private ResultsMapper resultsMapper;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private EsGoodsElasticService esGoodsElasticService;

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private EsGoodsLabelService esGoodsLabelService;

    @Autowired
    private GoodsLevelPriceQueryProvider goodsLevelPriceQueryProvider;

    /**
     * ES价格排序脚本
     */
    private final String PRICE_ORDER_SCRIPT =
            "def priceType=doc['goodsInfo.priceType'].value;" +
                    "def levelDiscountFlag=doc['goodsInfo.levelDiscountFlag'].value;" +
                    "def marketPrice=doc['goodsInfo.marketPrice'].value;" +
                    "if(priceType&&priceType==1){" +
                    "if(levelDiscountFlag && levelDiscountFlag == 1 && levelDiscount){" +
                    "return doc['goodsInfo.intervalMinPrice'].value * levelDiscount;" +
                    "};" +
                    "return doc['goodsInfo.intervalMinPrice'].value;" +
                    "};" +
                    "def customerPrices=_source.customerPrices;" +
                    "if(customerPrices && customerPrices.size()>0){" +
                    "for (cp in customerPrices){" +
                    "if(cp.customerId==customerId){return cp.price;};" +
                    "};" +
                    "};" +
                    "def goodsLevelPrices=_source.goodsLevelPrices;" +
                    "if(goodsLevelPrices && goodsLevelPrices.size()>0){" +
                    "for (lp in goodsLevelPrices){" +
                    "if(lp.levelId==levelId){return lp.price;};" +
                    "};" +
                    "};" +
                    "if(levelDiscount && levelDiscount > 0){" +
                    "return marketPrice * levelDiscount;" +
                    "};" +
                    "return marketPrice";

    /**
     * 分页查询ES商品(实现WEB的商品列表)
     *
     * @param queryRequest
     * @return
     */
    public EsGoodsInfoResponse page(EsGoodsInfoQueryRequest queryRequest) {
        EsGoodsInfoResponse goodsInfoResponse = EsGoodsInfoResponse.builder().build();
        //仅显示商家
        //queryRequest.setGoodsSource(GoodsSource.SELLER);
        EsSearchResponse response = getEsBaseInfoByParams(queryRequest);
        if (response.getData().size() < 1) {
            goodsInfoResponse.setEsGoodsInfoPage(new MicroServicePage<>(response.getData(),
                    PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), response.getTotal()));
            return goodsInfoResponse;
        }

        // 过滤不展示的商品标签
        response.getData().forEach(goods -> {
            List<GoodsLabelNestVO> labels = goods.getGoodsLabelList();
            if (CollectionUtils.isNotEmpty(labels)) {
                labels = labels.stream()
                        .filter(label -> Boolean.TRUE.equals(label.getLabelVisible()) && Objects.equals(label.getDelFlag(), DeleteFlag.NO))
                        .sorted(Comparator.comparing(GoodsLabelNestVO::getLabelSort).thenComparing(GoodsLabelNestVO::getGoodsLabelId).reversed())
                        .collect(Collectors.toList());
                goods.setGoodsLabelList(labels);
            }
        });

        List<String> skuIds =
                response.getData().stream().map(EsGoodsInfoVO::getGoodsInfo).map(GoodsInfoNestVO::getGoodsInfoId).distinct().collect(Collectors.toList());

        Map<String, GoodsInfoVO> goodsInfoMap = goodsInfoQueryProvider.listByIds(
                GoodsInfoListByIdsRequest.builder().goodsInfoIds(skuIds).build()
        ).getContext().getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g));


        List<EsGoodsInfoVO> esGoodsInfoList = new LinkedList<>();
        List<GoodsVO> goodses = new ArrayList<>();
        Set<String> existsGoods = new HashSet<>();
        for (EsGoodsInfoVO esGoodsInfo : response.getData()) {
            GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();
            //排除分销商品，错误数据
            if (Objects.nonNull(goodsInfo.getDistributionGoodsAudit()) &&
                    goodsInfo.getDistributionGoodsAudit() == DistributionGoodsAudit.CHECKED &&
                    Objects.isNull(goodsInfo.getDistributionCommission())) {
                goodsInfo.setDelFlag(DeleteFlag.YES);
                goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
                continue;
            }
            goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO :
                    goodsInfo.getMarketPrice());

            goodsInfo.setStock(goodsInfoMap.getOrDefault(goodsInfo.getGoodsInfoId(), new GoodsInfoVO()).getStock());

            if (Objects.isNull(goodsInfo.getStock()) || goodsInfo.getStock() < 1) {
                goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
            }
            esGoodsInfoList.add(esGoodsInfo);

            if (!existsGoods.contains(esGoodsInfo.getGoodsId())) {
                GoodsVO goodsVO = new GoodsVO();
                goodsVO.setGoodsId(esGoodsInfo.getGoodsId());
                goodsVO.setLinePrice(esGoodsInfo.getLinePrice());
                goodsVO.setGoodsUnit(esGoodsInfo.getGoodsUnit());
                goodses.add(goodsVO);
                existsGoods.add(esGoodsInfo.getGoodsId());
            }
        }
        response.setData(esGoodsInfoList);

        //提取聚合数据
        EsGoodsBaseResponse baseResponse = extractBrands(response);
        goodsInfoResponse.setBrands(baseResponse.getBrands());

        if (queryRequest.isCateAggFlag()) {
            //提取聚合数据
            baseResponse = extractGoodsCate(response);
            goodsInfoResponse.setCateList(baseResponse.getCateList());
        }

        //提取规格与规格值聚合数据
        baseResponse = extractGoodsSpecsAndSpecDetails(response);

        //提取商品标签数据
        goodsInfoResponse.setGoodsLabelVOList(esGoodsLabelService.extractGoodsLabel(response));

        goodsInfoResponse.setGoodsSpecs(baseResponse.getGoodsSpecs());
        goodsInfoResponse.setGoodsSpecDetails(baseResponse.getGoodsSpecDetails());
        goodsInfoResponse.setEsGoodsInfoPage(new MicroServicePage<>(response.getData(),
                PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), response.getTotal()));

        return goodsInfoResponse;
    }

    /**
     * 我的店铺（店铺精选页）
     *
     * @param queryRequest
     * @return
     */
    public EsGoodsInfoResponse distributorGoodsListByCustomerId(EsGoodsInfoQueryRequest queryRequest) {
        EsGoodsInfoResponse goodsInfoResponse = EsGoodsInfoResponse.builder().build();

        //聚合品牌
        queryRequest.putAgg(AggregationBuilders.terms("brand_group").field("goodsBrand.brandId"));

        EsSearchResponse response = elasticsearchTemplate.query(queryRequest.getSearchCriteria(),
                searchResponse -> EsSearchResponse.build(searchResponse, resultsMapper));

        if (response.getData().size() < 1) {
            goodsInfoResponse.setEsGoodsInfoPage(new MicroServicePage<>(response.getData(),
                    PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), response.getTotal()));
            return goodsInfoResponse;
        }

        // 过滤不展示的商品标签
        response.getData().forEach(goods -> {
            List<GoodsLabelNestVO> labels = goods.getGoodsLabelList();
            if (CollectionUtils.isNotEmpty(labels)) {
                labels = labels.stream()
                        .filter(label -> Boolean.TRUE.equals(label.getLabelVisible()) && Objects.equals(label.getDelFlag(), DeleteFlag.NO))
                        .sorted(Comparator.comparing(GoodsLabelNestVO::getLabelSort).thenComparing(GoodsLabelNestVO::getGoodsLabelId).reversed())
                        .collect(Collectors.toList());
                goods.setGoodsLabelList(labels);
            }
        });

        List<String> goodsInfoIds = queryRequest.getGoodsInfoIds();

        List<String> goodsIds =
                response.getData().stream().map(EsGoodsInfoVO::getGoodsInfo).map(GoodsInfoNestVO::getGoodsId).distinct().collect(Collectors.toList());
        List<String> skuIds =
                response.getData().stream().map(EsGoodsInfoVO::getGoodsInfo).map(GoodsInfoNestVO::getGoodsInfoId).distinct().collect(Collectors.toList());
        //批量查询SPU数据
        GoodsByConditionRequest goodsQueryRequest = new GoodsByConditionRequest();
        goodsQueryRequest.setGoodsIds(goodsIds);
        List<GoodsVO> goodses = goodsQueryProvider.listByCondition(goodsQueryRequest).getContext().getGoodsVOList();

        Map<String, GoodsInfoVO> goodsInfoMap = goodsInfoQueryProvider.listByIds(
                GoodsInfoListByIdsRequest.builder().goodsInfoIds(skuIds).build()
        ).getContext().getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g));

        //批量查询规格值表
        Map<String, List<GoodsInfoSpecDetailRelVO>> detailRels =
                goodsInfoSpecDetailRelQueryProvider.listBySkuIds(new GoodsInfoSpecDetailRelBySkuIdsRequest(skuIds))
                        .getContext().getGoodsInfoSpecDetailRelVOList().stream()
                        .collect(Collectors.groupingBy(GoodsInfoSpecDetailRelVO::getGoodsInfoId));
        List<EsGoodsInfoVO> data = response.getData();
        List<EsGoodsInfoVO> resultList = new LinkedList<>();
        for (String goodsInfoId : goodsInfoIds) {
            for (EsGoodsInfoVO esGoodsInfo : data) {
                GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();
                if (Objects.isNull(goodsInfo) || Objects.nonNull(goodsInfo) && !goodsInfoId.equals(goodsInfo.getGoodsInfoId())) {
                    continue;
                }
                goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO :
                        goodsInfo.getMarketPrice());
                Optional<GoodsVO> goodsOptional =
                        goodses.stream().filter(goods -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).findFirst();
                if (goodsOptional.isPresent()) {
                    GoodsVO goods = goodsOptional.get();
                    //为空，则以商品主图
                    if (StringUtils.isEmpty(goodsInfo.getGoodsInfoImg())) {
                        goodsInfo.setGoodsInfoImg(goods.getGoodsImg());
                    }
                    //填充规格值
                    if (Constants.yes.equals(goods.getMoreSpecFlag()) && MapUtils.isNotEmpty(detailRels) && detailRels.containsKey(goodsInfo.getGoodsInfoId())) {
                        goodsInfo.setSpecText(detailRels.get(goodsInfo.getGoodsInfoId()).stream().map(GoodsInfoSpecDetailRelVO::getDetailName).collect(Collectors.joining(" ")));
                    }
                    goodsInfo.setStock(goodsInfoMap.getOrDefault(goodsInfo.getGoodsInfoId(), new GoodsInfoVO()).getStock());

                    if (Objects.isNull(goodsInfo.getStock()) || goodsInfo.getStock() < 1) {
                        goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
                    }
                } else {//不存在，则做为删除标记
                    goodsInfo.setDelFlag(DeleteFlag.YES);
                    goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
                }
                resultList.add(esGoodsInfo);
            }
        }

        response.setData(resultList);

        //提取聚合数据
        EsGoodsBaseResponse baseResponse = extractBrands(response);
        goodsInfoResponse.setBrands(baseResponse.getBrands());

        goodsInfoResponse.setEsGoodsInfoPage(new MicroServicePage<>(response.getData(),
                PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), response.getTotal()));
        goodsInfoResponse.setGoodsList(goodses);
        return goodsInfoResponse;
    }

    /**
     * 分销员-我的店铺-选品功能
     *
     * @param queryRequest
     * @return
     */
    public EsGoodsInfoResponse distributorGoodsList(EsGoodsInfoQueryRequest queryRequest, List<String> goodsIdList) {
        EsGoodsInfoResponse goodsInfoResponse = EsGoodsInfoResponse.builder().build();
        EsSearchResponse response = getEsBaseInfoByParams(queryRequest);

        if (response.getData().size() < 1) {
            goodsInfoResponse.setEsGoodsInfoPage(new MicroServicePage<>(response.getData(),
                    PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), response.getTotal()));
            return goodsInfoResponse;
        }

        // 过滤不展示的商品标签
        response.getData().forEach(goods -> {
            List<GoodsLabelNestVO> labels = goods.getGoodsLabelList();
            if (CollectionUtils.isNotEmpty(labels)) {
                goods.setGoodsLabelList(labels.stream().filter(label -> Boolean.TRUE.equals(label.getLabelVisible())
                        && Objects.equals(label.getDelFlag(), DeleteFlag.NO))
                        .sorted(Comparator.comparing(GoodsLabelNestVO::getLabelSort).thenComparing(GoodsLabelNestVO::getGoodsLabelId).reversed())
                        .collect(Collectors.toList()));
            }
        });

        List<String> goodsIds =
                response.getData().stream().map(EsGoodsInfoVO::getGoodsInfo).map(GoodsInfoNestVO::getGoodsId).distinct().collect(Collectors.toList());
        List<String> skuIds =
                response.getData().stream().map(EsGoodsInfoVO::getGoodsInfo).map(GoodsInfoNestVO::getGoodsInfoId).distinct().collect(Collectors.toList());
        //批量查询SPU数据
        GoodsByConditionRequest goodsQueryRequest = new GoodsByConditionRequest();
        goodsQueryRequest.setGoodsIds(goodsIds);
        List<GoodsVO> goodses = goodsQueryProvider.listByCondition(goodsQueryRequest).getContext().getGoodsVOList();


        Map<String, GoodsInfoVO> goodsInfoMap = goodsInfoQueryProvider.listByIds(
                GoodsInfoListByIdsRequest.builder().goodsInfoIds(skuIds).build()
        ).getContext().getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g));


        //批量查询规格值表
        Map<String, List<GoodsInfoSpecDetailRelVO>> detailRels =
                goodsInfoSpecDetailRelQueryProvider.listBySkuIds(new GoodsInfoSpecDetailRelBySkuIdsRequest(skuIds))
                        .getContext().getGoodsInfoSpecDetailRelVOList().stream()
                        .collect(Collectors.groupingBy(GoodsInfoSpecDetailRelVO::getGoodsInfoId));
        List<EsGoodsInfoVO> esGoodsInfoList = new LinkedList<>();
        Boolean hideSelectedDistributionGoods = queryRequest.isHideSelectedDistributionGoods();
        for (EsGoodsInfoVO esGoodsInfo : response.getData()) {
            GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();
            Boolean anyMatch = goodsIdList.stream().anyMatch(id -> id.equals(goodsInfo.getGoodsInfoId()));

            goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO :
                    goodsInfo.getMarketPrice());
            Optional<GoodsVO> goodsOptional =
                    goodses.stream().filter(goods -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).findFirst();
            if (goodsOptional.isPresent()) {
                GoodsVO goods = goodsOptional.get();
                goodsInfo.setPriceType(goods.getPriceType());
                //为空，则以商品主图
                if (StringUtils.isEmpty(goodsInfo.getGoodsInfoImg())) {
                    goodsInfo.setGoodsInfoImg(goods.getGoodsImg());
                }
                if (anyMatch) {
                    goodsInfo.setJoinDistributior(1);
                } else {
                    goodsInfo.setJoinDistributior(0);
                }
                //填充规格值
                if (Constants.yes.equals(goods.getMoreSpecFlag()) && MapUtils.isNotEmpty(detailRels) && detailRels.containsKey(goodsInfo.getGoodsInfoId())) {
                    goodsInfo.setSpecText(detailRels.get(goodsInfo.getGoodsInfoId()).stream().map(GoodsInfoSpecDetailRelVO::getDetailName).collect(Collectors.joining(" ")));
                }
                goodsInfo.setStock(goodsInfoMap.getOrDefault(goodsInfo.getGoodsInfoId(), new GoodsInfoVO()).getStock());

                if (Objects.isNull(goodsInfo.getStock()) || goodsInfo.getStock() < 1) {
                    goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
                }
            } else {//不存在，则做为删除标记
                goodsInfo.setDelFlag(DeleteFlag.YES);
                goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
            }
            esGoodsInfoList.add(esGoodsInfo);
        }
        response.setData(esGoodsInfoList);
        //提取聚合数据
        EsGoodsBaseResponse baseResponse = extractBrands(response);
        goodsInfoResponse.setBrands(KsBeanUtil.convert(baseResponse.getBrands(), GoodsListBrandVO.class));
        if (queryRequest.isCateAggFlag()) {
            //提取聚合数据
            baseResponse = extractGoodsCate(response);
            goodsInfoResponse.setCateList(baseResponse.getCateList());
        }

        //提取规格与规格值聚合数据
        baseResponse = extractGoodsSpecsAndSpecDetails(response);
        goodsInfoResponse.setGoodsSpecs(baseResponse.getGoodsSpecs());
        goodsInfoResponse.setGoodsSpecDetails(baseResponse.getGoodsSpecDetails());

        goodsInfoResponse.setEsGoodsInfoPage(new MicroServicePage<>(response.getData(),
                PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), response.getTotal()));

        return goodsInfoResponse;
    }

    /**
     * 根据不同条件查询ES商品信息
     *
     * @param queryRequest
     * @return
     */
    public EsSearchResponse getEsBaseInfoByParams(EsGoodsInfoQueryRequest queryRequest) {
        if (StringUtils.isNotBlank(queryRequest.getKeywords())) {
            queryRequest.setKeywords(this.analyze(queryRequest.getKeywords()));
        }
        if (queryRequest.getCateId() != null) {
            //分类商品
            queryRequest = wrapperGoodsCateToEsGoodsInfoQueryRequest(queryRequest);
        }

        //店铺分类，加入所有子分类
        if (CollectionUtils.isNotEmpty(queryRequest.getStoreCateIds())) {
            queryRequest = wrapperStoreCateToEsGoodsInfoQueryRequest(queryRequest);
        }

        //设定排序
        if (queryRequest.getSortFlag() != null) {
            queryRequest = wrapperSortToEsGoodsInfoQueryRequest(queryRequest);
        }

        //聚合品牌
        queryRequest.putAgg(AggregationBuilders.terms("brand_group").field("goodsBrand.brandId").size(1000));

        if (queryRequest.isCateAggFlag()) {
            //聚合分类
            queryRequest.putAgg(AggregationBuilders.terms("cate_group").field("goodsCate.cateId"));
        }

        //聚合商品标签
        queryRequest.putAgg(AggregationBuilders.nested("goodsLabelList", "goodsLabelList")
                .subAggregation(AggregationBuilders.terms("goods_label_group")
                        .field("goodsLabelList.goodsLabelId").size(100000)));

        //渠道设置未配置或停用，不显示linkedMall商品
        filterLinkedMallShow(queryRequest);
        EsSearchResponse response = elasticsearchTemplate.query(queryRequest.getSearchCriteria(),
                searchResponse -> EsSearchResponse.build(searchResponse, resultsMapper));
        return response;
    }

    /**
     * 提取商品品牌聚合数据
     *
     * @param response
     * @return
     */
    public EsGoodsBaseResponse extractBrands(EsSearchResponse response) {
        List<? extends EsSearchResponse.AggregationResultItem> brandBucket = response.getAggResultMap().get(
                "brand_group");
        EsGoodsBaseResponse baseResponse = new EsGoodsBaseResponse();
        if (CollectionUtils.isNotEmpty(brandBucket)) {
            List<Long> brandIds =
                    brandBucket.stream().map(EsSearchResponse.AggregationResultItem<String>::getKey).map(NumberUtils::toLong).collect(Collectors.toList());
            baseResponse.setBrands(KsBeanUtil.convert(goodsBrandQueryProvider.list(GoodsBrandListRequest.builder().delFlag(DeleteFlag.NO.toValue()).brandIds(brandIds).build()).getContext().getGoodsBrandVOList(), GoodsListBrandVO.class));
        }
        return baseResponse;
    }

    /**
     * 提取商品分类聚合数据
     *
     * @param response
     * @return
     */
    public EsGoodsBaseResponse extractGoodsCate(EsSearchResponse response) {
        List<? extends EsSearchResponse.AggregationResultItem> cateBucket = response.getAggResultMap().get(
                "cate_group");
        EsGoodsBaseResponse baseResponse = new EsGoodsBaseResponse();
        if (CollectionUtils.isNotEmpty(cateBucket)) {
            List<Long> cateIds =
                    cateBucket.stream().map(EsSearchResponse.AggregationResultItem<String>::getKey).map(NumberUtils::toLong).collect(Collectors.toList());
            GoodsCateListByConditionRequest goodsCateQueryRequest = new GoodsCateListByConditionRequest();
            goodsCateQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            goodsCateQueryRequest.setCateIds(cateIds);
            GoodsCateListByConditionResponse goodsCateListByConditionResponse =
                    goodsCateQueryProvider.listByCondition(goodsCateQueryRequest).getContext();
            baseResponse.setCateList(Objects.nonNull(goodsCateListByConditionResponse) ?
                    goodsCateListByConditionResponse.getGoodsCateVOList() : Collections.emptyList());
        }
        return baseResponse;
    }

    /**
     * 提取规格与规格值聚合数据
     *
     * @param response
     * @return
     */
    public EsGoodsBaseResponse extractGoodsSpecsAndSpecDetails(EsSearchResponse response) {
        List<? extends EsSearchResponse.AggregationResultItem> specGroup = response.getAggResultMap().get(
                "specDetails");
        EsGoodsBaseResponse baseResponse = new EsGoodsBaseResponse();
        if (CollectionUtils.isNotEmpty(specGroup)) {
            List<GoodsSpecVO> goodsSpecs = new ArrayList<>();
            List<GoodsSpecDetailVO> goodsSpecDetails = new ArrayList<>();
            long i = 0;
            long j = 0;
            for (EsSearchResponse.AggregationResultItem spec : specGroup) {
                GoodsSpecVO goodsSpec = new GoodsSpecVO();
                goodsSpec.setSpecId(i);
                goodsSpec.setSpecName(spec.getKey().toString());
                goodsSpecs.add(goodsSpec);
                List<EsSearchResponse.AggregationResultItem> childs = spec.getChilds();
                for (EsSearchResponse.AggregationResultItem specDetail : childs) {
                    GoodsSpecDetailVO goodsSpecDetail = new GoodsSpecDetailVO();
                    goodsSpecDetail.setSpecDetailId(j);
                    goodsSpecDetail.setSpecId(i);
                    goodsSpecDetail.setDetailName(specDetail.getKey().toString());
                    goodsSpecDetails.add(goodsSpecDetail);
                    j++;
                }
                i++;
            }
            baseResponse.setGoodsSpecs(goodsSpecs);
            baseResponse.setGoodsSpecDetails(goodsSpecDetails);
        }
        return baseResponse;
    }

    /**
     * 包装排序字段到EsGoodsInfoQueryRequest查询对象中
     *
     * @param queryRequest
     * @return
     */
    public EsGoodsInfoQueryRequest wrapperSortToEsGoodsInfoQueryRequest(EsGoodsInfoQueryRequest queryRequest) {


        switch (queryRequest.getSortFlag()) {
            case 0:
                if (queryRequest.isQueryGoods()) {
                    queryRequest.putSort("goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("addedTime", SortOrder.DESC);
                    queryRequest.putSort("esSortPrice", SortOrder.DESC);
                } else {
                    queryRequest.putSort("goodsInfo.goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("addedTime", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.esSortPrice", SortOrder.DESC);
                }
                break;
            case 1:
                if (queryRequest.isQueryGoods()) {
                    queryRequest.putSort("addedTime", SortOrder.DESC);
                    queryRequest.putSort("goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("esSortPrice", SortOrder.DESC);
                } else {
                    queryRequest.putSort("addedTime", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.esSortPrice", SortOrder.DESC);
                }
                break;
            case 2:
                if (queryRequest.isQueryGoods()) {
                    queryRequest.putSort("esSortPrice", SortOrder.DESC);
                    queryRequest.putSort("goodsSalesNum", SortOrder.DESC);
                } else {
                    queryRequest.putSort("goodsInfo.esSortPrice", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.goodsSalesNum", SortOrder.DESC);
                }

                break;
            case 3:
                if (queryRequest.isQueryGoods()) {
                    queryRequest.putSort("esSortPrice", SortOrder.ASC);
                    queryRequest.putSort("goodsSalesNum", SortOrder.DESC);
                } else {
                    queryRequest.putSort("goodsInfo.esSortPrice", SortOrder.ASC);
                    queryRequest.putSort("goodsInfo.goodsSalesNum", SortOrder.DESC);
                }
                break;
            case 4:
                if (queryRequest.isQueryGoods()) {
                    queryRequest.putSort("goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("esSortPrice", SortOrder.DESC);
                } else {
                    queryRequest.putSort("goodsInfo.goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.esSortPrice", SortOrder.DESC);
                }
                break;
            case 5:
                if (queryRequest.isQueryGoods()) {
                    queryRequest.putSort("goodsEvaluateNum", SortOrder.DESC);
                    queryRequest.putSort("goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("esSortPrice", SortOrder.DESC);
                } else {
                    queryRequest.putSort("goodsInfo.goodsEvaluateNum", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.esSortPrice", SortOrder.DESC);
                }
                break;
            case 6:
                if (queryRequest.isQueryGoods()) {
                    queryRequest.putSort("goodsFeedbackRate", SortOrder.DESC);
                    queryRequest.putSort("goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("esSortPrice", SortOrder.DESC);
                } else {
                    queryRequest.putSort("goodsInfo.goodsFeedbackRate", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.esSortPrice", SortOrder.DESC);
                }
                break;
            case 7:
                if (queryRequest.isQueryGoods()) {
                    queryRequest.putSort("goodsCollectNum", SortOrder.DESC);
                    queryRequest.putSort("goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("esSortPrice", SortOrder.DESC);
                } else {
                    queryRequest.putSort("goodsInfo.goodsCollectNum", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.goodsSalesNum", SortOrder.DESC);
                    queryRequest.putSort("goodsInfo.esSortPrice", SortOrder.DESC);
                }
                break;
            case 8:
                // 按照佣金降序排序
                queryRequest.putSort("goodsInfo.distributionCommission", SortOrder.DESC);
                queryRequest.putSort("addedTime", SortOrder.DESC);

                break;
            case 9:
                // 按照佣金升序排序
                queryRequest.putSort("goodsInfo.distributionCommission", SortOrder.ASC);
                queryRequest.putSort("addedTime", SortOrder.DESC);

                break;
            case 10:
                // 按照排序号倒序->上架时间倒序
                queryRequest.putSort("sortNo", SortOrder.DESC);
                queryRequest.putSort("addedTime", SortOrder.DESC);
                break;
            case 11:
                // 按照创建时间排序
                queryRequest.putSort("addedTime", SortOrder.DESC);
                break;
            default:
                break;
        }
        if (StringUtils.isNotBlank(queryRequest.getKeywords())) {
            queryRequest.putScoreSort();

        }
        return queryRequest;
    }

    /**
     * 包装店铺分类信息到EsGoodsInfoQueryRequest查询对象中
     *
     * @param queryRequest
     * @return
     */
    public EsGoodsInfoQueryRequest wrapperStoreCateToEsGoodsInfoQueryRequest(EsGoodsInfoQueryRequest queryRequest) {
        BaseResponse<StoreCateListByStoreCateIdAndIsHaveSelfResponse> baseResponse =
                storeCateQueryProvider.listByStoreCateIdAndIsHaveSelf(new StoreCateListByStoreCateIdAndIsHaveSelfRequest(queryRequest.getStoreCateIds().get(0), false));
        StoreCateListByStoreCateIdAndIsHaveSelfResponse storeCateListByStoreCateIdAndIsHaveSelfResponse =
                baseResponse.getContext();
        if (Objects.nonNull(storeCateListByStoreCateIdAndIsHaveSelfResponse)) {
            List<StoreCateVO> storeCateVOList = storeCateListByStoreCateIdAndIsHaveSelfResponse.getStoreCateVOList();
            queryRequest.getStoreCateIds().addAll(storeCateVOList.stream().map(StoreCateVO::getStoreCateId).collect(Collectors.toList()));
        }
        return queryRequest;
    }

    /**
     * 包装分类商品信息到EsGoodsInfoQueryRequest查询对象中
     *
     * @param queryRequest
     * @return
     */
    public EsGoodsInfoQueryRequest wrapperGoodsCateToEsGoodsInfoQueryRequest(EsGoodsInfoQueryRequest queryRequest) {
        GoodsCateByIdResponse cate =
                goodsCateQueryProvider.getById(new GoodsCateByIdRequest(queryRequest.getCateId())).getContext();
        if (cate != null) {
            GoodsCateListByConditionRequest goodsCateListByConditionRequest = new GoodsCateListByConditionRequest();
            goodsCateListByConditionRequest.setLikeCatePath(ObjectUtils.toString(cate.getCatePath()).concat(String.valueOf(cate.getCateId())).concat("|"));
            List<GoodsCateVO> t_cateList = null;
            BaseResponse<GoodsCateListByConditionResponse> baseResponse =
                    goodsCateQueryProvider.listByCondition(goodsCateListByConditionRequest);
            GoodsCateListByConditionResponse goodsCateListByConditionResponse = baseResponse.getContext();
            if (Objects.nonNull(goodsCateListByConditionResponse)) {
                t_cateList = goodsCateListByConditionResponse.getGoodsCateVOList();
            }
            if (CollectionUtils.isNotEmpty(t_cateList)) {
                queryRequest.setCateIds(t_cateList.stream().map(GoodsCateVO::getCateId).collect(Collectors.toList()));
                queryRequest.getCateIds().add(queryRequest.getCateId());
                queryRequest.setCateId(null);
            }
        }
        return queryRequest;
    }

    /**
     * 分页查询ES商品(实现WEB的商品列表)
     *
     * @param queryRequest
     * @return
     */
    public EsGoodsResponse pageByGoods(EsGoodsInfoQueryRequest queryRequest) {
        EsGoodsResponse goodsResponse = EsGoodsResponse.builder().build();
        //仅显示商家的
        //queryRequest.setGoodsSource(GoodsSource.SELLER);
        if (StringUtils.isNotBlank(queryRequest.getKeywords())) {
            queryRequest.setKeywords(this.analyze(queryRequest.getKeywords()));
        }

        if (queryRequest.getCateId() != null) {
            //分类商品
            queryRequest = wrapperGoodsCateToEsGoodsInfoQueryRequest(queryRequest);
        }

        //店铺分类，加入所有子分类
        if (CollectionUtils.isNotEmpty(queryRequest.getStoreCateIds())) {
            queryRequest = wrapperStoreCateToEsGoodsInfoQueryRequest(queryRequest);
        }

        //设定排序
        if (queryRequest.getSortFlag() != null) {
            queryRequest = wrapperSortToEsGoodsInfoQueryRequest(queryRequest);
        }

        //聚合品牌
        queryRequest.putAgg(AggregationBuilders.terms("brand_group").field("goodsBrand.brandId").size(100000));

        if (queryRequest.isCateAggFlag()) {
            //聚合分类
            queryRequest.putAgg(AggregationBuilders.terms("cate_group").field("goodsCate.cateId"));
        }

        //聚合商品标签
        queryRequest.putAgg(AggregationBuilders.nested("goodsLabelList", "goodsLabelList")
                .subAggregation(AggregationBuilders.terms("goods_label_group")
                        .field("goodsLabelList.goodsLabelId").size(100000)));

        //渠道设置未配置或停用，不显示linkedMall商品
        this.filterLinkedMallShow(queryRequest);
        EsSearchResponse response = elasticsearchTemplate.query(queryRequest.getSearchCriteria(),
                searchResponse -> EsSearchResponse.buildGoods(searchResponse, resultsMapper));

        if (response.getGoodsData().size() < 1) {
            goodsResponse.setEsGoods(new MicroServicePage<>(response.getGoodsData(),
                    PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), response.getTotal()));
            return goodsResponse;
        }

        // 过滤不展示的商品标签
        response.getGoodsData().forEach(goods -> {
            if (CollectionUtils.isNotEmpty(goods.getGoodsLabelList())) {
                List<GoodsLabelNestVO> goodsLabelList =
                        goods.getGoodsLabelList().stream()
                                .filter(label -> Boolean.TRUE.equals(label.getLabelVisible()) && Objects.equals(label.getDelFlag(), DeleteFlag.NO))
                                .sorted(Comparator.comparing(GoodsLabelNestVO::getLabelSort).thenComparing(GoodsLabelNestVO::getGoodsLabelId).reversed())
                                .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(goodsLabelList)) {
                    goods.setGoodsLabelList(goodsLabelList);
                }
            }
        });

        List<GoodsInfoNestVO> goodsInfoVOList =
                response.getGoodsData().stream().map(EsGoodsVO::getGoodsInfos).flatMap(Collection::stream).collect(Collectors.toList());
        for (GoodsInfoNestVO goodsInfo : goodsInfoVOList) {
            goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO :
                    goodsInfo.getMarketPrice());
            if (Objects.isNull(goodsInfo.getStock()) || goodsInfo.getStock() < 1) {
                goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
            }
        }

        //提取聚合数据
        EsGoodsBaseResponse baseResponse = extractBrands(response);
        goodsResponse.setBrands(KsBeanUtil.convert(baseResponse.getBrands(), GoodsListBrandVO.class));

        if (queryRequest.isCateAggFlag()) {
            //提取聚合数据
            baseResponse = extractGoodsCate(response);
            goodsResponse.setCateList(baseResponse.getCateList());
        }

        //提取规格与规格值聚合数据
        baseResponse = extractGoodsSpecsAndSpecDetails(response);
        //提取商品标签数据
        goodsResponse.setGoodsLabelVOList(esGoodsLabelService.extractGoodsLabel(response));
        goodsResponse.setGoodsSpecs(baseResponse.getGoodsSpecs());
        goodsResponse.setGoodsSpecDetails(baseResponse.getGoodsSpecDetails());

        goodsResponse.setEsGoods(new MicroServicePage<>(response.getGoodsData(), PageRequest.of(queryRequest.getPageNum(),
                queryRequest.getPageSize()), response.getTotal()));
        return goodsResponse;
    }

    //更新与供应商关联的商家商品es
    public void initProviderEsGoodsInfo(Long storeId, List<String> providerGoodsIds) {
        List<String> goodsIds = new ArrayList<>();
        if (storeId != null && CollectionUtils.isEmpty(providerGoodsIds)) {
            GoodsByConditionResponse goodsByConditionResponse = goodsQueryProvider.listByCondition(GoodsByConditionRequest.builder().storeId(storeId).build()).getContext();
            if (goodsByConditionResponse != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(goodsByConditionResponse.getGoodsVOList())) {
                providerGoodsIds = goodsByConditionResponse.getGoodsVOList().stream().map(GoodsVO::getGoodsId).distinct().collect(Collectors.toList());
            }
        }
        if (CollectionUtils.isNotEmpty(providerGoodsIds)) {
            GoodsListByIdsResponse goodsListByIdsResponse = goodsQueryProvider.listByProviderGoodsId(GoodsListByIdsRequest.builder().goodsIds(providerGoodsIds).build()).getContext();
            if (goodsListByIdsResponse != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(goodsListByIdsResponse.getGoodsVOList())) {
                goodsIds = goodsListByIdsResponse.getGoodsVOList().stream().map(GoodsVO::getGoodsId).distinct().collect(Collectors.toList());
            }
            goodsIds.addAll(providerGoodsIds);
        }
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            esGoodsElasticService.initEsGoods(EsGoodsInfoRequest.builder().goodsIds(goodsIds).build());
        }
    }

    /**
     * 初始化SKU持化于ES
     */
    public void initEsGoodsInfo(EsGoodsInfoRequest request) {
        esGoodsElasticService.initEsGoods(request);
    }

    /**
     * 更新spu扩展属性
     * @param props
     */
    public void updateEsGoodsExtProp(List<Object[]> props){
        esGoodsElasticService.setExtPropForGoods(props);
    }
    /**
     * 上下架
     *
     * @param addedFlag    上下架状态
     * @param goodsIds     商品id列表
     * @param goodsInfoIds 商品skuId列表
     */
    public void updateAddedStatus(Integer addedFlag, List<String> goodsIds, List<String> goodsInfoIds) {

        Client client = elasticsearchTemplate.getClient();

        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        GoodsInfoListByConditionRequest infoQueryRequest = new GoodsInfoListByConditionRequest();
        infoQueryRequest.setShowPointFlag(true);
        if (goodsIds != null) { // 如果传了goodsIds，则按spu查
            queryRequest.setGoodsIds(goodsIds);
            infoQueryRequest.setGoodsIds(goodsIds);
        }
        if (goodsInfoIds != null) { // 如果传了goodsInfoIds，则按sku查
            queryRequest.setGoodsInfoIds(goodsInfoIds);
            infoQueryRequest.setGoodsInfoIds(goodsInfoIds);
        }
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        LocalDateTime now = LocalDateTime.now();

        Map<String, GoodsInfoVO> goodsInfoMap =
                goodsInfoQueryProvider.listByCondition(infoQueryRequest).getContext().getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, c -> c));
        if (esGoodsInfoList != null) {
            esGoodsInfoList.forEach(esGoodsInfo -> {
                esGoodsInfo.getGoodsInfo().setAddedFlag(addedFlag);
                if (goodsInfoMap.containsKey(esGoodsInfo.getGoodsInfo().getGoodsInfoId())) {
                    GoodsInfoVO info = goodsInfoMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId());
                    esGoodsInfo.getGoodsInfo().setAddedTime(info.getAddedTime());
                    esGoodsInfo.setAddedTime(info.getAddedTime());
                } else {
                    esGoodsInfo.getGoodsInfo().setAddedTime(now);
                    esGoodsInfo.setAddedTime(now);
                }
                esGoodsInfo.getGoodsInfo().setEsSortPrice();

                // EsGoodsInfo里的addedTime
                Map<String, Object> esGoodsInfoMap = new HashMap<>();
                esGoodsInfoMap.put("addedTime", esGoodsInfo.getAddedTime().format(DateTimeFormatter.ofPattern("yyyy" +
                        "-MM-dd HH:mm:ss.SSS")));

                // EsGoodsInfo里的GoodsInfo
                Map<String, String> map = new HashMap<>();
                map.put("addedFlag", addedFlag == null ? "" : addedFlag.toString());
                map.put("addedTime", esGoodsInfo.getGoodsInfo().getAddedTime().format(DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm:ss.SSS")));
                esGoodsInfoMap.put("goodsInfo", map);

                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setType(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setId(esGoodsInfo.getId()).setDoc(esGoodsInfoMap).execute().actionGet();
            });
        }
        queryRequest.setQueryGoods(true);
        SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoods.class);
        List<IndexQuery> esGoodsQuery = new ArrayList<>();
        if (esGoodsList != null) {
            esGoodsList.forEach(esGoods -> {
                esGoods.setAddedFlag(addedFlag);
                esGoods.getGoodsInfos().forEach(esGoodsInfo -> {
                    esGoodsInfo.setAddedFlag(addedFlag);
                    if (goodsInfoMap.containsKey(esGoodsInfo.getGoodsInfoId())) {
                        GoodsInfoVO info = goodsInfoMap.get(esGoodsInfo.getGoodsInfoId());
                        esGoodsInfo.setAddedTime(info.getAddedTime());
                        esGoods.setAddedTime(info.getAddedTime());
                    } else {
                        esGoods.setAddedTime(now);
                        esGoodsInfo.setAddedTime(now);
                    }
                    esGoodsInfo.setEsSortPrice();
                });

                IndexQuery iq = new IndexQuery();
                iq.setId(esGoods.getId());
                iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                iq.setType(EsConstants.DOC_GOODS_TYPE);
                iq.setObject(esGoods);
                esGoodsQuery.add(iq);

            });
            if (CollectionUtils.isNotEmpty(esGoodsQuery)) {
                elasticsearchTemplate.bulkIndex(esGoodsQuery);
            }
        }
    }

    /**
     * 根据商品批量删除
     *
     * @param goodsIds
     */
    public void deleteByGoods(List<String> goodsIds) {
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setGoodsIds(goodsIds);
        this.delete(queryRequest);
    }

    /**
     * 批量删除
     *
     * @param skuIds SKU编号
     */
    public void delete(List<String> skuIds) {
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setGoodsInfoIds(skuIds);
        this.delete(queryRequest);
    }

    /**
     * 批量删除
     *
     * @param queryRequest 参数
     */
    public void delete(EsGoodsInfoQueryRequest queryRequest) {
        Client client = elasticsearchTemplate.getClient();
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        if (esGoodsInfoList != null) {
            esGoodsInfoList.forEach(esGoodsInfo ->
                    client.prepareDelete()
                            .setIndex(EsConstants.DOC_GOODS_INFO_TYPE)
                            .setType(EsConstants.DOC_GOODS_INFO_TYPE)
                            .setId(esGoodsInfo.getId()).execute().actionGet());
        }
        queryRequest.setQueryGoods(true);
        SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoods.class);
        elasticsearchTemplate.refresh(EsGoodsInfo.class);
        if (esGoodsList != null) {
            esGoodsList.forEach(esGoods -> {
                client.prepareDelete()
                        .setIndex(EsConstants.DOC_GOODS_TYPE)
                        .setType(EsConstants.DOC_GOODS_TYPE)
                        .setId(esGoods.getId()).execute().actionGet();
            });
        }
        elasticsearchTemplate.refresh(EsGoods.class);
    }


    /**
     * 使用标准分词对字符串分词
     *
     * @param text 待分词文本
     * @return 分此后的词条
     */
    private String analyze(String text) {
        return analyze(text, "simple");
    }

    /**
     * 根据给定的分词器对字符串进行分词
     *
     * @param text     要分词的文本
     * @param analyzer 指定分词器
     * @return 分词后的词条列表
     */
    private String analyze(String text, String analyzer) {
        Client client = elasticsearchTemplate.getClient();
        final String fAnalyzer = StringUtils.isBlank(analyzer) ? "simple" : analyzer;
        AnalyzeRequestBuilder requestBuilder = client.admin().indices().prepareAnalyze(text).setAnalyzer(fAnalyzer);
        AnalyzeResponse response = client.admin().indices().analyze(requestBuilder.request()).actionGet();
        List<String> res = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(response.getTokens())) {
            res.addAll(response.getTokens().stream().map(AnalyzeResponse.AnalyzeToken::getTerm).collect(Collectors.toList()));
        }
        res.addAll(Arrays.asList(text.split("[^0-9]+")));
        res.add(text);
        return StringUtils.join(res.stream().distinct().collect(Collectors.toList()), " ");
    }

    /**
     * 根据商品spu批量获取商品属性关键Map
     *
     * @param goodsIds 商品id
     * @return 商品属性关键Map内容<商品id, 商品属性关联list>
     */
    private Map<String, List<GoodsPropDetailRelVO>> getPropDetailRelList(List<String> goodsIds) {
        GoodsPropDetailRelByIdsRequest relByIdsRequest = new GoodsPropDetailRelByIdsRequest();
        relByIdsRequest.setGoodsIds(goodsIds);
        List<GoodsPropDetailRelVO> goodsPropDetailRelVOList =
                goodsQueryProvider.getRefByGoodIds(relByIdsRequest).getContext().getGoodsPropDetailRelVOList();
        return goodsPropDetailRelVOList.stream().collect(Collectors.groupingBy(GoodsPropDetailRelVO::getGoodsId));
    }

    /**
     * 根据商品spu批量获取规格键值Map
     *
     * @param goodsIds 商品id
     * @return 规格键值Map内容<规格id, 规格名称>
     */
    private Map<Long, String> getGoodsSpecMapByGoodsId(List<String> goodsIds) {
        List<GoodsSpecVO> voList = goodsSpecQueryProvider.listByGoodsIds(
                GoodsSpecListByGoodsIdsRequest.builder().goodsIds(goodsIds).build()).getContext().getGoodsSpecVOList();
        return voList.stream().collect(Collectors.toMap(GoodsSpecVO::getSpecId, GoodsSpecVO::getSpecName));
    }

    /**
     * 根据商品sku批量获取区间价键值Map
     *
     * @param skuIds 商品skuId
     * @return 区间价键值Map内容<商品skuId, 区间价列表>
     */
    private Map<String, List<GoodsIntervalPriceVO>> getIntervalPriceMapBySkuId(List<String> skuIds) {
        List<GoodsIntervalPriceVO> voList = goodsIntervalPriceQueryProvider.listByGoodsIds(
                GoodsIntervalPriceListBySkuIdsRequest.builder().skuIds(skuIds).build()).getContext().getGoodsIntervalPriceVOList();
        return voList.stream().collect(Collectors.groupingBy(GoodsIntervalPriceVO::getGoodsInfoId));
    }

    /**
     * 删除品牌时，更新es数据
     */
    public void delBrandIds(List<Long> brandIds, Long storeId) {
        deleteBrandNameCommon(brandIds, EsConstants.DOC_GOODS_TYPE, storeId);
        deleteBrandNameCommon(brandIds, EsConstants.DOC_GOODS_INFO_TYPE, storeId);

    }

    /**
     * 删除店铺分类时更新es数据
     *
     * @param storeCateIds
     * @param storeId
     */
    public void delStoreCateIds(List<Long> storeCateIds, Long storeId) {
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setStoreCateIds(storeCateIds);
        queryRequest.setStoreId(storeId);
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        if (!esGoodsInfoList.iterator().hasNext()) {
            return;
        }
        List<IndexQuery> esGoodsInfos = new ArrayList<>();
        esGoodsInfoList.forEach(item -> {
                    item.setStoreCateIds(null);
                    IndexQuery iq = new IndexQuery();
                    iq.setObject(item);
                    iq.setIndexName(EsConstants.DOC_GOODS_INFO_TYPE);
//                    iq.setIndexName(EsConstants.INDEX_NAME);
                    iq.setType(EsConstants.DOC_GOODS_INFO_TYPE);
//                    iq.setParentId(item.getCateBrandId());
                    esGoodsInfos.add(iq);
                }
        );
        //生成新数据
        elasticsearchTemplate.bulkIndex(esGoodsInfos);
    }

    /**
     * 更新classify
     */
    public void updateClassify(Integer id, String name) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new TermQueryBuilder("classify.id", id)).build();
        List<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(searchQuery, EsGoods.class);
        if(CollectionUtils.isEmpty(esGoodsList)){
            return;
        }
        List<IndexQuery> esGood = new ArrayList<>();
        for (EsGoods esGoods : esGoodsList) {
            List<ClassifySimpleProviderResponse> classify = esGoods.getClassify();
            for (ClassifySimpleProviderResponse classifySimpleProviderResponse : classify) {
                if(classifySimpleProviderResponse.getId().equals(id)){
                    classifySimpleProviderResponse.setClassifyName(name);
                    IndexQuery iq = new IndexQuery();
                    iq.setObject(esGoods);
                    iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                    iq.setType(EsConstants.DOC_GOODS_TYPE);
                    esGood.add(iq);
                    break;
                }
            }
        }
        elasticsearchTemplate.bulkIndex(esGood);
    }

    /**
     * 删除classify
     */
    public void deleteClassify(Integer id) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new TermQueryBuilder("classify.id", id)).build();
        List<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(searchQuery, EsGoods.class);
        if(CollectionUtils.isEmpty(esGoodsList)){
            return;
        }
        List<IndexQuery> esGood = new ArrayList<>();
        for (EsGoods esGoods : esGoodsList) {
            List<ClassifySimpleProviderResponse> classify = esGoods.getClassify();
            Iterator<ClassifySimpleProviderResponse> iterator = classify.iterator();
            while (iterator.hasNext()) {
                ClassifySimpleProviderResponse classifySimpleProviderResponse = iterator.next();
                if(classifySimpleProviderResponse.getId().equals(id)) iterator.remove();
                IndexQuery iq = new IndexQuery();
                iq.setObject(esGoods);
                iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                iq.setType(EsConstants.DOC_GOODS_TYPE);
                esGood.add(iq);
                break;
            }
        }
        elasticsearchTemplate.bulkIndex(esGood);
    }

    /**
     * 更新分销佣金、分销商品审核状态（添加分销商品时）
     *
     * @param esGoodsInfoDistributionRequest
     * @return true:批量更新成功，false:批量更新失败（部分更新成功）
     */
    public Boolean modifyDistributionCommission(EsGoodsInfoModifyDistributionCommissionRequest esGoodsInfoDistributionRequest) {
        Client client = elasticsearchTemplate.getClient();
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        List<DistributionGoodsInfoModifyDTO> distributionGoodsInfoDTOList =
                esGoodsInfoDistributionRequest.getDistributionGoodsInfoDTOList();
        List<String> goodsInfoIds =
                distributionGoodsInfoDTOList.stream().map(DistributionGoodsInfoModifyDTO::getGoodsInfoId).collect(Collectors.toList());
        Map<String, BigDecimal> stringBigDecimalMap = distributionGoodsInfoDTOList.stream().collect(Collectors.toMap
                (DistributionGoodsInfoModifyDTO::getGoodsInfoId, g -> g.getCommissionRate()));
        Map<String, BigDecimal> commssionlMap = distributionGoodsInfoDTOList.stream().collect(Collectors.toMap
                (DistributionGoodsInfoModifyDTO::getGoodsInfoId, g -> g.getDistributionCommission()));
        queryRequest.setGoodsInfoIds(goodsInfoIds);

        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        Integer result = 0;
        if (esGoodsInfoList != null) {
            Map<String, Object> esGoodsInfoMap;
            Map<String, Object> map;
            for (EsGoodsInfo esGoodsInfo : esGoodsInfoList) {
                esGoodsInfoMap = new HashMap<>(4);
                map = new HashMap<>(4);
                BigDecimal commission = commssionlMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId());
                BigDecimal commissionRate = stringBigDecimalMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId());
                if (Objects.nonNull(commissionRate)) {
                    map.put("commissionRate", commissionRate.doubleValue());
                }
                if (Objects.nonNull(commission)) {
                    map.put("distributionCommission", commission.doubleValue());
                }
                map.put("distributionGoodsAudit", esGoodsInfoDistributionRequest.getDistributionGoodsAudit().toValue());
                esGoodsInfoMap.put("goodsInfo", map);
                if (Objects.nonNull(esGoodsInfoDistributionRequest.getDistributionGoodsStatus())) {
                    esGoodsInfoMap.put("distributionGoodsStatus",
                            esGoodsInfoDistributionRequest.getDistributionGoodsStatus());
                }
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setType(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setId(esGoodsInfo.getId()).setDoc(esGoodsInfoMap).execute().actionGet();
                result++;
            }
            queryRequest.setQueryGoods(true);
            SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
            Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoods.class);
            List<IndexQuery> esGoodsQuery = new ArrayList<>();
            if (esGoodsList != null) {
                esGoodsList.forEach(esGoods -> {
                    esGoods.getGoodsInfos().forEach(esGoodsInfo -> {
                        BigDecimal commissionRate = stringBigDecimalMap.get(esGoodsInfo.getGoodsInfoId());
                        BigDecimal commission = commssionlMap.get(esGoodsInfo.getGoodsInfoId());
                        if (Objects.nonNull(commissionRate)) {
                            esGoodsInfo.setCommissionRate(commissionRate);
                            esGoodsInfo.setDistributionCommission(commission);
                        }
                        boolean isDistributionGoods = esGoodsInfoDistributionRequest.getDistributionGoodsInfoDTOList().parallelStream().anyMatch(goodsInfo -> StringUtils.equals(goodsInfo.getGoodsInfoId(), esGoodsInfo.getGoodsInfoId()));
                        if (isDistributionGoods) {
                            esGoodsInfo.setDistributionGoodsAudit(esGoodsInfoDistributionRequest.getDistributionGoodsAudit());
                        }
                    });
                    if (Objects.nonNull(esGoodsInfoDistributionRequest.getDistributionGoodsStatus())) {
                        esGoods.setDistributionGoodsStatus(esGoodsInfoDistributionRequest.getDistributionGoodsStatus());
                    }
                    IndexQuery iq = new IndexQuery();
                    iq.setId(esGoods.getId());
                    iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                    iq.setType(EsConstants.DOC_GOODS_TYPE);
                    iq.setObject(esGoods);
                    esGoodsQuery.add(iq);

                });
                if (CollectionUtils.isNotEmpty(esGoodsQuery)) {
                    elasticsearchTemplate.bulkIndex(esGoodsQuery);
                }
            }
        }
        return result.equals(goodsInfoIds.size()) ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * 更新分销商品审核状态（平台端审核时）
     *
     * @param request
     * @return true:批量更新成功，false:批量更新失败（部分更新成功）
     */
    public Boolean modifyDistributionGoodsAudit(EsGoodsInfoModifyDistributionGoodsAuditRequest request) {
        Client client = elasticsearchTemplate.getClient();
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        List<String> goodsInfoIds = request.getGoodsInfoIds();
        queryRequest.setGoodsInfoIds(goodsInfoIds);
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        Integer result = 0;
        if (esGoodsInfoList != null) {
            Integer distributionGoodsAudit = request.getDistributionGoodsAudit();
            DistributionGoodsAudit goodsAudit = DistributionGoodsAudit.values()[distributionGoodsAudit];
            Map<String, Object> esGoodsInfoMap;
            Map<String, Object> map;
            for (EsGoodsInfo esGoodsInfo : esGoodsInfoList) {
                esGoodsInfoMap = new HashMap<>(4);
                map = new HashMap<>(4);
                map.put("distributionGoodsAuditReason",
                        distributionGoodsAudit.equals(DistributionGoodsAudit.CHECKED.toValue()) ? "" :
                                request.getDistributionGoodsAuditReason());
                map.put("distributionGoodsAudit", distributionGoodsAudit);
                esGoodsInfoMap.put("goodsInfo", map);
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setType(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setId(esGoodsInfo.getId()).setDoc(esGoodsInfoMap).execute().actionGet();

                result++;
            }
            queryRequest.setQueryGoods(true);
            SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
            Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoods.class);
            List<IndexQuery> esGoodsQuery = new ArrayList<>();
            if (esGoodsList != null) {
                esGoodsList.forEach(esGoods -> {
                    esGoods.getGoodsInfos().forEach(esGoodsInfo -> {
                        Boolean aBoolean =
                                goodsInfoIds.stream().anyMatch(goodsInfoId -> goodsInfoId.equals(esGoodsInfo.getGoodsInfoId()));
                        if (aBoolean) {
                            esGoodsInfo.setDistributionGoodsAudit(goodsAudit);
                            esGoodsInfo.setDistributionGoodsAuditReason(distributionGoodsAudit.equals(DistributionGoodsAudit.CHECKED.toValue()) ? "" : request.getDistributionGoodsAuditReason());

                        }
                    });

                    IndexQuery iq = new IndexQuery();
                    iq.setId(esGoods.getId());
                    iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                    iq.setType(EsConstants.DOC_GOODS_TYPE);
                    iq.setObject(esGoods);
                    esGoodsQuery.add(iq);

                });
                if (CollectionUtils.isNotEmpty(esGoodsQuery)) {
                    elasticsearchTemplate.bulkIndex(esGoodsQuery);
                }
            }
        }
        return result.equals(goodsInfoIds.size()) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 商家-社交分销开关设置，更新分销商品状态
     *
     * @param request
     * @return
     */
    public Boolean modifyDistributionGoodsStatus(EsGoodsInfoModifyDistributionGoodsStatusRequest request) {
        Client client = elasticsearchTemplate.getClient();
        Long storeId = request.getStoreId();
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED.toValue());
        queryRequest.setStoreId(storeId);
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        if (esGoodsInfoList != null) {
            Integer distributionGoodsStatus = request.getDistributionGoodsStatus();
            for (EsGoodsInfo esGoodsInfo : esGoodsInfoList) {
                Map<String, Object> map = new HashMap<>(4);
                map.put("distributionGoodsStatus", distributionGoodsStatus);
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setType(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setId(esGoodsInfo.getId()).setDoc(map).execute().actionGet();
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_GOODS_TYPE)
                        .setType(EsConstants.DOC_GOODS_TYPE)
                        .setId(esGoodsInfo.getGoodsInfo().getGoodsId()).setDoc(map).execute().actionGet();
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 更新分销商品审核状态（修改商品销售模式：零售->批发）
     *
     * @param spuId
     * @return true:批量更新成功，false:批量更新失败（部分更新成功）
     */
    public Boolean modifyDistributionGoodsAudit(String spuId) {
        Client client = elasticsearchTemplate.getClient();
        EsGoodsInfoQueryRequest request = new EsGoodsInfoQueryRequest();
        request.setGoodsIds(Collections.singletonList(spuId));
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(request.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        if (esGoodsInfoList != null) {
            Map<String, Object> esGoodsInfoMap;
            Map<String, Object> map;
            for (EsGoodsInfo esGoodsInfo : esGoodsInfoList) {
                esGoodsInfoMap = new HashMap<>(4);
                map = new HashMap<>(4);
                map.put("distributionGoodsAuditReason", "");
                map.put("distributionGoodsAudit", DistributionGoodsAudit.COMMON_GOODS.toValue());
                esGoodsInfoMap.put("goodsInfo", map);
                client.prepareUpdate()
                        .setIndex(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setType(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setId(esGoodsInfo.getId()).setDoc(esGoodsInfoMap).execute().actionGet();
            }
            request.setQueryGoods(true);
            SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(request.getWhereCriteria()).build();
            Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoods.class);
            List<IndexQuery> esGoodsQuery = new ArrayList<>();
            if (esGoodsList != null) {
                esGoodsList.forEach(esGoods -> {
                    esGoods.getGoodsInfos().forEach(esGoodsInfo -> {
                        esGoodsInfo.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                        esGoodsInfo.setDistributionGoodsAuditReason("");
                    });

                    IndexQuery iq = new IndexQuery();
                    iq.setId(esGoods.getId());
                    iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                    iq.setType(EsConstants.DOC_GOODS_TYPE);
                    iq.setObject(esGoods);
                    esGoodsQuery.add(iq);

                });
                if (CollectionUtils.isNotEmpty(esGoodsQuery)) {
                    elasticsearchTemplate.bulkIndex(esGoodsQuery);
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 修改spu和sku的商品分类索引信息
     *
     * @param goodsCateVO
     */
    public void updateCateName(GoodsCateVO goodsCateVO) {
        updateCateNameCommon(goodsCateVO);
    }

    /**
     * 修改spu和sku的商品品牌索引信息
     *
     * @param goodsBrandVO
     */
    public void updateBrandName(GoodsBrandVO goodsBrandVO) {
        updateBrandNameCommon(goodsBrandVO);
    }

    /**
     * 修改商品分类
     *
     * @param goodsCateVO 分类bean
     * @return
     */
    private Long updateCateNameCommon(GoodsCateVO goodsCateVO) {
        String queryName = "goodsCate.cateId";
        String updateName = "goodsCate.cateName";
        Long resCount = 0L;
        if (Objects.nonNull(goodsCateVO)) {
            Client client = elasticsearchTemplate.getClient();

            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);

            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE, EsConstants.DOC_GOODS_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.termQuery(queryName, goodsCateVO.getCateId()))
                    //修改操作
                    .script(new Script("ctx._source." + updateName + "='" + goodsCateVO.getCateName() + "'"));
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
        }
        return resCount;
    }

    /**
     * 修改商品品牌
     *
     * @param goodsBrandVO 品牌bean
     * @return
     */
    private Long updateBrandNameCommon(GoodsBrandVO goodsBrandVO) {
        String queryName = "goodsBrand.brandId";
        String updateName = "goodsBrand.brandName";
        Long resCount = 0L;
        if (Objects.nonNull(goodsBrandVO)) {
            Client client = elasticsearchTemplate.getClient();

            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);

            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE, EsConstants.DOC_GOODS_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.termQuery(queryName, goodsBrandVO.getBrandId()))
                    //修改操作
                    .script(new Script("ctx._source." + updateName + "='" + goodsBrandVO.getBrandName() + "'"));
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
        }
        return resCount;
    }

    /**
     * 删除商品品牌之后同步es
     *
     * @param ids       删除的品牌Id
     * @param indexName 索引名称
     * @param storeId   店铺Id
     * @return
     */
    private Long deleteBrandNameCommon(List<Long> ids, String indexName, Long storeId) {
        String queryName = "goodsBrand.brandId";
        String queryStoreName = StringUtils.equals(EsConstants.DOC_GOODS_TYPE, indexName) ? "goodsInfos.storeId" :
                "goodsInfo.storeId";
        Long resCount = 0L;
        if (CollectionUtils.isNotEmpty(ids)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery(queryName, ids));
            if (Objects.nonNull(storeId)) {
                boolQueryBuilder.must(termQuery(queryStoreName, storeId));
            }
            updateByQuery = updateByQuery.source(indexName)
                    //查询要修改的结果集
                    .filter(boolQueryBuilder)
                    //修改操作
                    .script(new Script("ctx._source.goodsBrand.brandName='';ctx._source.goodsBrand.brandId='';ctx" +
                            "._source.goodsBrand.pinyin=''"));
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
        }
        return resCount;
    }

    /**
     * 新增不需要审核的企业购商品时 刷新es
     *
     * @param batchEnterPrisePriceDTOS
     */
    public Boolean updateEnterpriseGoodsInfo(List<BatchEnterPrisePriceDTO> batchEnterPrisePriceDTOS, EnterpriseAuditState enterpriseAuditState) {
        Client client = elasticsearchTemplate.getClient();
        List<String> goodsInfoIds = batchEnterPrisePriceDTOS.stream().map(BatchEnterPrisePriceDTO::getGoodsInfoId).collect(Collectors.toList());
        Map<String, BigDecimal> skuIdEnterprisePriceMap = new HashMap<>();
        batchEnterPrisePriceDTOS.forEach(b -> skuIdEnterprisePriceMap.put(b.getGoodsInfoId(), b.getEnterPrisePrice()));
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setGoodsInfoIds(goodsInfoIds);
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        Integer result = 0;
        if (esGoodsInfoList != null) {
            Map<String, Object> esGoodsInfoMap;
            Map<String, Object> map;
            queryRequest.setQueryGoods(true);
            SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
            Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoods.class);
            List<IndexQuery> esGoodsQuery = new ArrayList<>();
            if (esGoodsList != null) {
                for (EsGoodsInfo esGoodsInfo : esGoodsInfoList) {
                    esGoodsInfoMap = new HashMap<>(4);
                    map = new HashMap<>(4);
                    map.put("enterPriseAuditStatus", enterpriseAuditState.toValue());
                    map.put("enterPrisePrice", String.valueOf(skuIdEnterprisePriceMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId())));
                    esGoodsInfoMap.put("goodsInfo", map);
                    if (EnterpriseAuditState.CHECKED.equals(enterpriseAuditState)) {
                        map.put("esSortPrice", String.valueOf(skuIdEnterprisePriceMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId())));
                    }
                    client.prepareUpdate()
                            .setType(EsConstants.DOC_GOODS_INFO_TYPE)
                            .setIndex(EsConstants.DOC_GOODS_INFO_TYPE)
                            .setId(esGoodsInfo.getId()).setDoc(esGoodsInfoMap).execute().actionGet();
                    result++;
                }
                esGoodsList.forEach(esGoods -> {
                    esGoods.getGoodsInfos().forEach(esGoodsInfo -> {
                        Boolean aBoolean =
                                goodsInfoIds.stream().anyMatch(goodsInfoId -> goodsInfoId.equals(esGoodsInfo.getGoodsInfoId()));
                        if (aBoolean) {
                            esGoodsInfo.setEnterPriseAuditStatus(enterpriseAuditState.toValue());
                            esGoodsInfo.setEnterPrisePrice(skuIdEnterprisePriceMap.get(esGoodsInfo.getGoodsInfoId()));
                            esGoodsInfo.setEsSortPrice();
                        }
                    });

                    IndexQuery iq = new IndexQuery();
                    iq.setId(esGoods.getId());
                    iq.setType(EsConstants.DOC_GOODS_TYPE);
                    iq.setObject(esGoods);
                    iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                    esGoodsQuery.add(iq);

                });
                if (CollectionUtils.isNotEmpty(esGoodsQuery)) {
                    elasticsearchTemplate.bulkIndex(esGoodsQuery);
                }
            }
        }
        return result.equals(goodsInfoIds.size()) ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * 更新企业购商品（平台端审核时）
     *
     * @param request
     * @return true:批量更新成功，false:批量更新失败（部分更新成功）
     */
    public Boolean modifyEnterpriseAuditStatus(EsGoodsInfoEnterpriseAuditRequest request) {
        Client client = elasticsearchTemplate.getClient();
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        List<String> goodsInfoIds = request.getGoodsInfoIds();
        queryRequest.setGoodsInfoIds(goodsInfoIds);
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        Integer result = 0;
        if (esGoodsInfoList != null) {
            Map<String, Object> esGoodsInfoMap;
            Map<String, Object> map;
            for (EsGoodsInfo esGoodsInfo : esGoodsInfoList) {
                esGoodsInfoMap = new HashMap<>(4);
                map = new HashMap<>(4);
                map.put("enterPriseGoodsAuditReason",
                        request.getEnterPriseAuditStatus().equals(EnterpriseAuditState.NOT_PASS) ? request.getEnterPriseGoodsAuditReason() : "");
                map.put("enterPriseAuditStatus", request.getEnterPriseAuditStatus().toValue());
                if (EnterpriseAuditState.CHECKED.equals(request.getEnterPriseAuditStatus())) {
                    map.put("esSortPrice", String.valueOf(esGoodsInfo.getGoodsInfo().getEnterPrisePrice()));
                }
                esGoodsInfoMap.put("goodsInfo", map);
                client.prepareUpdate()
                        .setType(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setIndex(EsConstants.DOC_GOODS_INFO_TYPE)
                        .setId(esGoodsInfo.getId()).setDoc(esGoodsInfoMap).execute().actionGet();
                result++;
            }
            queryRequest.setQueryGoods(true);
            SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).build();
            Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoods.class);
            List<IndexQuery> esGoodsQuery = new ArrayList<>();
            if (esGoodsList != null) {
                esGoodsList.forEach(esGoods -> {
                    esGoods.getGoodsInfos().forEach(esGoodsInfo -> {
                        Boolean aBoolean =
                                goodsInfoIds.stream().anyMatch(goodsInfoId -> goodsInfoId.equals(esGoodsInfo.getGoodsInfoId()));
                        if (aBoolean) {
                            esGoodsInfo.setEnterPriseAuditStatus(request.getEnterPriseAuditStatus().toValue());
                            esGoodsInfo.setEnterPriseGoodsAuditReason(request.getEnterPriseAuditStatus().equals(EnterpriseAuditState.NOT_PASS)
                                    ? request.getEnterPriseGoodsAuditReason() : "");
                            esGoodsInfo.setEsSortPrice();
                        }
                    });

                    IndexQuery iq = new IndexQuery();
                    iq.setId(esGoods.getId());
                    iq.setObject(esGoods);
                    iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                    iq.setType(EsConstants.DOC_GOODS_TYPE);
                    esGoodsQuery.add(iq);

                });
                if (CollectionUtils.isNotEmpty(esGoodsQuery)) {
                    elasticsearchTemplate.bulkIndex(esGoodsQuery);
                }
            }
        }
        return result.equals(goodsInfoIds.size()) ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * 修改商品销量
     *
     * @param spuId    spuId
     * @param salesNum 销量
     * @return
     */
    public Long updateSalesNumBySpuId(String spuId, Long salesNum) {
        Long resCount = 0L;
        if (StringUtils.isNotEmpty(spuId) && Objects.nonNull(salesNum)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.termQuery("goodsInfo.goodsId", spuId))
                    //修改操作
                    .script(new Script("ctx._source.goodsInfo.goodsSalesNum=" + salesNum));
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.termQuery("_id", spuId))
                    //修改操作
                    .script(new Script("ctx._source.goodsSalesNum=" + salesNum));
            BulkByScrollResponse response1 = updateByQuery.get();
            resCount += response1.getUpdated();
        }
        return resCount;
    }

    /**
     * 修改商品排序号
     *
     * @param spuId  spuId
     * @param sortNo 排序号
     * @return
     */
    public Long updateSortNoBySpuId(String spuId, Long sortNo) {
        Long resCount = 0L;
        if (StringUtils.isNotEmpty(spuId) && Objects.nonNull(sortNo)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.termQuery("goodsInfo.goodsId", spuId))
                    //修改操作
                    .script(new Script("ctx._source.sortNo=" + sortNo));
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.termQuery("_id", spuId))
                    //修改操作
                    .script(new Script("ctx._source.sortNo=" + sortNo));
            BulkByScrollResponse response1 = updateByQuery.get();
            resCount += response1.getUpdated();
        }
        return resCount;
    }

    /**
     * 渠道设置未配置或停用，不显示linkedMall商品
     *
     * @param queryRequest 请求
     */
    private void filterLinkedMallShow(EsGoodsInfoQueryRequest queryRequest) {
        ThirdPlatformConfigResponse config = thirdPlatformConfigQueryProvider.get(
                ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build()).getContext();
        if (Objects.isNull(config) || Constants.no.equals(config.getStatus())) {
            queryRequest.setNotShowLinkedMallFlag(Boolean.TRUE);
        }
    }

    /**
     * 修改商品价格
     *
     * @param request 调价参数
     * @return
     */
    public void adjustPrice(EsGoodsInfoAdjustPriceRequest request) {

        //根据goodsInfoIds获取goodsIds
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder()
                .goodsInfoIds(request.getGoodsInfoIds()).build()).getContext().getGoodsInfos();
        List<String> goodsIds = goodsInfos.stream().map(GoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());

        //查询SPU
        List<GoodsVO> goodsVOList = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(goodsIds).build()).getContext().getGoodsVOList();
        Map<String, GoodsVO> goodsVOMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity()));
        //查询指定SPU下的所有SKU
        goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().goodsIds(goodsIds).build()).getContext().getGoodsInfos();
        Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfos.stream()
                .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
        List<String> goodsInfoIds = new ArrayList<>(goodsInfoVOMap.keySet());

        Map<String, List<GoodsLevelPriceNest>> levelPriceMap = new HashMap<>();
        Map<String, List<GoodsIntervalPriceVO>> intervalPriceMap = new HashMap<>();

        //区间价Map
        intervalPriceMap.putAll(getIntervalPriceMapBySkuId(goodsInfoIds));
        //等级价Map
        levelPriceMap.putAll(goodsLevelPriceQueryProvider.listBySkuIds(new GoodsLevelPriceBySkuIdsRequest(goodsInfoIds, PriceType.SKU))
                .getContext().getGoodsLevelPriceList()
                .stream().map(price -> KsBeanUtil.convert(price, GoodsLevelPriceNest.class))
                .collect(Collectors.groupingBy(GoodsLevelPriceNest::getGoodsInfoId)));

        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        //sku
        queryRequest.setGoodsIds(goodsIds);
        queryRequest.setPageSize(goodsInfoIds.size());
        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).withPageable(queryRequest.getPageable()).build();
        Iterable<EsGoodsInfo> esGoodsInfoList = elasticsearchTemplate.queryForList(searchQuery, EsGoodsInfo.class);
        List<IndexQuery> esGoodsInfoQuery = new ArrayList<>();
        if (esGoodsInfoList != null) {
            esGoodsInfoList.forEach(esGoodsInfo -> {
                GoodsInfoVO goodsInfoVO = goodsInfoVOMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId());
                GoodsVO goodsVO = goodsVOMap.get(esGoodsInfo.getGoodsId());
                if (Objects.nonNull(goodsInfoVO)) {
                    esGoodsInfo.getGoodsInfo().setAloneFlag(goodsInfoVO.getAloneFlag());
                    esGoodsInfo.getGoodsInfo().setPriceType(goodsVO.getPriceType());
                    esGoodsInfo.getGoodsInfo().setSaleType(goodsInfoVO.getSaleType());
                    esGoodsInfo.getGoodsInfo().setMarketPrice(goodsInfoVO.getMarketPrice());
                    esGoodsInfo.getGoodsInfo().setSupplyPrice(goodsInfoVO.getSupplyPrice());

                    //区间价
                    if (CollectionUtils.isNotEmpty(intervalPriceMap.get(goodsInfoVO.getGoodsInfoId()))) {
                        List<BigDecimal> prices = intervalPriceMap.get(goodsInfoVO.getGoodsInfoId()).stream().map(GoodsIntervalPriceVO::getPrice).filter(Objects::nonNull).collect(Collectors.toList());
                        esGoodsInfo.getGoodsInfo().setIntervalMinPrice(prices.stream().filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(goodsInfoVO.getMarketPrice()));
                        esGoodsInfo.getGoodsInfo().setIntervalMaxPrice(prices.stream().filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(goodsInfoVO.getMarketPrice()));
                    }
                    esGoodsInfo.setGoodsLevelPrices(levelPriceMap.get(goodsInfoVO.getGoodsInfoId()));
                }

                IndexQuery iq = new IndexQuery();
                iq.setId(esGoodsInfo.getId());
                iq.setIndexName(EsConstants.DOC_GOODS_INFO_TYPE);
                iq.setType(EsConstants.DOC_GOODS_INFO_TYPE);
                iq.setObject(esGoodsInfo);
                esGoodsInfoQuery.add(iq);
            });
            if (CollectionUtils.isNotEmpty(esGoodsInfoQuery)) {
                elasticsearchTemplate.bulkIndex(esGoodsInfoQuery);
            }
        }

        //spu
        queryRequest.setQueryGoods(true);
        SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).withPageable(queryRequest.getPageable()).build();
        Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoods.class);
        //spu
        List<IndexQuery> esGoodsQuery = new ArrayList<>();
        if (esGoodsList != null) {
            esGoodsList.forEach(esGoods -> {
                GoodsVO goodsVO = goodsVOMap.get(esGoods.getId());
                esGoods.getGoodsInfos().forEach(esGoodsInfo -> {
                    GoodsInfoVO goodsInfoVO = goodsInfoVOMap.get(esGoodsInfo.getGoodsInfoId());
                    if (Objects.nonNull(goodsInfoVO)) {
                        esGoodsInfo.setAloneFlag(goodsInfoVO.getAloneFlag());
                        esGoodsInfo.setPriceType(goodsVO.getPriceType());
                        esGoodsInfo.setSaleType(goodsInfoVO.getSaleType());
                        esGoodsInfo.setMarketPrice(goodsInfoVO.getMarketPrice());
                        esGoodsInfo.setSupplyPrice(goodsInfoVO.getSupplyPrice());
                        //区间价
                        if (CollectionUtils.isNotEmpty(intervalPriceMap.get(goodsInfoVO.getGoodsInfoId()))) {
                            List<BigDecimal> prices = intervalPriceMap.get(goodsInfoVO.getGoodsInfoId()).stream().map(GoodsIntervalPriceVO::getPrice).filter(Objects::nonNull).collect(Collectors.toList());
                            esGoodsInfo.setIntervalMinPrice(prices.stream().filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(goodsInfoVO.getMarketPrice()));
                            esGoodsInfo.setIntervalMaxPrice(prices.stream().filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(goodsInfoVO.getMarketPrice()));
                        }
                    }
                });

                IndexQuery iq = new IndexQuery();
                iq.setId(esGoods.getId());
                iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                iq.setType(EsConstants.DOC_GOODS_TYPE);
                iq.setObject(esGoods);
                esGoodsQuery.add(iq);
            });
            if (CollectionUtils.isNotEmpty(esGoodsQuery)) {
                elasticsearchTemplate.bulkIndex(esGoodsQuery);
            }
        }

        //代销商品
        if (PriceAdjustmentType.SUPPLY.equals(request.getType())) {
            Map<String, BigDecimal> supplyPriceMap = goodsInfoVOMap.values().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, GoodsInfoVO::getSupplyPrice));
            adjustConsignedGoods(supplyPriceMap);
        }
    }

    /**
     * 代销商品调价
     *
     * @param supplyPriceMap
     */
    public void adjustConsignedGoods(Map<String, BigDecimal> supplyPriceMap) {

        //遍历每一个供应商商品
        supplyPriceMap.forEach((key, value) -> {
            //查询该商品的代销
            GoodsInfoListByConditionResponse response = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().providerGoodsInfoIds(Lists.newArrayList(key)).build()).getContext();
            if (response != null && CollectionUtils.isNotEmpty(response.getGoodsInfos())) {
                List<String> goodsInfoIds = response.getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
                EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
                queryRequest.setGoodsInfoIds(goodsInfoIds);
                queryRequest.setPageSize(goodsInfoIds.size());
                SearchQuery infoSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).withPageable(queryRequest.getPageable()).build();
                List<EsGoodsInfo> esGoodsInfos = elasticsearchTemplate.queryForList(infoSearchQuery, EsGoodsInfo.class);
                List<IndexQuery> esGoodsInfoQuery = new ArrayList<>();
                if (esGoodsInfos != null) {
                    esGoodsInfos.forEach(esGoodsInfo -> {
                        esGoodsInfo.getGoodsInfo().setSupplyPrice(value);

                        IndexQuery iq = new IndexQuery();
                        iq.setId(esGoodsInfo.getId());
                        iq.setIndexName(EsConstants.DOC_GOODS_INFO_TYPE);
                        iq.setType(EsConstants.DOC_GOODS_INFO_TYPE);
                        iq.setObject(esGoodsInfo);
                        esGoodsInfoQuery.add(iq);
                    });

                    if (CollectionUtils.isNotEmpty(esGoodsInfoQuery)) {
                        elasticsearchTemplate.bulkIndex(esGoodsInfoQuery);
                    }
                }

                queryRequest.setQueryGoods(true);
                SearchQuery goodsSearchQuery = (new NativeSearchQueryBuilder()).withQuery(queryRequest.getWhereCriteria()).withPageable(queryRequest.getPageable()).build();
                Iterable<EsGoods> esGoodsList = elasticsearchTemplate.queryForList(goodsSearchQuery, EsGoods.class);
                List<IndexQuery> esGoodsQuery = new ArrayList<>();
                if (esGoodsList != null) {
                    esGoodsList.forEach(esGoods -> {
                        esGoods.getGoodsInfos().forEach(esGoodsInfo -> {
                            esGoodsInfo.setSupplyPrice(value);
                        });

                        IndexQuery iq = new IndexQuery();
                        iq.setId(esGoods.getId());
                        iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                        iq.setType(EsConstants.DOC_GOODS_TYPE);
                        iq.setObject(esGoods);
                        esGoodsQuery.add(iq);
                    });
                    if (CollectionUtils.isNotEmpty(esGoodsQuery)) {
                        elasticsearchTemplate.bulkIndex(esGoodsQuery);
                    }
                }
            }
        });

    }

    public void updateSaleType() {

    }

    /**
     * 根据skuId列表查询商品
     * @param request skuIds
     * @return
     */
    public EsGoodsInfoListResponse listByIds(EsGoodsInfoListRequest request) {
        EsGoodsInfoListResponse response = new EsGoodsInfoListResponse();
        List<EsGoodsInfo> esGoodsInfoVOS = elasticsearchTemplate.queryForList(request.getSearchCriteria(), EsGoodsInfo.class);
        response.setGoodsInfos(KsBeanUtil.convert(esGoodsInfoVOS, EsGoodsInfoVO.class));
        return response;
    }
}
