package com.wanmi.sbc.marketing.coupon.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByGoodsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByIdsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.marketing.MarketingPluginService;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeQueryRequest;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponCache;
import com.wanmi.sbc.marketing.coupon.model.root.*;
import com.wanmi.sbc.marketing.coupon.model.vo.CouponView;
import com.wanmi.sbc.marketing.coupon.mongorepository.CouponCacheRepository;
import com.wanmi.sbc.marketing.coupon.request.CouponCacheCenterRequest;
import com.wanmi.sbc.marketing.coupon.request.CouponCacheInitRequest;
import com.wanmi.sbc.marketing.coupon.request.CouponCacheQueryRequest;
import com.wanmi.sbc.marketing.coupon.request.CouponQueryRequest;
import com.wanmi.sbc.marketing.coupon.response.CouponCenterPageResponse;
import com.wanmi.sbc.marketing.coupon.response.CouponGoodsQueryResponse;
import com.wanmi.sbc.marketing.coupon.response.CouponListResponse;
import com.wanmi.sbc.marketing.redis.RedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponCacheService {

    private static final String TIME_KEY = "COUPON_CACHE_REFRESH_TIME";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CouponCacheRepository couponCacheRepository;

    @Autowired
    private CouponActivityService couponActivityService;

    @Autowired
    private CouponInfoService couponInfoService;

    @Autowired
    private CouponActivityConfigService couponActivityConfigService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CouponMarketingScopeService couponMarketingScopeService;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private MarketingPluginService marketingPluginService;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private CouponCateService couponCateService;

    @Autowired
    private CouponCodeService couponCodeService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private ContractBrandQueryProvider contractBrandQueryProvider;

    @Autowired
    private ClassifyProvider classifyProvider;

    /**
     * 领券中心 - 查询正在进行的优惠券活动，暂时全部查询，不带任何条件，领券的时候做判断
     *
     * @param queryRequest
     * @return
     */
    public CouponCenterPageResponse getCouponStarted(CouponCacheCenterRequest queryRequest) {
        this.refreshCache();
        CouponCacheQueryRequest request = CouponCacheQueryRequest.builder()
                .couponType(queryRequest.getCouponType())
                .couponCateId(queryRequest.getCouponCateId())
                .couponScene(queryRequest.getCouponScene())
                .couponActivityIds(queryRequest.getActivityIds())
                .couponInfoIds(queryRequest.getCouponInfoIds())
                .activityName(queryRequest.getActivityName())
                .build();
        if(Objects.nonNull(queryRequest.getStoreId())) {
            request.setStoreIds(Arrays.asList(queryRequest.getStoreId()));
        }
        /**
         * 活动先生效的在前
         * 通用券＞店铺券＞运费券
         * 优惠券开始时间
         * */
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponActivity.startTime"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponInfo.couponTypeInteger"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponInfo.createTime"));
        PageRequest pageRequest = PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize(),
                Sort.by(orders));
        List<CouponCache> couponCacheList = mongoTemplate.find(new Query(request.getCriteria()).with(pageRequest),
                CouponCache.class);
        if (CollectionUtils.isNotEmpty(couponCacheList)) {
            long count = mongoTemplate.count(new Query(request.getCriteria()), CouponCache.class);
            // 获取店铺ids，排除平台优惠券
            List<Long> storeIds =
                    couponCacheList.stream().filter(item -> item.getCouponInfo().getPlatformFlag() == DefaultFlag.NO)
                            .map(item -> item.getCouponInfo().getStoreId()).collect(Collectors.toList());
            // 获取平台类目
            List<Long> cateIds =
                    couponCacheList.stream().filter(item -> item.getCouponInfo().getScopeType() == ScopeType.BOSS_CATE)
                            .flatMap(item -> item.getScopes().stream().map(CouponMarketingScope::getScopeId)).map(Long::valueOf).distinct().sorted().collect(Collectors.toList());
            // 获取店铺类目
            List<Long> storeCateIds =
                    couponCacheList.stream().filter(item -> item.getCouponInfo().getScopeType() == ScopeType.STORE_CATE)
                            .flatMap(item -> item.getScopes().stream().map(CouponMarketingScope::getScopeId)).map(Long::valueOf).distinct().sorted().collect(Collectors.toList());
            // 获取品牌
            List<Long> brandIds =
                    couponCacheList.stream().filter(item -> item.getCouponInfo().getScopeType() == ScopeType.BRAND)
                            .flatMap(item -> item.getScopes().stream().map(CouponMarketingScope::getScopeId)).map(Long::valueOf).distinct().sorted().collect(Collectors.toList());

            return CouponCenterPageResponse.builder()
                    //券详情
                    .couponViews(
                            new PageImpl<>(CouponView.converter(couponCacheList
                                    //券库存
                                    , couponCodeService.mapLeftCount(couponCacheList)
                                    //领用状态
                                    , couponCodeService.mapFetchStatus(couponCacheList, queryRequest.getCustomerId()))
                                    , pageRequest, count))
                    //storeNameList
                    .storeMap(this.mapStoreNameById(storeIds))
                    //平台分类+品牌+商家分类
                    .cateMap(this.mapCateById(cateIds))
                    .brandMap(this.mapBrandById(brandIds))
                    .storeCateMap(this.mapStoreCateById(storeCateIds))
                     .build();
        } else {
            return CouponCenterPageResponse.builder().couponViews(new PageImpl<>(Collections.emptyList(), pageRequest
                    , 0)).build();
        }
    }


    /**
     * 通过商品列表，查询相关优惠券
     *
     * @param goodsInfoIds
     * @param customerId
     * @return
     */
    public CouponListResponse listCouponForGoodsList(List<String> goodsInfoIds, String customerId, Long storeId,List<String> couponScene) {
        GoodsInfoListByIdsRequest goodsInfoListByIdsRequest = new GoodsInfoListByIdsRequest();
        goodsInfoListByIdsRequest.setGoodsInfoIds(goodsInfoIds);
        if (Objects.nonNull(storeId)) {
            goodsInfoListByIdsRequest.setStoreId(storeId);
        }
        List<GoodsInfoVO> goodsInfoList =
                goodsInfoQueryProvider.listByIds(goodsInfoListByIdsRequest).getContext().getGoodsInfos();
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            throw new SbcRuntimeException(CouponErrorCode.GOODS_NOT_EXIST);
        }
        CustomerVO customer = null;
        if (customerId != null) {

//        Customer customer = customerService.findById(customerId);
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
//        if (customer == null) {
//            throw new SbcRuntimeException(CouponErrorCode.CUSTOMER_NOT_EXIST);
//        }
        }
        List<CouponCache> couponCacheList = this.listCouponForGoodsList(goodsInfoList, customer,couponScene);

        // 获取店铺ids，排除平台优惠券
        List<Long> storeIds =
                couponCacheList.stream().filter(item -> item.getCouponInfo().getPlatformFlag() == DefaultFlag.NO)
                        .map(item -> item.getCouponInfo().getStoreId()).distinct().collect(Collectors.toList());
        return CouponListResponse.builder()
                //券详情
                .couponViews(CouponView.converter(couponCacheList
                        //券库存
                        , couponCodeService.mapLeftCount(couponCacheList)
                        //领用状态
                        , couponCodeService.mapFetchStatus(couponCacheList, customerId)))
                //storeNameList
                .storeMap(this.mapStoreNameById(storeIds))
                .build();
    }


    /**
     * 通过商品列表，查询相关优惠券
     *
     * @param goodsInfoList
     * @param customer
     * @return
     */
