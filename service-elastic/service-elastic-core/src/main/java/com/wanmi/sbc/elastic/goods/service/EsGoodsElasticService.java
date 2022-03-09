package com.wanmi.sbc.elastic.goods.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.goods.model.root.EsCateBrand;
import com.wanmi.sbc.elastic.goods.model.root.EsGoods;
import com.wanmi.sbc.elastic.goods.model.root.EsGoodsInfo;
import com.wanmi.sbc.elastic.goods.model.root.GoodsCustomerPriceNest;
import com.wanmi.sbc.elastic.goods.model.root.GoodsExtProps;
import com.wanmi.sbc.elastic.goods.model.root.GoodsInfoNest;
import com.wanmi.sbc.elastic.goods.model.root.GoodsLabelNest;
import com.wanmi.sbc.elastic.goods.model.root.GoodsLevelPriceNest;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsCustomerPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateGoodsRelaQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsPageByConditionRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsPropDetailRelByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsCountByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoFillEnterpriseRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsCustomerPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceListBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateGoodsRelaListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifySimpleProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByIdsResponse;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsPropDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsPropVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingListByStoreIdsRequest;
import com.wanmi.sbc.marketing.bean.vo.DistributionStoreSettingVO;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ES商品信息数据源操作
 * Created by daiyitian on 2017/4/21.
 */
@Service
public class EsGoodsElasticService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
    private GoodsLevelPriceQueryProvider goodsLevelPriceQueryProvider;

    @Autowired
    private GoodsCustomerPriceQueryProvider goodsCustomerPriceQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private EsCateBrandService esCateBrandService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private StoreCateGoodsRelaQueryProvider storeCateGoodsRelaQueryProvider;

    @Autowired
    private DistributionSettingQueryProvider distributionSettingQueryProvider;

    @Autowired
    private EsGoodsLabelService esGoodsLabelService;

    @Autowired
    private EnterpriseGoodsInfoProvider enterpriseGoodsInfoProvider;

    @Autowired
    private ClassifyProvider classifyProvider;

    @Autowired
    private AtmosphereProvider atmosphereProvider;

    /**
     * 初始化SPU持化于ES
     */
    public void initEsGoods(EsGoodsInfoRequest request) {
        boolean isClear = request.isClearEsIndex();
        boolean isMapping = false;
//        if (elasticsearchTemplate.indexExists(EsConstants.DOC_GOODS_TYPE)
//                || elasticsearchTemplate.indexExists(EsConstants.DOC_GOODS_INFO_TYPE)) {
//            if (isClear) {
//                logger.info("商品spu->删除索引");
//                elasticsearchTemplate.deleteIndex(EsConstants.DOC_GOODS_TYPE);
//                elasticsearchTemplate.deleteIndex(EsConstants.DOC_GOODS_INFO_TYPE);
//                isMapping = true;
//            }
//        } else { //主要考虑第一次新增商品，此时还没有索引的时候
//            isMapping = true;
//        }
//        if (isMapping) {
//            //重建商品索引
//            elasticsearchTemplate.getClient().admin().indices()
//                    .prepareCreate(EsConstants.DOC_GOODS_TYPE).execute().actionGet();
//            elasticsearchTemplate.putMapping(EsGoods.class);
//            elasticsearchTemplate.getClient().admin().indices()
//                    .prepareCreate(EsConstants.DOC_GOODS_INFO_TYPE).execute().actionGet();
//            elasticsearchTemplate.putMapping(EsGoodsInfo.class);
//        }
        if (request.getGoodsIds() == null) {
            request.setGoodsIds(new ArrayList<>());
        }
        if (StringUtils.isNotBlank(request.getGoodsId())) {
            request.getGoodsIds().add(request.getGoodsId());
        }
        if (CollectionUtils.isNotEmpty(request.getSkuIds())) {
            //批量查询所有SKU信息列表
            GoodsInfoListByConditionRequest infoQueryRequest = new GoodsInfoListByConditionRequest();
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            infoQueryRequest.setCompanyInfoId(request.getCompanyInfoId());
            infoQueryRequest.setGoodsInfoIds(request.getSkuIds());
            infoQueryRequest.setStoreId(request.getStoreId());
            infoQueryRequest.setGoodsIds(request.getGoodsIds());
            infoQueryRequest.setBrandIds(request.getBrandIds());
            List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(infoQueryRequest).getContext().getGoodsInfos();
            List<String> goodsIds = goodsInfos.stream().map(GoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(goodsIds)) {
                request.getGoodsIds().addAll(goodsIds);
            } else {
                return;
            }
        }
        //批量查询所有SPU信息列表
        GoodsCountByConditionRequest goodsCountQueryRequest = new GoodsCountByConditionRequest();
        goodsCountQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        goodsCountQueryRequest.setCompanyInfoId(request.getCompanyInfoId());
        goodsCountQueryRequest.setGoodsIds(request.getGoodsIds());
        goodsCountQueryRequest.setStoreId(request.getStoreId());
        goodsCountQueryRequest.setBrandIds(request.getBrandIds());
        goodsCountQueryRequest.setCreateTimeBegin(request.getCreateTimeBegin());
        goodsCountQueryRequest.setCreateTimeEnd(request.getCreateTimeEnd());
        Long totalCount = goodsQueryProvider.countByCondition(goodsCountQueryRequest).getContext().getCount();

        if (totalCount <= 0) {
            return;
        }

        logger.info("商品所有索引开始");
        long startTime = System.currentTimeMillis();

        /**
         * 一个ID因采用uuid有32位字符串，目前mysql的SQL语句最大默认限制1M，通过mysql的配置文件（my.ini）中的max_allowed_packet来调整
         * 每批查询2000个GoodsID，根据jvm内存、服务请求超时时间来综合考虑调整。
         */
        Integer pageSize = 2000;
        if (request.getPageSize() != null) {
            pageSize = request.getPageSize();
        }

        long pageCount = 0L;
        long m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        Map<Long, StoreVO> storeMap = new HashMap<>();
        Map<Long, StoreVO> providerStoreMap = new HashMap<>();
        Map<String, GoodsInfoVO> providerGoodsInfoVOMap = new HashMap<>();
        Map<String, GoodsVO> providerGoodsVOMap = new HashMap<>();
        Map<String, DefaultFlag> distributionStoreSettingMap = new HashMap<>();
        Map<String, List<GoodsLevelPriceNest>> levelPriceMap = new HashMap<>();
        Map<String, List<GoodsCustomerPriceNest>> customerPriceMap = new HashMap<>();
        Map<String, List<GoodsIntervalPriceVO>> intervalPriceMap = new HashMap<>();
        Map<String, String> goodsInfoSpecDetailMap = new HashMap<>();
        Map<String, List<GoodsPropDetailRelVO>> goodsPropDetailMap = new HashMap<>();
//        Map<String, List<Long>> storeCateGoodsMap = new HashMap<>();

        Map<Long, GoodsCateVO> goodsCateMap = goodsCateQueryProvider.listByCondition(new GoodsCateListByConditionRequest())
                .getContext().getGoodsCateVOList().stream().collect(Collectors.toMap(GoodsCateVO::getCateId, goodsCate -> goodsCate));
        Map<Long, GoodsLabelNest> labelMap = esGoodsLabelService.getLabelMap();
        GoodsPageByConditionRequest goodsPageRequest = new GoodsPageByConditionRequest();
        KsBeanUtil.copyPropertiesThird(goodsCountQueryRequest, goodsPageRequest);
        goodsPageRequest.setDelFlag(DeleteFlag.NO.toValue());
        goodsPageRequest.setGoodsIds(goodsCountQueryRequest.getGoodsIds());

        //查询氛围
        AtmosphereQueryRequest atmosRequest = new AtmosphereQueryRequest();
        atmosRequest.setPageNum(0);
        atmosRequest.setPageSize(1000);

        atmosRequest.setStartTime(LocalDateTime.now());
        atmosRequest.setEndTime(LocalDateTime.now());
        atmosRequest.setSortColumn("id");
        atmosRequest.setSortRole("desc");


        int errorThrow = 0;//满10次，退出循环往上抛异常
        int pageIndex = 0; //开始位置
        if (request.getPageIndex() != null) {
            pageIndex = request.getPageIndex();
        }
        for (int i = pageIndex; i < pageCount; i++) {
            try {
                goodsPageRequest.setPageNum(i);
                goodsPageRequest.setPageSize(pageSize);
                List<GoodsVO> goodsList = goodsQueryProvider.pageByCondition(goodsPageRequest).getContext().getGoodsPage().getContent();
                if (CollectionUtils.isNotEmpty(goodsList)) {
                    List<String> goodsIds = goodsList.stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
                    GoodsInfoListByConditionRequest infoQueryRequest = new GoodsInfoListByConditionRequest();
                    infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
                    infoQueryRequest.setGoodsIds(goodsIds);
                    infoQueryRequest.setShowPointFlag(true);
                    List<GoodsInfoVO> goodsinfos = goodsInfoQueryProvider.listByCondition(infoQueryRequest).getContext().getGoodsInfos();
                    //设置企业购-最高价,最低价
                    GoodsInfoFillEnterpriseRequest fillRequest = new GoodsInfoFillEnterpriseRequest();
                    fillRequest.setGoodsInfos(goodsinfos);
                    goodsinfos = enterpriseGoodsInfoProvider.fillEnterpriseMinMaxPrice(fillRequest).getContext().getGoodsInfos();
                    if (CollectionUtils.isEmpty(goodsinfos)) {
                        continue;
                    }
                    List<String> skuIds = goodsinfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
                    atmosRequest.setSkuId(skuIds);
                    List<String> providerGoodsInfoIds = goodsinfos.stream().map(GoodsInfoVO::getProviderGoodsInfoId).collect(Collectors.toList());
                    providerGoodsInfoIds = providerGoodsInfoIds.stream()
                            .filter(v -> !providerGoodsInfoVOMap.containsKey(v)) //仅提取不存在map的
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(providerGoodsInfoIds)) {
                        GoodsInfoListByConditionRequest providerInfoQueryRequest = new GoodsInfoListByConditionRequest();
                        providerInfoQueryRequest.setGoodsInfoIds(providerGoodsInfoIds);
                        GoodsInfoListByIdsResponse goodsInfoListByIdsResponse = goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(providerGoodsInfoIds).build()).getContext();
                        if (goodsInfoListByIdsResponse != null && CollectionUtils.isNotEmpty(goodsInfoListByIdsResponse.getGoodsInfos())) {
                            List<GoodsInfoVO> providerGoodsinfos = goodsInfoListByIdsResponse.getGoodsInfos();
                            //供应商map
                            providerGoodsInfoVOMap.putAll(providerGoodsinfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g)));
                        }
                    }

                    List<Long> brandIds = goodsList.stream().filter(s -> Objects.nonNull(s.getBrandId())).map(GoodsVO::getBrandId).collect(Collectors.toList());
                    Map<Long, GoodsBrandVO> goodsBrandMap = new HashMap<>();
                    if (CollectionUtils.isNotEmpty(brandIds)) {
                        goodsBrandMap.putAll(goodsBrandQueryProvider.list(GoodsBrandListRequest.builder().brandIds(brandIds).build())
                                .getContext().getGoodsBrandVOList().stream().collect(Collectors.toMap(GoodsBrandVO::getBrandId,
                                        goodsBrand -> goodsBrand)));
                    }

                    List<String> providerGoodsIds = goodsList.stream().map(GoodsVO::getProviderGoodsId).collect(Collectors.toList());
                    providerGoodsIds = providerGoodsIds.stream()
                            .filter(v -> !providerGoodsVOMap.containsKey(v)) //仅提取不存在map的
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(providerGoodsIds)) {
                        GoodsListByIdsResponse providerGoodsListByIdsResponse = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(providerGoodsIds).build()).getContext();
                        if (providerGoodsListByIdsResponse != null && CollectionUtils.isNotEmpty(providerGoodsListByIdsResponse.getGoodsVOList())) {
                            List<GoodsVO> providerGoods = providerGoodsListByIdsResponse.getGoodsVOList();
                            //供应商map
                            providerGoodsVOMap.putAll(providerGoods.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, g -> g)));
                        }
                    }
                    //区间价Map
                    intervalPriceMap.putAll(getIntervalPriceMapBySkuId(skuIds));
                    //等级价Map
                    levelPriceMap.putAll(goodsLevelPriceQueryProvider.listBySkuIds(new GoodsLevelPriceBySkuIdsRequest(skuIds, PriceType.SKU))
                            .getContext().getGoodsLevelPriceList()
                            .stream().map(price -> KsBeanUtil.convert(price, GoodsLevelPriceNest.class))
                            .collect(Collectors.groupingBy(GoodsLevelPriceNest::getGoodsInfoId)));

                    //客户价Map
                    customerPriceMap.putAll(goodsCustomerPriceQueryProvider.listBySkuIds(new GoodsCustomerPriceBySkuIdsRequest(skuIds))
                            .getContext().getGoodsCustomerPriceVOList().stream()
                            .map(price -> KsBeanUtil.convert(price, GoodsCustomerPriceNest.class))
                            .collect(Collectors.groupingBy(GoodsCustomerPriceNest::getGoodsInfoId)));

                    //规格值Map
                    goodsInfoSpecDetailMap.putAll(goodsInfoSpecDetailRelQueryProvider.listBySkuIds(new GoodsInfoSpecDetailRelBySkuIdsRequest(skuIds))
                            .getContext().getGoodsInfoSpecDetailRelVOList().stream()
                            .filter(v -> StringUtils.isNotBlank(v.getDetailName()))
                            .collect(Collectors.toMap(GoodsInfoSpecDetailRelVO::getGoodsInfoId, GoodsInfoSpecDetailRelVO::getDetailName, (a, b) -> a.concat(" ").concat(b))));

                    //属性值Map
                    goodsPropDetailMap.putAll(getPropDetailRelList(goodsIds));

                    //商品店铺分类Map