//    public List<CouponCache> listCouponForGoodsList(List<GoodsInfo> goodsInfoList, Customer customer){
    public List<CouponCache> listCouponForGoodsList(List<GoodsInfoVO> goodsInfoList, CustomerVO customer,List<String> couponScene) {
        //组装等级数据
//        Map<Long, CustomerLevel> levelMap = marketingPluginService.getCustomerLevels(goodsInfoList, customer);
        Map<Long, CommonLevelVO> levelMap = marketingPluginService.getCustomerLevels(goodsInfoList, customer);

        List<String> goodsIds = goodsInfoList.stream().filter(item -> item.getGoodsId() != null)
                .map(GoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());

        //组装商品分类 -- 店铺类目
        List<Long> storeCateIds = new ArrayList<>();
        Map<String, List<Integer>> storeCateIdMap = classifyProvider.searchGroupedClassifyIdByGoodsId(goodsIds).getContext();
        if(storeCateIdMap != null){
            for (List<Integer> value : storeCateIdMap.values()) {
                for (Integer integer : value) {
                    storeCateIds.add(integer.longValue());
                }
            }
        }
        List<GoodsVO> goodsList =
                goodsQueryProvider.listByIds(new GoodsListByIdsRequest(goodsIds)).getContext().getGoodsVOList();
        //组装商品分类 -- 平台类目
        List<Long> cateIds = goodsList.stream().filter(item -> item.getCateId() != null)
                .map(GoodsVO::getCateId).collect(Collectors.toList());
        //组装品牌
        List<Long> brandIds = goodsList.stream().filter(item -> item.getBrandId() != null)
                .map(GoodsVO::getBrandId).distinct().collect(Collectors.toList());

        this.refreshCache();
        CouponCacheQueryRequest request = CouponCacheQueryRequest.builder()
                .brandIds(brandIds)
                .cateIds(cateIds)
                .storeCateIds(storeCateIds)
                .goodsInfoIds(goodsInfoList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()))
                .storeIds(goodsInfoList.stream().map(GoodsInfoVO::getStoreId).collect(Collectors.toList()))
                .couponScene(couponScene)
                .levelMap(levelMap).build();
        /**
         * 通用券＞店铺券
         * 面值大在前
         * 如有重复：按创建时间，最先创建的在前
         * */
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponInfo.couponTypeInteger"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "couponInfo.denomination"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponInfo.createTime"));
        return mongoTemplate.find(new Query(request.getCriteria()).with(Sort.by(orders)), CouponCache.class);
    }

    /**
     * 通过商品，查询相关优惠券
     *
     * @param goodsInfoId
     * @param customerId
     * @return
     */
    public CouponListResponse listCouponForGoodsDetail(String goodsInfoId, String customerId, Long storeId,List<String> couponScene) {
        GoodsInfoByIdRequest goodsInfoByIdRequest = new GoodsInfoByIdRequest();
        goodsInfoByIdRequest.setGoodsInfoId(goodsInfoId);
        if (Objects.nonNull(storeId)) {
            goodsInfoByIdRequest.setStoreId(storeId);
        }
        GoodsInfoByIdResponse goodsInfo = goodsInfoQueryProvider.getById(goodsInfoByIdRequest).getContext();
        Map<Long, CommonLevelVO> levelMap = new HashMap();
        if (goodsInfo == null) {
            throw new SbcRuntimeException(CouponErrorCode.GOODS_NOT_EXIST);
        }

        CustomerVO customer = null;
        if (customerId != null) {
            //        Customer customer = customerService.findById(customerId);
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        }

//        Map<Long, CustomerLevel> levelMap = marketingPluginService.getCustomerLevels(Collections.singletonList
//        (goodsInfo), customer);
        levelMap = marketingPluginService.getCustomerLevels(Collections.singletonList
                (goodsInfo), customer);

        GoodsByIdResponse goods = goodsQueryProvider.getById(new GoodsByIdRequest(goodsInfo.getGoodsId())).getContext();
        if (goods == null) {
            throw new SbcRuntimeException(CouponErrorCode.GOODS_NOT_EXIST);
        }
        //组装分类
        goodsInfo.setCateId(goods.getCateId());
        //组装品牌
        goodsInfo.setBrandId(goods.getBrandId());
        List<CouponCache> couponCacheList = listCouponForGoodsInfo(goodsInfo, levelMap,couponScene);
//        List<Long> storeIds = couponCacheList.stream().map(item -> item.getCouponInfo().getStoreId()).distinct()
//        .collect(Collectors.toList());
        return CouponListResponse.builder()
                //券详情
                .couponViews(CouponView.converter(couponCacheList
                        //券库存
                        , couponCodeService.mapLeftCount(couponCacheList)
                        //领用状态
                        , couponCodeService.mapFetchStatus(couponCacheList, customerId)))
                //storeNameList 商品详情，页面上有店铺名称，不需要查询一次
//                .storeMap(this.mapStoreNameById(storeIds))
                .build();
    }


    public CouponListResponse getResponse(List<CouponCache> caches, CustomerVO customerVO) {

        return CouponListResponse.builder()
                //券详情
                .couponViews(CouponView.converter(caches
                        //券库存
                        , couponCodeService.mapLeftCount(caches)
                        //领用状态
                        , couponCodeService.mapFetchStatus(caches, customerVO.getCustomerId())))
                //storeNameList 商品详情，页面上有店铺名称，不需要查询一次
//                .storeMap(this.mapStoreNameById(storeIds))
                .build();

    }

    /**
     * 通过商品 + 用户等级，查询相关优惠券 = 提供给营销插件使用
     * <p>
     * 营销插件，列表 + 详情 都会使用该方法
     * 默认入参GoodsInfo已经包含了平台类目cateId和品牌brandId
     *
     * @param goodsInfo
     * @param levelMap
     * @return
     */
    public List<CouponCache> listCouponForGoodsInfo(GoodsInfoVO goodsInfo, Map<Long, CommonLevelVO> levelMap,List<String> couponScene) {
        //组装店铺分类
        List<Long> storeCateIds = new ArrayList<>();
        Map<String, List<Integer>> storeCateIdMap = classifyProvider.searchGroupedClassifyIdByGoodsId(Collections.singletonList(goodsInfo.getGoodsId())).getContext();
        if(storeCateIdMap != null){
            List<Integer> cateIds = storeCateIdMap.get(goodsInfo.getGoodsId());
            if(cateIds != null){
                storeCateIds = cateIds.stream().map(Integer::longValue).collect(Collectors.toList());
            }
        }
        this.refreshCache();
        CouponCacheQueryRequest request = CouponCacheQueryRequest.builder()
                .brandIds(goodsInfo.getBrandId() != null ? Collections.singletonList(goodsInfo.getBrandId()) : null)
                .cateIds(goodsInfo.getCateId() != null ? Collections.singletonList(goodsInfo.getCateId()) : null)
                .storeCateIds(storeCateIds)
                .goodsInfoIds(goodsInfo.getGoodsInfoId() != null ?
                        Collections.singletonList(goodsInfo.getGoodsInfoId()) : null)
                .levelMap(levelMap)
                .storeIds(Collections.singletonList(goodsInfo.getStoreId()))
                .couponScene(couponScene)
                .build();
        /**
         * 通用券＞店铺券
         * 面值大在前
         * 如有重复：按创建时间，最先创建的在前
         * */
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponInfo.couponTypeInteger"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "couponInfo.denomination"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponInfo.createTime"));
        return mongoTemplate.find(new Query(request.getCriteria()).with(Sort.by(orders)), CouponCache.class);
    }


    public List<CouponCache> listCouponForGoodsInfos(GoodsInfoVO goodsInfo, Map<Long, CommonLevelVO> levelMap,List<Long> storeCateIds,List<String> couponScene) {
        CouponCacheQueryRequest request = CouponCacheQueryRequest.builder()
                .brandIds(goodsInfo.getBrandId() != null ? Collections.singletonList(goodsInfo.getBrandId()) : null)
                .cateIds(goodsInfo.getCateId() != null ? Collections.singletonList(goodsInfo.getCateId()) : null)
                .storeCateIds(storeCateIds)
                .goodsInfoIds(goodsInfo.getGoodsInfoId() != null ?
                        Collections.singletonList(goodsInfo.getGoodsInfoId()) : null)
                .levelMap(levelMap)
                .storeIds(Collections.singletonList(goodsInfo.getStoreId()))
                .couponScene(couponScene)
                .build();
        /**
         * 通用券＞店铺券
         * 面值大在前
         * 如有重复：按创建时间，最先创建的在前
         * */
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponInfo.couponTypeInteger"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "couponInfo.denomination"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "couponInfo.createTime"));
        return mongoTemplate.find(new Query(request.getCriteria()).with(Sort.by(orders)), CouponCache.class);
    }



    /**
     * 凑单页方法
     *
     * @param couponId
     * @return 返回查询条件，供bff调用goodsEs查询
     */
    public CouponGoodsQueryResponse listGoodsByCouponId(String couponId, String activityId, String customerId,
                                                        Long storeId) {
        CouponInfo couponInfo;
        if (Objects.nonNull(storeId)) {
            couponInfo = couponInfoService.findByCouponIdAndStoreIdAndDelFlag(couponId, storeId);
        } else {
            couponInfo = couponInfoService.getCouponInfoById(couponId);
        }
        if (couponInfo == null) {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_INFO_NOT_EXIST);
        }

        List<CouponCode> couponCodeList = couponCodeService.listCouponCodeByCondition(CouponCodeQueryRequest.builder()
                .customerId(customerId).delFlag(DeleteFlag.NO).useStatus(DefaultFlag.NO)
                .couponId(couponId).activityId(activityId).build());

        if (CollectionUtils.isEmpty(couponCodeList)) {
            throw new SbcRuntimeException(CouponErrorCode.COUPON_INFO_NOT_EXIST);
        }

        //当前用户所拥有的优惠券 -- 因为订单逆向流程会有退还券，有可能一个用户会有多张相同（相同活动+相同优惠券）的券，去第一张
        CouponCode couponCode = couponCodeList.get(0);

        //组装优惠券信息
        CouponGoodsQueryResponse couponGoodsQueryResponse = new CouponGoodsQueryResponse();
        if (couponInfo.getCouponType() == CouponType.STORE_VOUCHERS) {
            couponGoodsQueryResponse.setStoreId(couponInfo.getStoreId());
        }
        couponGoodsQueryResponse.setStartTime(DateUtil.format(couponCode.getStartTime(), DateUtil.FMT_DATE_1));
        couponGoodsQueryResponse.setEndTime(DateUtil.format(couponCode.getEndTime(), DateUtil.FMT_DATE_1));
        couponGoodsQueryResponse.setScopeType(couponInfo.getScopeType());
        couponGoodsQueryResponse.setPlatformFlag(couponInfo.getPlatformFlag());
        if (couponInfo.getPlatformFlag() == DefaultFlag.NO) {
//            Store store = storeService.find(couponInfo.getStoreId());
            StoreVO store =
                    storeQueryProvider.getById(new StoreByIdRequest(couponInfo.getStoreId())).getContext().getStoreVO();
            couponGoodsQueryResponse.setStoreName(store != null ? store.getStoreName() : null);
        }
        couponGoodsQueryResponse.setFullBuyType(couponInfo.getFullBuyType());
        couponGoodsQueryResponse.setFullBuyPrice(couponInfo.getFullBuyPrice());
        couponGoodsQueryResponse.setDenomination(couponInfo.getDenomination());

        List<CouponMarketingScope> scopeList;
        switch (couponInfo.getScopeType()) {
            case ALL:
                couponGoodsQueryResponse.setIsAll(DefaultFlag.YES);
                break;
            case BRAND:
                scopeList = couponMarketingScopeService.listScopeByCouponId(couponId);
                couponGoodsQueryResponse.setIsAll(DefaultFlag.NO);
                final List<Long> brandIds =
                        scopeList.stream().map(item -> Long.parseLong(item.getScopeId())).sorted().collect(Collectors.toList());
                couponGoodsQueryResponse.setBrandIds(brandIds);
                couponGoodsQueryResponse.setBrandMap(mapBrandById(brandIds));
                //过滤已经被删除或者取消签约的品牌

                if (DefaultFlag.NO.equals(couponInfo.getPlatformFlag())) {
                    //获取店铺签约的品牌
                    ContractBrandListRequest brandRequest = new ContractBrandListRequest();
                    brandRequest.setGoodsBrandIds(brandIds);
                    brandRequest.setStoreId(couponInfo.getStoreId());
                    //获取店铺签约的品牌
                    List<ContractBrandVO> brandList =
                            contractBrandQueryProvider.list(brandRequest).getContext().getContractBrandVOList();
                    //筛选出店铺签约的品牌信息
                    brandList = brandList.stream().filter(item ->
                            brandIds.stream().anyMatch(i ->
                                    i.equals(item.getGoodsBrand().getBrandId())
                            )
                    ).collect(Collectors.toList());
                    couponGoodsQueryResponse.setQueryBrandIds(
                            brandList.stream().map(i -> i.getGoodsBrand().getBrandId()).collect(Collectors.toList()));
                } else {
                    //获取平台的品牌
                    GoodsBrandListRequest brandRequest = new GoodsBrandListRequest();
                    brandRequest.setDelFlag(DeleteFlag.NO.toValue());
                    brandRequest.setBrandIds(brandIds);
                    List<GoodsBrandVO> brandList =
                            goodsBrandQueryProvider.list(brandRequest).getContext().getGoodsBrandVOList();
                    couponGoodsQueryResponse.setQueryBrandIds(
                            brandList.stream().map(i -> i.getBrandId()).collect(Collectors.toList()));
                }

                break;
            case BOSS_CATE:
                scopeList = couponMarketingScopeService.listScopeByCouponId(couponId);
                couponGoodsQueryResponse.setIsAll(DefaultFlag.NO);
                List<Long> cateIds =
                        scopeList.stream().map(item -> Long.parseLong(item.getScopeId())).sorted().collect(Collectors.toList());
                List<Long> cateIds4es = scopeList.stream().filter(item -> item.getCateGrade() == 3)
                        .map(item -> Long.parseLong(item.getScopeId())).collect(Collectors.toList());
                couponGoodsQueryResponse.setCateIds(cateIds);
                couponGoodsQueryResponse.setCateIds4es(cateIds4es);
                couponGoodsQueryResponse.setCateMap(mapCateById(cateIds));
                break;
            case STORE_CATE:
                scopeList = couponMarketingScopeService.listScopeByCouponId(couponId);
                couponGoodsQueryResponse.setIsAll(DefaultFlag.NO);
                List<Long> storeCateIds =
                        scopeList.stream().map(item -> Long.parseLong(item.getScopeId())).sorted().collect(Collectors.toList());
                couponGoodsQueryResponse.setStoreCateIds(storeCateIds);
                couponGoodsQueryResponse.setStoreCateMap(mapStoreCateById(storeCateIds));
                break;
            case SKU:
                scopeList = couponMarketingScopeService.listScopeByCouponId(couponId);
                couponGoodsQueryResponse.setIsAll(DefaultFlag.NO);
                couponGoodsQueryResponse.setGoodsInfoId(scopeList.stream().map(CouponMarketingScope::getScopeId).collect(Collectors.toList()));
                break;
            default:
                couponGoodsQueryResponse.setIsAll(DefaultFlag.YES);
                break;
        }
        return couponGoodsQueryResponse;
    }


    /**
     * 刷新优惠券缓存方法
     */
    public void refreshCache() {
        boolean hasKey = redisService.hasKey(TIME_KEY);
        LocalDateTime now = LocalDateTime.now();
        CouponActivity lastActivity = couponActivityService.getLastActivity();
        if (lastActivity == null) {
            return;
        }
        if (!hasKey) {
            //全量刷新缓存
            CouponCacheInitRequest request = CouponCacheInitRequest.builder().build();
            List<CouponCache> cacheList = couponActivityConfigService.queryCouponStarted(request);
            this.addScope(cacheList);
            this.addCouponCate(cacheList);
            couponCacheRepository.deleteAll();
            couponCacheRepository.saveAll(cacheList);
            redisService.setString(TIME_KEY, now.format(TIME_FORMAT));
        } else {
            LocalDateTime lastTime = lastActivity.getStartTime();
            LocalDateTime cacheTime = LocalDateTime.parse(redisService.getString(TIME_KEY), TIME_FORMAT);
            if (lastTime.isAfter(cacheTime.minusMinutes(2))) {// 因为我们活动的时间是精确到分钟，所以会有一分钟的误差
                //缓存需要部分更新
                CouponCacheInitRequest request =
                        CouponCacheInitRequest.builder().queryEndTime(now).queryStartTime(lastTime).build();
                List<CouponCache> cacheList = couponActivityConfigService.queryCouponStarted(request);
                this.addScope(cacheList);
                this.addCouponCate(cacheList);
                Set<String> activityIdSet =
                        cacheList.stream().map(CouponCache::getCouponActivityId).collect(Collectors.toSet());
                //按id集合删除缓存
                couponCacheRepository.deleteByCouponActivityIdIn(activityIdSet);
                couponCacheRepository.saveAll(cacheList);
                redisService.setString(TIME_KEY, now.format(TIME_FORMAT));
            }
        }
    }


    /**
     * 刷新优惠券缓存方法
     * 按优惠券活动ids局部刷新
     */
    public void refreshCachePart(List<String> activityIds) {
        //缓存需要部分更新
        CouponCacheInitRequest request = CouponCacheInitRequest.builder().couponActivityIds(activityIds).build();
        List<CouponCache> cacheList = couponActivityConfigService.queryCouponStarted(request);
        this.addScope(cacheList);
        this.addCouponCate(cacheList);
        Set<String> activityIdSet =
                cacheList.stream().map(CouponCache::getCouponActivityId).collect(Collectors.toSet());
        //按id集合删除缓存
        couponCacheRepository.deleteByCouponActivityIdIn(activityIdSet);
        couponCacheRepository.saveAll(cacheList);
    }

    /**
     * 组装优惠券涵盖范围
     *
     * @param cacheList
     */
    private void addScope(List<CouponCache> cacheList) {
        List<String> couponIds =
                cacheList.stream().map(CouponCache::getCouponInfoId).distinct().collect(Collectors.toList());
        Map<String, List<CouponMarketingScope>> scopeList = couponMarketingScopeService.mapScopeByCouponIds(couponIds);
        cacheList.forEach(item -> item.setScopes(scopeList.get(item.getCouponInfoId())));
    }

    /**
     * 组装优惠券分类
     *
     * @param cacheList
     */
    private void addCouponCate(List<CouponCache> cacheList) {
        List<String> couponIds =
                cacheList.stream().map(CouponCache::getCouponInfoId).distinct().collect(Collectors.toList());
        Map<String, List<CouponCateRela>> cateMap = couponCateService.mapCateByCouponIds(couponIds);
        cacheList.forEach(item -> {
            List<CouponCateRela> relaList = cateMap.get(item.getCouponInfoId());
            if (relaList != null) {
                item.setCouponCateIds(relaList.stream().map(CouponCateRela::getCateId).collect(Collectors.toList()));
            }
        });
    }

    /**
     * 因为优惠券分类随时可以修改
     *
     * @param couponCateId
     * @param isDelete
     */
    public void updateCateRelaCache(String couponCateId, boolean isDelete) {
        Query query;
        if (isDelete) {
            query = Query.query(Criteria.where("couponCateIds").is(couponCateId));
        } else {
            query = Query.query(new Criteria().andOperator(
                    Criteria.where("couponCateIds").is(couponCateId),
                    Criteria.where("couponInfo.platformFlag").is(DefaultFlag.NO.toString())
            ));
        }
        mongoTemplate.updateMulti(query, new Update().pull("couponCateIds", couponCateId), CouponCache.class);
    }

    /**
     * 更新优惠券剩余标识
     *
     * @param activityConfigId
     * @param defaultFlag
     */
    public void updateCouponLeftCache(String activityConfigId, DefaultFlag defaultFlag) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("activityConfigId").is(activityConfigId)),
                Update.update("hasLeft", defaultFlag), CouponCache.class);
    }

    /**
     * 获取storeName的map
     *
     * @param storeIds
     * @return
     */