//                    storeCateGoodsMap.putAll(storeCateGoodsRelaQueryProvider.listByGoodsIds(new StoreCateGoodsRelaListByGoodsIdsRequest(goodsIds)).getContext().getStoreCateGoodsRelaVOList().stream()
//                            .collect(Collectors.groupingBy(StoreCateGoodsRelaVO::getGoodsId, Collectors.mapping(StoreCateGoodsRelaVO::getStoreCateId, Collectors.toList()))));

                    Map<String, List<ClassifySimpleProviderResponse>> groupedClassify = classifyProvider.searchGroupedClassifyByGoodsId(goodsIds).getContext();

                    List<Long> storeIds = goodsList.stream().map(GoodsVO::getStoreId).filter(Objects::nonNull)
                            .filter(v -> !storeMap.containsKey(v)) //仅提取不存在map的store
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(storeIds)) {
                        storeMap.putAll(storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest(storeIds))
                                .getContext().getStoreVOList().stream()
                                .collect(Collectors.toMap(StoreVO::getStoreId, store -> store)));
                        List<String> stringList = storeIds.stream().distinct().map(String::valueOf).collect(Collectors.toList());
                        distributionStoreSettingMap.putAll(distributionSettingQueryProvider.listByStoreIds(new DistributionStoreSettingListByStoreIdsRequest(stringList)).getContext().getList().stream().collect(Collectors.toMap(DistributionStoreSettingVO::getStoreId, DistributionStoreSettingVO::getOpenFlag)));
                    }
                    List<Long> providerStoreIds = goodsList.stream().map(GoodsVO::getProviderId).filter(Objects::nonNull)
                            .filter(v -> !providerStoreMap.containsKey(v)) //仅提取不存在map的store
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(providerStoreIds)) {
                        providerStoreMap.putAll(storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest(providerStoreIds))
                                .getContext().getStoreVOList().stream()
                                .collect(Collectors.toMap(StoreVO::getStoreId, store -> store)));
                    }

                    //氛围
                    BaseResponse<MicroServicePage<AtmosphereDTO>> atmosResponse = atmosphereProvider.page(atmosRequest);

                    //遍历SKU，填充SPU、图片
                    List<IndexQuery> esGoodsList = new ArrayList<>();
                    List<IndexQuery> esSkuList = new ArrayList<>();
                    List<GoodsInfoVO> finalGoodsInfos = goodsinfos;
                    for (GoodsVO goods : goodsList) {
                        EsGoods esGoods = new EsGoods();
                        if (CollectionUtils.isEmpty(goods.getGoodsChannelTypeSet())) {
                            logger.error("商品id {} 商品No:{} 商品名称:{} 渠道为空，不刷新到es中", goods.getGoodsId(), goods.getGoodsNo(), goods.getGoodsName());
                            continue;
                        }
                        esGoods.setGoodsChannelTypeList(goods.getGoodsChannelTypeSet().stream().map(Integer::valueOf).collect(Collectors.toList()));
                        esGoods.setGoodsUnBackImg(goods.getGoodsUnBackImg());
                        esGoods.setCpsSpecial(goods.getCpsSpecial());
                        esGoods.setAnchorPushs(goods.getAnchorPushs());
                        if (StringUtils.isNotBlank(goods.getAnchorPushs()) && goods.getAnchorPushs().contains("1")) {
                            esGoods.setFdjd(1);
                        }
                        esGoods.setId(goods.getGoodsId());
                        esGoods.setVendibilityStatus(buildGoodsVendibility(goods, providerStoreMap, providerGoodsVOMap));
                        esGoods.setAddedTime(goods.getAddedTime());
                        esGoods.setAddedTimeNew(goods.getAddedTime());
                        esGoods.setLowGoodsName(StringUtils.lowerCase(goods.getGoodsName()));
                        esGoods.setPinyinGoodsName(esGoods.getLowGoodsName());
                        esGoods.setGoodsUnit(goods.getGoodsUnit());
                        esGoods.setLinePrice(goods.getLinePrice());
                        esGoods.setAuditStatus(goods.getAuditStatus().toValue());
                        esGoods.setSortNo(goods.getSortNo());
                        esGoods.setGoodsInfos(new ArrayList<>());
                        esGoods.setThirdPlatformType(goods.getThirdPlatformType());
                        esGoods.setStoreId(goods.getStoreId());
                        esGoods.setCompanyInfoId(goods.getCompanyInfoId());
                        esGoods.setProviderId(goods.getProviderId());
                        esGoods.setRealGoodsSalesNum(goods.getGoodsSalesNum());
                        esGoods.setGoodsNo(goods.getGoodsNo());
                        esGoods.setAddedFlag(goods.getAddedFlag());
                        esGoods.setGoodsSource(goods.getGoodsSource());
                        esGoods.setGoodsType(goods.getGoodsType());
                        esGoods.setStock(goods.getStock());
                        esGoods.setCreateTime(goods.getCreateTime());
                        esGoods.setGoodsName(goods.getGoodsName());
                        esGoods.setProviderGoodsId(goods.getProviderGoodsId());
                        //填充商品销量
                        Long shamSalesNum = goods.getShamSalesNum() == null ? 0 : goods.getShamSalesNum();
                        esGoods.setGoodsSalesNum(goods.getGoodsSalesNum() == null ? shamSalesNum : goods.getGoodsSalesNum() + shamSalesNum);
                        esGoods.setGoodsSalesNumNew(goods.getGoodsSalesNum() == null ? shamSalesNum : goods.getGoodsSalesNum() + shamSalesNum);

                        //填充商品收藏量
                        esGoods.setGoodsCollectNum(goods.getGoodsCollectNum() == null ? 0 : goods.getGoodsCollectNum());
                        //填充商品评论数
                        esGoods.setGoodsEvaluateNum(goods.getGoodsEvaluateNum() == null ? 0 : goods.getGoodsEvaluateNum());
                        //填充商品好评数
                        esGoods.setGoodsFavorableCommentNum(goods.getGoodsFavorableCommentNum() == null ? 0 :
                                goods.getGoodsFavorableCommentNum());
                        //填充好评率数据
                        Long goodsFeedbackRate = 0L;
                        if (Objects.nonNull(goods.getGoodsEvaluateNum()) && Objects.nonNull(goods.getGoodsFavorableCommentNum())
                                && goods.getGoodsEvaluateNum() > 0L) {
                            goodsFeedbackRate =
                                    (long) ((double) goods.getGoodsFavorableCommentNum() / (double) goods.getGoodsEvaluateNum() * 100);
                        }
                        esGoods.setGoodsFeedbackRate(goodsFeedbackRate);

                        //分配属性值
                        if (goodsPropDetailMap.containsKey(goods.getGoodsId())) {
                            List<GoodsPropDetailRelVO> goodsPropDetailRelVOS = goodsPropDetailMap.get(goods.getGoodsId());
                            Iterator<GoodsPropDetailRelVO> it = goodsPropDetailRelVOS.iterator();
                            GoodsExtProps goodsExtProps = new GoodsExtProps();
                            while (it.hasNext()) {
                                GoodsPropDetailRelVO rel = it.next();
                                if(Objects.equals(rel.getPropValue(),"null")){
                                    continue;
                                }
                                if("作者".equals(rel.getPropName())){
                                    goodsExtProps.setAuthor(rel.getPropValue());
                                    it.remove();
                                }else if("出版社".equals(rel.getPropName())){
                                    goodsExtProps.setPublisher(rel.getPropValue());
                                    it.remove();
                                }else if("定价".equals(rel.getPropName()) && StringUtils.isNotEmpty(rel.getPropValue())){
                                    goodsExtProps.setPrice(Double.parseDouble(rel.getPropValue()));
                                    it.remove();
                                }else if("评分".equals(rel.getPropName()) && StringUtils.isNotEmpty(rel.getPropValue())){
                                    goodsExtProps.setScore(Double.parseDouble(rel.getPropValue()));
                                    it.remove();
                                }else if("ISBN".equals(rel.getPropName())){
                                    goodsExtProps.setIsbn(rel.getPropValue());
                                    it.remove();
                                }
                            }
                            esGoods.setGoodsExtProps(goodsExtProps);
//                            esGoods.setPropDetailNesteds(esProps);
//                            esGoods.setPropDetailIds(goodsPropDetailMap.get(goods.getGoodsId()).stream().distinct().collect(Collectors.toList()));

                        }
                        //设置spu的分类和品牌
                        GoodsCateVO goodsCate = goodsCateMap.get(goods.getCateId());
                        GoodsBrandVO goodsBrand = new GoodsBrandVO();
                        goodsBrand.setBrandId(0L);
                        if (goods.getBrandId() != null) {
                            goodsBrand = goodsBrandMap.get(goods.getBrandId());
                        }
                        EsCateBrand esCateBrand = esCateBrandService.putEsCateBrand(goodsCate, goodsBrand);
                        if (Objects.nonNull(esCateBrand.getGoodsBrand())) {
                            esGoods.setGoodsBrand(esCateBrand.getGoodsBrand());
                        }

                        if (Objects.nonNull(esCateBrand.getGoodsCate())) {
                            esGoods.setGoodsCate(esCateBrand.getGoodsCate());
                        }
                        //填充签约有效期时间
                        if (MapUtils.isNotEmpty(storeMap) && storeMap.containsKey(goods.getStoreId())) {
                            StoreVO store = storeMap.get(goods.getStoreId());
                            esGoods.setContractStartDate(store.getContractStartDate());
                            esGoods.setContractEndDate(store.getContractEndDate());
                            esGoods.setStoreState(store.getStoreState().toValue());
                            //店铺名称和商家名称
                            esGoods.setStoreName(store.getStoreName());
                            esGoods.setSupplierName(store.getSupplierName());
                        }

                        //填充供应商名称
                        if (Objects.nonNull(esGoods.getProviderId()) && providerStoreMap.containsKey(esGoods.getProviderId())) {
                            esGoods.setProviderName(providerStoreMap.get(goods.getProviderId()).getSupplierName());
                        }

                        //获取店铺等级
//                        if (storeCateGoodsMap.containsKey(goods.getGoodsId())) {
//                            esGoods.setStoreCateIds(storeCateGoodsMap.get(goods.getGoodsId()));
//                        }
                        if(groupedClassify != null && groupedClassify.containsKey(goods.getGoodsId())){
                            esGoods.setClassify(groupedClassify.get(goods.getGoodsId()));
                        }

                        if (MapUtils.isNotEmpty(distributionStoreSettingMap) && distributionStoreSettingMap.containsKey(goods.getStoreId().toString())) {
                            esGoods.setDistributionGoodsStatus(distributionStoreSettingMap.get(goods.getStoreId().toString()) == DefaultFlag.NO ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
                        }

                        //获取商品标签
                        esGoods.setGoodsLabelList(Collections.emptyList());
                        if (StringUtils.isNotBlank(goods.getLabelIdStr()) && MapUtils.isNotEmpty(labelMap)) {
                            String[] labelIds = goods.getLabelIdStr().split(",");
                            esGoods.setGoodsLabelList(Arrays.stream(labelIds).map(NumberUtils::toLong)
                                    .filter(labelMap::containsKey).map(labelMap::get).collect(Collectors.toList()));
                        }

                        Map<String, GoodsInfoNest> firstSkuBySpuId = new HashMap<>();
                        finalGoodsInfos.stream().filter(goodsInfoVO -> goods.getGoodsId().equals(goodsInfoVO.getGoodsId()))
                                .forEach(goodsInfoVO -> {
                                    GoodsInfoNest goodsInfoNest = KsBeanUtil.convert(goodsInfoVO, GoodsInfoNest.class);
                                    goodsInfoNest.setCateId(goods.getCateId());
                                    goodsInfoNest.setBrandId(goods.getBrandId());
                                    goodsInfoNest.setPriceType(goods.getPriceType());
                                    goodsInfoNest.setCompanyType(goods.getCompanyType());
                                    goodsInfoNest.setGoodsCubage(goods.getGoodsCubage());
                                    //企业购商品信息
                                    goodsInfoNest.setEnterPriseAuditStatus(goodsInfoVO.getEnterPriseAuditState() != null ?
                                            goodsInfoVO.getEnterPriseAuditState().toValue() : EnterpriseAuditState.INIT.toValue());
                                    goodsInfoNest.setEnterPriseMinPrice(goodsInfoVO.getEnterpriseMinPrice());
                                    goodsInfoNest.setEnterPriseMaxPrice(goodsInfoVO.getEnterpriseMaxPrice());
                                    goodsInfoNest.setEnterPrisePrice(goodsInfoVO.getEnterPrisePrice());
                                    goodsInfoNest.setEnterPriseGoodsAuditReason(goodsInfoVO.getEnterPriseGoodsAuditReason());
                                    goodsInfoNest.setEnterpriseCustomerFlag(goodsInfoVO.getEnterpriseCustomerFlag());
                                    goodsInfoNest.setEnterpriseDiscountFlag(goodsInfoVO.getEnterpriseDiscountFlag());
                                    goodsInfoNest.setEnterprisePriceType(goodsInfoVO.getEnterprisePriceType());

                                    goodsInfoNest.setEsSortPrice();
                                    goodsInfoNest.setGoodsStatus(GoodsStatus.OK);
                                    goodsInfoNest.setGoodsSalesNum(esGoods.getGoodsSalesNum());
                                    goodsInfoNest.setGoodsCollectNum(esGoods.getGoodsCollectNum());
                                    goodsInfoNest.setGoodsEvaluateNum(esGoods.getGoodsEvaluateNum());
                                    goodsInfoNest.setGoodsFavorableCommentNum(esGoods.getGoodsFavorableCommentNum());
                                    goodsInfoNest.setGoodsFeedbackRate(esGoods.getGoodsFeedbackRate());
                                    goodsInfoNest.setVendibilityStatus(buildGoodsInfoVendibility(goodsInfoVO, providerStoreMap, providerGoodsInfoVOMap));
                                    goodsInfoNest.setProviderStatus(goodsInfoVO.getProviderStatus() == null ? Constants.yes : goodsInfoVO.getProviderStatus());
                                    goodsInfoNest.setSupplyPrice(goodsInfoVO.getSupplyPrice());
                                    goodsInfoNest.setStoreId(esGoods.getStoreId());
                                    goodsInfoNest.setStoreName(esGoods.getStoreName());
                                    goodsInfoNest.setGoodsType(esGoods.getGoodsType());
                                    goodsInfoNest.setCpsSpecial(esGoods.getCpsSpecial());
                                    goodsInfoNest.setAnchorPushs(goods.getAnchorPushs());
                                    if (StringUtils.isNotBlank(goodsInfoVO.getProviderGoodsInfoId())) {
                                        GoodsInfoVO providerGoodsInfoVO = providerGoodsInfoVOMap.get(goodsInfoVO.getProviderGoodsInfoId());
                                        if (providerGoodsInfoVO != null) {
                                            goodsInfoNest.setStock(providerGoodsInfoVO.getStock());
                                        }
                                    }
                                    //填充规格值
                                    if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                                        goodsInfoNest.setSpecText(goodsInfoSpecDetailMap.get(goodsInfoVO.getGoodsInfoId()));
                                    }

                                    //为空，则以商品主图
                                    if (StringUtils.isEmpty(goodsInfoNest.getGoodsInfoImg())) {
                                        goodsInfoNest.setGoodsInfoImg(goods.getGoodsImg());
                                    }

                                    //区间价
                                    if (CollectionUtils.isNotEmpty(intervalPriceMap.get(goodsInfoVO.getGoodsInfoId()))) {
                                        List<BigDecimal> prices = intervalPriceMap.get(goodsInfoVO.getGoodsInfoId()).stream().map(GoodsIntervalPriceVO::getPrice).filter(Objects::nonNull).collect(Collectors.toList());
                                        goodsInfoNest.setIntervalMinPrice(prices.stream().filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(goodsInfoVO.getMarketPrice()));
                                        goodsInfoNest.setIntervalMaxPrice(prices.stream().filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(goodsInfoVO.getMarketPrice()));
                                    }

                                    if (goodsInfoNest.getBuyPoint() == null) {
                                        goodsInfoNest.setBuyPoint(NumberUtils.LONG_ZERO);
                                    }

                                    if (!firstSkuBySpuId.containsKey(goodsInfoNest.getGoodsId())) {
                                        firstSkuBySpuId.put(goodsInfoNest.getGoodsId(), goodsInfoNest);
                                    }

                                    //SKU索引
                                    EsGoodsInfo esGoodsInfo = new EsGoodsInfo();
                                    esGoodsInfo.setId(goodsInfoNest.getGoodsInfoId());
                                    esGoodsInfo.setGoodsId(goodsInfoNest.getGoodsId());
                                    if (Objects.nonNull(esCateBrand.getGoodsCate())) {
                                        esGoodsInfo.setGoodsCate(esCateBrand.getGoodsCate());
                                    }
                                    if (Objects.nonNull(esCateBrand.getGoodsBrand())) {
                                        esGoodsInfo.setGoodsBrand(esCateBrand.getGoodsBrand());
                                    }
                                    //设置氛围
                                    if(atmosResponse!= null && atmosResponse.getContext()!=null && CollectionUtils.isNotEmpty(atmosResponse.getContext().getContent())){
                                        Optional<AtmosphereDTO> atmos = atmosResponse.getContext().getContent().stream().filter(p->p.getSkuId().equals(goodsInfoNest.getGoodsInfoId())).findFirst();
                                        if(atmos.isPresent()){
                                            goodsInfoNest.setAtmosType(atmos.get().getAtmosType());
                                            goodsInfoNest.setStartTime(atmos.get().getStartTime());
                                            goodsInfoNest.setEndTime(atmos.get().getEndTime());
                                            goodsInfoNest.setImageUrl(atmos.get().getImageUrl());
                                            goodsInfoNest.setElementOne(atmos.get().getElementOne());
                                            goodsInfoNest.setElementTwo(atmos.get().getElementTwo());
                                            goodsInfoNest.setElementThree(atmos.get().getElementThree());
                                            goodsInfoNest.setElementFour(atmos.get().getElementFour());

                                        }
                                    }
                                    esGoodsInfo.setGoodsInfo(goodsInfoNest);
                                    esGoodsInfo.setAddedTime(goodsInfoNest.getAddedTime());
                                    esGoodsInfo.setGoodsLevelPrices(levelPriceMap.get(goodsInfoNest.getGoodsInfoId()));
                                    esGoodsInfo.setCustomerPrices(customerPriceMap.get(goodsInfoNest.getGoodsInfoId()));
                                    esGoodsInfo.setLowGoodsName(esGoods.getLowGoodsName());
                                    esGoodsInfo.setPinyinGoodsName(esGoods.getLowGoodsName());
                                    esGoodsInfo.setGoodsUnit(esGoods.getGoodsUnit());
                                    esGoodsInfo.setLinePrice(esGoods.getLinePrice());
                                    esGoodsInfo.setPropDetailIds(esGoods.getPropDetailIds());
                                    esGoodsInfo.setContractStartDate(esGoods.getContractStartDate());
                                    esGoodsInfo.setContractEndDate(esGoods.getContractEndDate());
                                    esGoodsInfo.setStoreState(esGoods.getStoreState());
                                    esGoodsInfo.setStoreCateIds(esGoods.getStoreCateIds());
                                    esGoodsInfo.setAuditStatus(esGoods.getAuditStatus());
                                    esGoodsInfo.setDistributionGoodsStatus(esGoods.getDistributionGoodsStatus());
                                    esGoodsInfo.setVendibilityStatus(buildGoodsInfoVendibility(goodsInfoVO, providerStoreMap, providerGoodsInfoVOMap));
                                    esGoodsInfo.setGoodsLabelList(esGoods.getGoodsLabelList());
                                    esGoodsInfo.setGoodsSource(esGoods.getGoodsSource());
                                    esGoodsInfo.setGoodsNo(esGoods.getGoodsNo());
                                    esGoodsInfo.setGoodsName(esGoods.getGoodsName());
                                    esGoods.getGoodsInfos().add(goodsInfoNest);
                                    IndexQuery iq = new IndexQuery();
                                    iq.setObject(esGoodsInfo);
                                    iq.setIndexName(EsConstants.DOC_GOODS_INFO_TYPE);
                                    iq.setType(EsConstants.DOC_GOODS_INFO_TYPE);
                                    esSkuList.add(iq);
                                });
                        //第一个SKU的信息
                        if (MapUtils.isNotEmpty(firstSkuBySpuId)) {
                            GoodsInfoNest goodsInfoNest = firstSkuBySpuId.get(esGoods.getId());
                            esGoods.setEsSortPrice(goodsInfoNest.getEsSortPrice());
                            esGoods.setSortPrice(goodsInfoNest.getEsSortPrice());
                            esGoods.setBuyPoint(goodsInfoNest.getBuyPoint());
//                        esGoods.setGoodsLevelPrices(levelPriceMap.getOrDefault(goodsInfoNest.getGoodsInfoId(), new ArrayList<>()));
//                        esGoods.setCustomerPrices(customerPriceMap.getOrDefault(goodsInfoNest.getGoodsInfoId(), new ArrayList<>()));
                        }
                        IndexQuery iq = new IndexQuery();
                        iq.setObject(esGoods);
                        iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                        iq.setType(EsConstants.DOC_GOODS_TYPE);
                        esGoodsList.add(iq);
                    }



                    //持久化商品
                    elasticsearchTemplate.bulkIndex(esGoodsList);
                    elasticsearchTemplate.refresh(EsGoods.class);

                    //持久化商品
                    elasticsearchTemplate.bulkIndex(esSkuList);
                    elasticsearchTemplate.refresh(EsGoodsInfo.class);

                    //清空缓存
                    intervalPriceMap.clear();
                    goodsInfoSpecDetailMap.clear();
                    goodsPropDetailMap.clear();
//                specMap.clear();
                    levelPriceMap.clear();
                    customerPriceMap.clear();
//                    storeCateGoodsMap.clear();
                }
            } catch (Exception e) {
                logger.error("初始化ES商品页码位置".concat(String.valueOf(i)).concat("，异常："), e);
                errorThrow++;
                if (errorThrow >= 10) {
                    throw new SbcRuntimeException("K-030407", new Object[]{i});
                }
                i--;
            } catch (Throwable t) {
                logger.error("初始化ES商品页码位置".concat(String.valueOf(i)).concat("，异常："), t);
                throw new SbcRuntimeException("K-030407", new Object[]{i});
            }
        }

        logger.info(String.format("商品所有索引结束->花费%s毫秒", (System.currentTimeMillis() - startTime)));
    }

    /**
     * 更新spu扩展属性
     * @param props
     */
    public void setExtPropForGoods(List<Object[]> props){
        List<UpdateQuery> updateQueries = new ArrayList<>();
        for (Object[] prop : props) {
            String goodsId = (String) prop[0];
            String author = (String) prop[1];
            String publisher = (String) prop[2];
            double price = (double) prop[3];
            double score = (double) prop[4];
            String isbn = (String) prop[5];

            Map<String, Object> params = new HashMap<>();
            Map<String, Object> wrapper = new HashMap<>();
            params.put("score", score);
            params.put("price", price);
            params.put("publisher", publisher);
            params.put("author", author);
            params.put("isbn", isbn);
            wrapper.put("goodsExtProps", params);

            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.doc(wrapper);
            UpdateQueryBuilder updateQueryBuilder = new UpdateQueryBuilder();
            UpdateQuery build = updateQueryBuilder.withId(goodsId).withUpdateRequest(updateRequest)
                    .withClass(EsGoods.class).build();
            updateQueries.add(build);
        }
        elasticsearchTemplate.bulkUpdate(updateQueries);
    }

    /**
     * 初始化SPU持化于ES
     */
    public void initEsGoods2() {
        EsGoodsInfoRequest request = EsGoodsInfoRequest.builder().build();
        if (request.getGoodsIds() == null) {
            request.setGoodsIds(new ArrayList<>());
        }

        if (StringUtils.isNotBlank(request.getGoodsId())) {
            request.getGoodsIds().add(request.getGoodsId());
        }

        if (CollectionUtils.isNotEmpty(request.getSkuIds())) {
            //批量查询所有SKU信息列表
            GoodsInfoListByConditionRequest infoQueryRequest = new GoodsInfoListByConditionRequest();
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());

            infoQueryRequest.setCompanyInfoId(request.getCompanyInfoId());
            infoQueryRequest.setGoodsInfoIds(request.getSkuIds());
            infoQueryRequest.setStoreId(request.getStoreId());
            infoQueryRequest.setGoodsIds(request.getGoodsIds());
            infoQueryRequest.setBrandIds(request.getBrandIds());
            List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(infoQueryRequest).getContext().getGoodsInfos();
            List<String> goodsIds = goodsInfos.stream().map(GoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(goodsIds)) {
                request.getGoodsIds().addAll(goodsIds);
            } else {
                return;
            }
        }
        //批量查询所有SPU信息列表
        GoodsCountByConditionRequest goodsCountQueryRequest = new GoodsCountByConditionRequest();
        goodsCountQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        goodsCountQueryRequest.setCompanyInfoId(request.getCompanyInfoId());
        goodsCountQueryRequest.setGoodsIds(request.getGoodsIds());
        goodsCountQueryRequest.setStoreId(request.getStoreId());
        goodsCountQueryRequest.setBrandIds(request.getBrandIds());
        goodsCountQueryRequest.setCreateTimeBegin(request.getCreateTimeBegin());
        goodsCountQueryRequest.setCreateTimeEnd(request.getCreateTimeEnd());
        Long totalCount = goodsQueryProvider.countByCondition(goodsCountQueryRequest).getContext().getCount();

        if (totalCount <= 0) {
            return;
        }

        logger.info("商品所有索引开始");
        long startTime = System.currentTimeMillis();

        /**
         * 一个ID因采用uuid有32位字符串，目前mysql的SQL语句最大默认限制1M，通过mysql的配置文件（my.ini）中的max_allowed_packet来调整
         * 每批查询2000个GoodsID，根据jvm内存、服务请求超时时间来综合考虑调整。
         */
        Integer pageSize = 2000;
        if (request.getPageSize() != null) {
            pageSize = request.getPageSize();
        }

        long pageCount = 0L;
        long m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        Map<Long, StoreVO> storeMap = new HashMap<>();
        Map<Long, StoreVO> providerStoreMap = new HashMap<>();
        Map<String, GoodsInfoVO> providerGoodsInfoVOMap = new HashMap<>();
        Map<String, GoodsVO> providerGoodsVOMap = new HashMap<>();
        Map<String, DefaultFlag> distributionStoreSettingMap = new HashMap<>();
        Map<String, List<GoodsLevelPriceNest>> levelPriceMap = new HashMap<>();
        Map<String, List<GoodsCustomerPriceNest>> customerPriceMap = new HashMap<>();
        Map<String, List<GoodsIntervalPriceVO>> intervalPriceMap = new HashMap<>();
        Map<String, String> goodsInfoSpecDetailMap = new HashMap<>();