//    private Map<Long, String> mapStoreNameById(List<Long> storeIds) {
//        if (CollectionUtils.isEmpty(storeIds)) return null;
//        List<Store> storeList = storeService.findList(storeIds);
//        return storeList.stream().collect(Collectors.toMap(Store::getStoreId, Store::getStoreName));
//    }
    private Map<Long, String> mapStoreNameById(List<Long> storeIds) {
        if (CollectionUtils.isEmpty(storeIds)) {
            return null;
        }
        List<StoreVO> storeList = storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest
                (storeIds)).getContext().getStoreVOList();
        return storeList.stream().collect(Collectors.toMap(StoreVO::getStoreId, StoreVO::getStoreName));
    }

    /**
     * 获取cateName的map
     *
     * @param cateIds
     * @return
     */
    private Map<Long, String> mapCateById(List<Long> cateIds) {
        if (CollectionUtils.isEmpty(cateIds)) {
            return null;
        }
        List<GoodsCateVO> goodsCateList =
                goodsCateQueryProvider.getByIds(new GoodsCateByIdsRequest(cateIds)).getContext().getGoodsCateVOList();
        return goodsCateList.stream().collect(Collectors.toMap(GoodsCateVO::getCateId, GoodsCateVO::getCateName));
    }

    /**
     * 获取storeCateName的map
     *
     * @param storeCateIds
     * @return
     */
    private Map<Long, String> mapStoreCateById(List<Long> storeCateIds) {
        if (CollectionUtils.isEmpty(storeCateIds)) {
            return null;
        }
        List<StoreCateVO> goodsCateList =
                storeCateQueryProvider.listByIds(new StoreCateListByIdsRequest(storeCateIds)).getContext().getStoreCateVOList();
        return goodsCateList.stream().collect(Collectors.toMap(StoreCateVO::getStoreCateId, StoreCateVO::getCateName));
    }

    /**
     * 获取storeCateName的map
     *
     * @param brandIds
     * @return
     */
    private Map<Long, String> mapBrandById(List<Long> brandIds) {
        if (!CollectionUtils.isNotEmpty(brandIds)) {
            return null;
        }
        List<GoodsBrandVO> goodsCateList =
                goodsBrandQueryProvider.listByIds(new GoodsBrandByIdsRequest(brandIds)).getContext().getGoodsBrandVOList();
        return goodsCateList.stream().collect(Collectors.toMap(GoodsBrandVO::getBrandId, GoodsBrandVO::getBrandName));
    }

    public CouponCenterPageResponse getCouponList(CouponQueryRequest request) {
        Page<CouponCache> pageCache = couponActivityConfigService.queryCouponStartedAndUnStart(request);
        if (pageCache!= null && CollectionUtils.isNotEmpty(pageCache.getContent())) {
            return CouponCenterPageResponse.builder()
                    //券详情
                    .couponViews(
                            new PageImpl<>(CouponView.converter(pageCache.getContent()
                                    //券库存
                                    , couponCodeService.mapLeftCount(pageCache.getContent())
                                    //领用状态
                                    , couponCodeService.mapFetchStatus(pageCache.getContent(), null))
                                    , request.getPageable(), pageCache.getTotalElements()))
                    .build();
        } else {
            return CouponCenterPageResponse.builder().couponViews(new PageImpl<>(Collections.emptyList(), request.getPageable()
                    , 0)).build();
        }
    }


}