//        Map<String, List<Long>> goodsPropDetailMap = new HashMap<>();
//        Map<String, List<Long>> storeCateGoodsMap = new HashMap<>();

        Map<Long, GoodsCateVO> goodsCateMap = goodsCateQueryProvider.listByCondition(new GoodsCateListByConditionRequest())
                .getContext().getGoodsCateVOList().stream().collect(Collectors.toMap(GoodsCateVO::getCateId, goodsCate -> goodsCate));

        Map<Long, GoodsLabelNest> labelMap = esGoodsLabelService.getLabelMap();

        GoodsPageByConditionRequest goodsPageRequest = new GoodsPageByConditionRequest();
        KsBeanUtil.copyPropertiesThird(goodsCountQueryRequest, goodsPageRequest);
        goodsPageRequest.setDelFlag(DeleteFlag.NO.toValue());
        goodsPageRequest.setGoodsIds(goodsCountQueryRequest.getGoodsIds());

        int errorThrow = 0;//满10次，退出循环往上抛异常
        int pageIndex = 0; //开始位置
        if (request.getPageIndex() != null) {
            pageIndex = request.getPageIndex();
        }
        for (int i = pageIndex; i < pageCount; i++) {
            try {
                goodsPageRequest.setPageNum(i);
                goodsPageRequest.setPageSize(pageSize);
                List<GoodsVO> goodsList = goodsQueryProvider.pageByCondition(goodsPageRequest).getContext().getGoodsPage().getContent();
                if (CollectionUtils.isNotEmpty(goodsList)) {
                    List<String> goodsIds = goodsList.stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
                    GoodsInfoListByConditionRequest infoQueryRequest = new GoodsInfoListByConditionRequest();
                    infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
                    infoQueryRequest.setGoodsIds(goodsIds);
                    infoQueryRequest.setShowPointFlag(true);
                    List<GoodsInfoVO> goodsinfos = goodsInfoQueryProvider.listByCondition(infoQueryRequest).getContext().getGoodsInfos();
                    //设置企业购-最高价,最低价
                    GoodsInfoFillEnterpriseRequest fillRequest = new GoodsInfoFillEnterpriseRequest();
                    fillRequest.setGoodsInfos(goodsinfos);
                    goodsinfos = enterpriseGoodsInfoProvider.fillEnterpriseMinMaxPrice(fillRequest).getContext().getGoodsInfos();
                    if (CollectionUtils.isEmpty(goodsinfos)) {
                        continue;
                    }
                    List<String> skuIds = goodsinfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());

                    List<String> providerGoodsInfoIds = goodsinfos.stream().map(GoodsInfoVO::getProviderGoodsInfoId).collect(Collectors.toList());
                    providerGoodsInfoIds = providerGoodsInfoIds.stream()
                            .filter(v -> !providerGoodsInfoVOMap.containsKey(v)) //仅提取不存在map的
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(providerGoodsInfoIds)) {
                        GoodsInfoListByConditionRequest providerInfoQueryRequest = new GoodsInfoListByConditionRequest();
                        providerInfoQueryRequest.setGoodsInfoIds(providerGoodsInfoIds);
                        GoodsInfoListByIdsResponse goodsInfoListByIdsResponse = goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(providerGoodsInfoIds).build()).getContext();
                        if (goodsInfoListByIdsResponse != null && CollectionUtils.isNotEmpty(goodsInfoListByIdsResponse.getGoodsInfos())) {
                            List<GoodsInfoVO> providerGoodsinfos = goodsInfoListByIdsResponse.getGoodsInfos();
                            //供应商map
                            providerGoodsInfoVOMap.putAll(providerGoodsinfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g)));
                        }
                    }

                    List<Long> brandIds = goodsList.stream().filter(s -> Objects.nonNull(s.getBrandId())).map(GoodsVO::getBrandId).collect(Collectors.toList());
                    Map<Long, GoodsBrandVO> goodsBrandMap = new HashMap<>();
                    if (CollectionUtils.isNotEmpty(brandIds)) {
                        goodsBrandMap.putAll(goodsBrandQueryProvider.list(GoodsBrandListRequest.builder().brandIds(brandIds).build())
                                .getContext().getGoodsBrandVOList().stream().collect(Collectors.toMap(GoodsBrandVO::getBrandId,
                                        goodsBrand -> goodsBrand)));
                    }

                    List<String> providerGoodsIds = goodsList.stream().map(GoodsVO::getProviderGoodsId).collect(Collectors.toList());
                    providerGoodsIds = providerGoodsIds.stream()
                            .filter(v -> !providerGoodsVOMap.containsKey(v)) //仅提取不存在map的
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(providerGoodsIds)) {
                        GoodsListByIdsResponse providerGoodsListByIdsResponse = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(providerGoodsIds).build()).getContext();
                        if (providerGoodsListByIdsResponse != null && CollectionUtils.isNotEmpty(providerGoodsListByIdsResponse.getGoodsVOList())) {
                            List<GoodsVO> providerGoods = providerGoodsListByIdsResponse.getGoodsVOList();
                            //供应商map
                            providerGoodsVOMap.putAll(providerGoods.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, g -> g)));
                        }
                    }

                    //区间价Map
                    intervalPriceMap.putAll(getIntervalPriceMapBySkuId(skuIds));

                    //等级价Map
                    levelPriceMap.putAll(goodsLevelPriceQueryProvider.listBySkuIds(new GoodsLevelPriceBySkuIdsRequest(skuIds, PriceType.SKU))
                            .getContext().getGoodsLevelPriceList()
                            .stream().map(price -> KsBeanUtil.convert(price, GoodsLevelPriceNest.class))
                            .collect(Collectors.groupingBy(GoodsLevelPriceNest::getGoodsInfoId)));

                    //客户价Map
                    customerPriceMap.putAll(goodsCustomerPriceQueryProvider.listBySkuIds(new GoodsCustomerPriceBySkuIdsRequest(skuIds))
                            .getContext().getGoodsCustomerPriceVOList().stream()
                            .map(price -> KsBeanUtil.convert(price, GoodsCustomerPriceNest.class))
                            .collect(Collectors.groupingBy(GoodsCustomerPriceNest::getGoodsInfoId)));


                    //规格值Map
                    goodsInfoSpecDetailMap.putAll(goodsInfoSpecDetailRelQueryProvider.listBySkuIds(new GoodsInfoSpecDetailRelBySkuIdsRequest(skuIds))
                            .getContext().getGoodsInfoSpecDetailRelVOList().stream()
                            .filter(v -> StringUtils.isNotBlank(v.getDetailName()))
                            .collect(Collectors.toMap(GoodsInfoSpecDetailRelVO::getGoodsInfoId, GoodsInfoSpecDetailRelVO::getDetailName, (a, b) -> a.concat(" ").concat(b))));

                    //属性值Map
//                    goodsPropDetailMap.putAll(getPropDetailRelList(goodsIds));

                    //商品店铺分类Map
//                    storeCateGoodsMap.putAll(storeCateGoodsRelaQueryProvider.listByGoodsIds(new StoreCateGoodsRelaListByGoodsIdsRequest(goodsIds)).getContext().getStoreCateGoodsRelaVOList().stream()
//                            .collect(Collectors.groupingBy(StoreCateGoodsRelaVO::getGoodsId, Collectors.mapping(StoreCateGoodsRelaVO::getStoreCateId, Collectors.toList()))));
                    Map<String, List<ClassifySimpleProviderResponse>> groupedClassify = classifyProvider.searchGroupedClassifyByGoodsId(goodsIds).getContext();

                    List<Long> storeIds = goodsList.stream().map(GoodsVO::getStoreId).filter(Objects::nonNull)
                            .filter(v -> !storeMap.containsKey(v)) //仅提取不存在map的store
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(storeIds)) {
                        storeMap.putAll(storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest(storeIds))
                                .getContext().getStoreVOList().stream()
                                .collect(Collectors.toMap(StoreVO::getStoreId, store -> store)));
                        List<String> stringList = storeIds.stream().distinct().map(String::valueOf).collect(Collectors.toList());
                        distributionStoreSettingMap.putAll(distributionSettingQueryProvider.listByStoreIds(new DistributionStoreSettingListByStoreIdsRequest(stringList)).getContext().getList().stream().collect(Collectors.toMap(DistributionStoreSettingVO::getStoreId, DistributionStoreSettingVO::getOpenFlag)));
                    }
                    List<Long> providerStoreIds = goodsList.stream().map(GoodsVO::getProviderId).filter(Objects::nonNull)
                            .filter(v -> !providerStoreMap.containsKey(v)) //仅提取不存在map的store
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(providerStoreIds)) {
                        providerStoreMap.putAll(storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest(providerStoreIds))
                                .getContext().getStoreVOList().stream()
                                .collect(Collectors.toMap(StoreVO::getStoreId, store -> store)));
                    }


                    //遍历SKU，填充SPU、图片
                    List<IndexQuery> esGoodsList = new ArrayList<>();
                    List<IndexQuery> esSkuList = new ArrayList<>();
                    List<GoodsInfoVO> finalGoodsInfos = goodsinfos;
                    goodsList.forEach(goods -> {
                        EsGoods esGoods = new EsGoods();
                        esGoods.setCpsSpecial(goods.getCpsSpecial());
                        esGoods.setId(goods.getGoodsId());
                        esGoods.setVendibilityStatus(buildGoodsVendibility(goods, providerStoreMap, providerGoodsVOMap));
                        esGoods.setAddedTime(goods.getAddedTime());
                        esGoods.setAddedTimeNew(goods.getAddedTime());
                        esGoods.setLowGoodsName(StringUtils.lowerCase(goods.getGoodsName()));
                        esGoods.setPinyinGoodsName(esGoods.getLowGoodsName());
                        esGoods.setGoodsUnit(goods.getGoodsUnit());
                        esGoods.setLinePrice(goods.getLinePrice());
                        esGoods.setAuditStatus(goods.getAuditStatus().toValue());
                        esGoods.setSortNo(goods.getSortNo());
                        esGoods.setGoodsInfos(new ArrayList<>());
                        esGoods.setThirdPlatformType(goods.getThirdPlatformType());
                        esGoods.setStoreId(goods.getStoreId());
                        esGoods.setCompanyInfoId(goods.getCompanyInfoId());
                        esGoods.setProviderId(goods.getProviderId());
                        esGoods.setRealGoodsSalesNum(goods.getGoodsSalesNum());
                        esGoods.setGoodsNo(goods.getGoodsNo());
                        esGoods.setAddedFlag(goods.getAddedFlag());
                        esGoods.setGoodsSource(goods.getGoodsSource());
                        esGoods.setGoodsType(goods.getGoodsType());
                        esGoods.setStock(goods.getStock());
                        esGoods.setCreateTime(goods.getCreateTime());
                        esGoods.setGoodsName(goods.getGoodsName());
                        esGoods.setProviderGoodsId(goods.getProviderGoodsId());
                        //填充商品销量
                        Long shamSalesNum = goods.getShamSalesNum() == null ? 0 : goods.getShamSalesNum();
                        esGoods.setGoodsSalesNum(goods.getGoodsSalesNum() == null ? shamSalesNum : goods.getGoodsSalesNum() + shamSalesNum);
                        esGoods.setGoodsSalesNumNew(goods.getGoodsSalesNum() == null ? shamSalesNum : goods.getGoodsSalesNum() + shamSalesNum);
                        //填充商品收藏量
                        esGoods.setGoodsCollectNum(goods.getGoodsCollectNum() == null ? 0 : goods.getGoodsCollectNum());
                        //填充商品评论数
                        esGoods.setGoodsEvaluateNum(goods.getGoodsEvaluateNum() == null ? 0 : goods.getGoodsEvaluateNum());
                        //填充商品好评数
                        esGoods.setGoodsFavorableCommentNum(goods.getGoodsFavorableCommentNum() == null ? 0 :
                                goods.getGoodsFavorableCommentNum());
                        //填充好评率数据
                        Long goodsFeedbackRate = 0L;
                        if (Objects.nonNull(goods.getGoodsEvaluateNum()) && Objects.nonNull(goods.getGoodsFavorableCommentNum())
                                && goods.getGoodsEvaluateNum() > 0L) {
                            goodsFeedbackRate =
                                    (long) ((double) goods.getGoodsFavorableCommentNum() / (double) goods.getGoodsEvaluateNum() * 100);
                        }
                        esGoods.setGoodsFeedbackRate(goodsFeedbackRate);

                        //分配属性值
//                        if (goodsPropDetailMap.containsKey(goods.getGoodsId())) {
//                            esGoods.setPropDetailIds(goodsPropDetailMap.get(goods.getGoodsId()).stream().distinct().collect(Collectors.toList()));
//                        }
                        //设置spu的分类和品牌
                        GoodsCateVO goodsCate = goodsCateMap.get(goods.getCateId());
                        GoodsBrandVO goodsBrand = new GoodsBrandVO();
                        goodsBrand.setBrandId(0L);
                        if (goods.getBrandId() != null) {
                            goodsBrand = goodsBrandMap.get(goods.getBrandId());
                        }
                        EsCateBrand esCateBrand = esCateBrandService.putEsCateBrand(goodsCate, goodsBrand);
                        if (Objects.nonNull(esCateBrand.getGoodsBrand())) {
                            esGoods.setGoodsBrand(esCateBrand.getGoodsBrand());
                        }

                        if (Objects.nonNull(esCateBrand.getGoodsCate())) {
                            esGoods.setGoodsCate(esCateBrand.getGoodsCate());
                        }
                        //填充签约有效期时间
                        if (MapUtils.isNotEmpty(storeMap) && storeMap.containsKey(goods.getStoreId())) {
                            StoreVO store = storeMap.get(goods.getStoreId());
                            esGoods.setContractStartDate(store.getContractStartDate());
                            esGoods.setContractEndDate(store.getContractEndDate());
                            esGoods.setStoreState(store.getStoreState().toValue());
                            //店铺名称和商家名称
                            esGoods.setStoreName(store.getStoreName());
                            esGoods.setSupplierName(store.getSupplierName());
                        }

                        //填充供应商名称
                        if (Objects.nonNull(esGoods.getProviderId()) && providerStoreMap.containsKey(esGoods.getProviderId())) {
                            esGoods.setProviderName(providerStoreMap.get(goods.getProviderId()).getSupplierName());
                        }

                        //获取店铺等级
//                        if (storeCateGoodsMap.containsKey(goods.getGoodsId())) {
//                            esGoods.setStoreCateIds(storeCateGoodsMap.get(goods.getGoodsId()));
//                        }
                        if(groupedClassify != null && groupedClassify.containsKey(goods.getGoodsId())){
                            esGoods.setClassify(groupedClassify.get(goods.getGoodsId()));
                        }

                        if (MapUtils.isNotEmpty(distributionStoreSettingMap) && distributionStoreSettingMap.containsKey(goods.getStoreId().toString())) {
                            esGoods.setDistributionGoodsStatus(distributionStoreSettingMap.get(goods.getStoreId().toString()) == DefaultFlag.NO ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
                        }

                        //获取商品标签
                        esGoods.setGoodsLabelList(Collections.emptyList());
                        if (StringUtils.isNotBlank(goods.getLabelIdStr()) && MapUtils.isNotEmpty(labelMap)) {
                            String[] labelIds = goods.getLabelIdStr().split(",");
                            esGoods.setGoodsLabelList(Arrays.stream(labelIds).map(NumberUtils::toLong)
                                    .filter(labelMap::containsKey).map(labelMap::get).collect(Collectors.toList()));
                        }

                        Map<String, GoodsInfoNest> firstSkuBySpuId = new HashMap<>();
                        finalGoodsInfos.stream().filter(goodsInfoVO -> goods.getGoodsId().equals(goodsInfoVO.getGoodsId()))
                                .forEach(goodsInfoVO -> {
                                    GoodsInfoNest goodsInfoNest = KsBeanUtil.convert(goodsInfoVO, GoodsInfoNest.class);
                                    goodsInfoNest.setCateId(goods.getCateId());
                                    goodsInfoNest.setBrandId(goods.getBrandId());
                                    goodsInfoNest.setPriceType(goods.getPriceType());
                                    goodsInfoNest.setCompanyType(goods.getCompanyType());
                                    goodsInfoNest.setGoodsCubage(goods.getGoodsCubage());
                                    //企业购商品信息
                                    goodsInfoNest.setEnterPriseAuditStatus(goodsInfoVO.getEnterPriseAuditState() != null ?
                                            goodsInfoVO.getEnterPriseAuditState().toValue() : EnterpriseAuditState.INIT.toValue());
                                    goodsInfoNest.setEnterPriseMinPrice(goodsInfoVO.getEnterpriseMinPrice());
                                    goodsInfoNest.setEnterPriseMaxPrice(goodsInfoVO.getEnterpriseMaxPrice());
                                    goodsInfoNest.setEnterPrisePrice(goodsInfoVO.getEnterPrisePrice());
                                    goodsInfoNest.setEnterPriseGoodsAuditReason(goodsInfoVO.getEnterPriseGoodsAuditReason());
                                    goodsInfoNest.setEnterpriseCustomerFlag(goodsInfoVO.getEnterpriseCustomerFlag());
                                    goodsInfoNest.setEnterpriseDiscountFlag(goodsInfoVO.getEnterpriseDiscountFlag());
                                    goodsInfoNest.setEnterprisePriceType(goodsInfoVO.getEnterprisePriceType());

                                    goodsInfoNest.setEsSortPrice();
                                    goodsInfoNest.setGoodsStatus(GoodsStatus.OK);
                                    goodsInfoNest.setGoodsSalesNum(esGoods.getGoodsSalesNum());
                                    goodsInfoNest.setGoodsCollectNum(esGoods.getGoodsCollectNum());
                                    goodsInfoNest.setGoodsEvaluateNum(esGoods.getGoodsEvaluateNum());
                                    goodsInfoNest.setGoodsFavorableCommentNum(esGoods.getGoodsFavorableCommentNum());
                                    goodsInfoNest.setGoodsFeedbackRate(esGoods.getGoodsFeedbackRate());
                                    goodsInfoNest.setVendibilityStatus(buildGoodsInfoVendibility(goodsInfoVO, providerStoreMap, providerGoodsInfoVOMap));
                                    goodsInfoNest.setProviderStatus(goodsInfoVO.getProviderStatus() == null ? Constants.yes : goodsInfoVO.getProviderStatus());
                                    goodsInfoNest.setSupplyPrice(goodsInfoVO.getSupplyPrice());
                                    goodsInfoNest.setStoreId(esGoods.getStoreId());
                                    goodsInfoNest.setStoreName(esGoods.getStoreName());
                                    goodsInfoNest.setGoodsType(esGoods.getGoodsType());
                                    goodsInfoNest.setCpsSpecial(esGoods.getCpsSpecial());
                                    if (StringUtils.isNotBlank(goodsInfoVO.getProviderGoodsInfoId())) {
                                        GoodsInfoVO providerGoodsInfoVO = providerGoodsInfoVOMap.get(goodsInfoVO.getProviderGoodsInfoId());
                                        if (providerGoodsInfoVO != null) {
                                            goodsInfoNest.setStock(providerGoodsInfoVO.getStock());
                                        }
                                    }
                                    //填充规格值
                                    if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                                        goodsInfoNest.setSpecText(goodsInfoSpecDetailMap.get(goodsInfoVO.getGoodsInfoId()));
                                    }

                                    //为空，则以商品主图
                                    if (StringUtils.isEmpty(goodsInfoNest.getGoodsInfoImg())) {
                                        goodsInfoNest.setGoodsInfoImg(goods.getGoodsImg());
                                    }

                                    //区间价
                                    if (CollectionUtils.isNotEmpty(intervalPriceMap.get(goodsInfoVO.getGoodsInfoId()))) {
                                        List<BigDecimal> prices = intervalPriceMap.get(goodsInfoVO.getGoodsInfoId()).stream().map(GoodsIntervalPriceVO::getPrice).filter(Objects::nonNull).collect(Collectors.toList());
                                        goodsInfoNest.setIntervalMinPrice(prices.stream().filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(goodsInfoVO.getMarketPrice()));
                                        goodsInfoNest.setIntervalMaxPrice(prices.stream().filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(goodsInfoVO.getMarketPrice()));
                                    }

                                    if (goodsInfoNest.getBuyPoint() == null) {
                                        goodsInfoNest.setBuyPoint(NumberUtils.LONG_ZERO);
                                    }

                                    if (!firstSkuBySpuId.containsKey(goodsInfoNest.getGoodsId())) {
                                        firstSkuBySpuId.put(goodsInfoNest.getGoodsId(), goodsInfoNest);
                                    }

                                    //SKU索引
                                    EsGoodsInfo esGoodsInfo = new EsGoodsInfo();
                                    esGoodsInfo.setId(goodsInfoNest.getGoodsInfoId());
                                    esGoodsInfo.setGoodsId(goodsInfoNest.getGoodsId());
                                    if (Objects.nonNull(esCateBrand.getGoodsCate())) {
                                        esGoodsInfo.setGoodsCate(esCateBrand.getGoodsCate());
                                    }
                                    if (Objects.nonNull(esCateBrand.getGoodsBrand())) {
                                        esGoodsInfo.setGoodsBrand(esCateBrand.getGoodsBrand());
                                    }
                                    esGoodsInfo.setGoodsInfo(goodsInfoNest);
                                    esGoodsInfo.setAddedTime(goodsInfoNest.getAddedTime());
                                    esGoodsInfo.setGoodsLevelPrices(levelPriceMap.get(goodsInfoNest.getGoodsInfoId()));
                                    esGoodsInfo.setCustomerPrices(customerPriceMap.get(goodsInfoNest.getGoodsInfoId()));
                                    esGoodsInfo.setLowGoodsName(esGoods.getLowGoodsName());
                                    esGoodsInfo.setPinyinGoodsName(esGoods.getLowGoodsName());
                                    esGoodsInfo.setGoodsUnit(esGoods.getGoodsUnit());
                                    esGoodsInfo.setLinePrice(esGoods.getLinePrice());
                                    esGoodsInfo.setPropDetailIds(esGoods.getPropDetailIds());
                                    esGoodsInfo.setContractStartDate(esGoods.getContractStartDate());
                                    esGoodsInfo.setContractEndDate(esGoods.getContractEndDate());
                                    esGoodsInfo.setStoreState(esGoods.getStoreState());
                                    esGoodsInfo.setStoreCateIds(esGoods.getStoreCateIds());
                                    esGoodsInfo.setAuditStatus(esGoods.getAuditStatus());
                                    esGoodsInfo.setDistributionGoodsStatus(esGoods.getDistributionGoodsStatus());
                                    esGoodsInfo.setVendibilityStatus(buildGoodsInfoVendibility(goodsInfoVO, providerStoreMap, providerGoodsInfoVOMap));
                                    esGoodsInfo.setGoodsLabelList(esGoods.getGoodsLabelList());
                                    esGoodsInfo.setGoodsSource(esGoods.getGoodsSource());
                                    esGoodsInfo.setGoodsNo(esGoods.getGoodsNo());
                                    esGoodsInfo.setGoodsName(esGoods.getGoodsName());
                                    esGoods.getGoodsInfos().add(goodsInfoNest);
                                    IndexQuery iq = new IndexQuery();
                                    iq.setObject(esGoodsInfo);
                                    iq.setIndexName(EsConstants.DOC_GOODS_INFO_TYPE);
                                    iq.setType(EsConstants.DOC_GOODS_INFO_TYPE);
                                    esSkuList.add(iq);
                                });
                        //第一个SKU的信息
                        if (MapUtils.isNotEmpty(firstSkuBySpuId)) {
                            GoodsInfoNest goodsInfoNest = firstSkuBySpuId.get(esGoods.getId());
                            esGoods.setEsSortPrice(goodsInfoNest.getEsSortPrice());
                            esGoods.setSortPrice(goodsInfoNest.getEsSortPrice());
                            esGoods.setBuyPoint(goodsInfoNest.getBuyPoint());
//                        esGoods.setGoodsLevelPrices(levelPriceMap.getOrDefault(goodsInfoNest.getGoodsInfoId(), new ArrayList<>()));
//                        esGoods.setCustomerPrices(customerPriceMap.getOrDefault(goodsInfoNest.getGoodsInfoId(), new ArrayList<>()));
                        }
                        IndexQuery iq = new IndexQuery();
                        iq.setObject(esGoods);
                        iq.setIndexName(EsConstants.DOC_GOODS_TYPE);
                        iq.setType(EsConstants.DOC_GOODS_TYPE);
                        esGoodsList.add(iq);
                    });


                    //持久化商品
                    elasticsearchTemplate.bulkIndex(esGoodsList);
                    elasticsearchTemplate.refresh(EsGoods.class);

                    //持久化商品
                    elasticsearchTemplate.bulkIndex(esSkuList);
                    elasticsearchTemplate.refresh(EsGoodsInfo.class);

                    //清空缓存
                    intervalPriceMap.clear();
                    goodsInfoSpecDetailMap.clear();
//                    goodsPropDetailMap.clear();
//                specMap.clear();
                    levelPriceMap.clear();
                    customerPriceMap.clear();
//                    storeCateGoodsMap.clear();
                }
            } catch (Exception e) {
                logger.error("初始化ES商品页码位置".concat(String.valueOf(i)).concat("，异常："), e);
                errorThrow++;
                if (errorThrow >= 10) {
                    throw new SbcRuntimeException("K-030407", new Object[]{i});
                }
                i--;
            } catch (Throwable t) {
                logger.error("初始化ES商品页码位置".concat(String.valueOf(i)).concat("，异常："), t);
                throw new SbcRuntimeException("K-030407", new Object[]{i});
            }
        }

        logger.info(String.format("商品所有索引结束->花费%s毫秒", (System.currentTimeMillis() - startTime)));
    }



    /**
     * @param goods
     * @param providerStoreMap
     * @return
     */
    private Integer buildGoodsVendibility(GoodsVO goods, Map<Long, StoreVO> providerStoreMap, Map<String, GoodsVO> providerGoodsVOMap) {
        Integer vendibility = Constants.yes;

        LocalDateTime now = LocalDateTime.now();

        String providerGoodsId = goods.getProviderGoodsId();

        if (StringUtils.isNotBlank(providerGoodsId)) {

            GoodsVO providerGoods = providerGoodsVOMap.get(providerGoodsId);

            if (providerGoods != null) {
                if (Constants.no.equals(goods.getVendibility())) {
                    vendibility = Constants.no;
                }
            }

        }
        return vendibility;
    }

    /**
     * @param goodsInfo
     * @param providerStoreMap
     * @return
     */
    private Integer buildGoodsInfoVendibility(GoodsInfoVO goodsInfo, Map<Long, StoreVO> providerStoreMap, Map<String, GoodsInfoVO> providerGoodsInfoVOMap) {
        Integer vendibility = Constants.yes;

        String providerGoodsInfoId = goodsInfo.getProviderGoodsInfoId();

        if (StringUtils.isNotBlank(providerGoodsInfoId)) {

            GoodsInfoVO providerGoodsInfo = providerGoodsInfoVOMap.get(providerGoodsInfoId);

            if (providerGoodsInfo != null) {
                if (Constants.no.equals(goodsInfo.getVendibility())) {
                    vendibility = Constants.no;
                }
            }

        }
        return vendibility;
    }

    /**
     * 根据商品spu批量获取商品属性关键Map
     * @param goodsIds 商品id
     * @return 商品属性关键Map内容<商品id, 商品属性关联list>
     */
    private Map<String, List<GoodsPropDetailRelVO>> getPropDetailRelList(List<String> goodsIds) {
        GoodsPropDetailRelByIdsRequest relByIdsRequest = new GoodsPropDetailRelByIdsRequest();
        relByIdsRequest.setGoodsIds(goodsIds);
        List<GoodsPropDetailRelVO> goodsPropDetailRelVOList = goodsQueryProvider.getRefByGoodIds(relByIdsRequest).getContext().getGoodsPropDetailRelVOList();
        Map<Long, List<GoodsPropDetailRelVO>> idRel = goodsPropDetailRelVOList.stream().collect(Collectors.groupingBy(GoodsPropDetailRelVO::getPropId));
        BaseResponse<List<GoodsPropVO>> propsRes = goodsQueryProvider.getPropByIds(new ArrayList<>(idRel.keySet()));
        if(propsRes.getContext() != null){
            List<GoodsPropVO> props = propsRes.getContext();
            for (GoodsPropVO prop : props) {
                List<GoodsPropDetailRelVO> goodsPropDetailRelVos = idRel.get(prop.getPropId());
                for (GoodsPropDetailRelVO goodsPropDetailRelVo : goodsPropDetailRelVos) {
                    goodsPropDetailRelVo.setPropName(prop.getPropName());
                    goodsPropDetailRelVo.setPropType(prop.getPropType());
                }
            }
        }
        Map<String, List<GoodsPropDetailRelVO>> goodsPropRel = goodsPropDetailRelVOList.stream().collect(Collectors.groupingBy(GoodsPropDetailRelVO::getGoodsId));
        return goodsPropRel;
    }

    /**
     * 根据商品sku批量获取区间价键值Map
     * @param skuIds 商品skuId
     * @return 区间价键值Map内容<商品skuId, 区间价列表>
     */
    private Map<String, List<GoodsIntervalPriceVO>> getIntervalPriceMapBySkuId(List<String> skuIds) {
        List<GoodsIntervalPriceVO> voList = goodsIntervalPriceQueryProvider.listByGoodsIds(
                GoodsIntervalPriceListBySkuIdsRequest.builder().skuIds(skuIds).build()).getContext().getGoodsIntervalPriceVOList();
        return voList.stream().collect(Collectors.groupingBy(GoodsIntervalPriceVO::getGoodsInfoId));
    }
}
