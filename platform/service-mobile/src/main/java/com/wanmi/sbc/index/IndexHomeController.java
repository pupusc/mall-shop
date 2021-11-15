package com.wanmi.sbc.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.SortGoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.configure.SpringUtil;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.index.requst.BranchVenueIdRequest;
import com.wanmi.sbc.index.requst.KeyRequest;
import com.wanmi.sbc.index.requst.SkuIdsRequest;
import com.wanmi.sbc.index.requst.VersionRequest;
import com.wanmi.sbc.index.response.ActivityBranchResponse;
import com.wanmi.sbc.index.response.IndexConfigChild2Response;
import com.wanmi.sbc.index.response.IndexConfigResponse;
import com.wanmi.sbc.index.response.ProductConfigResponse;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.xxl.job.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @menu 商城首页
 * @tag feature_d_1111_index
 * @status undone
 */
@RestController
@RequestMapping("/index")
public class IndexHomeController {


    @Autowired
    private RefreshConfig refreshConfig;

    @Autowired
    private RedisListService redisService;

    @Autowired
    private RedisTemplate redisTemplate;
    public static final Integer GOODS_SIZE = 5;
    public static final Integer BOOKS_SIZE = 1;

    @Autowired
    private RedisService redis;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    /**
     * @description 获取首页配置数据
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/config")
    public BaseResponse<IndexConfigResponse> config() {
        IndexConfigResponse indexConfigResponse = JSON.parseObject(refreshConfig.getIndexConfig(), IndexConfigResponse.class);
        return BaseResponse.success(indexConfigResponse);
    }


    /**
     * @description 热销榜
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/hot")
    public BaseResponse<MicroServicePage<SortGoodsCustomResponse>> hot(@RequestBody VersionRequest versionRequest) {
        MicroServicePage<SortGoodsCustomResponse> page = new MicroServicePage();
        if (versionRequest.getPageNum() == 0) {
            versionRequest.setPageNum(1);
        }
        Long refreshHotCount = getRefreshHotCount(versionRequest.getPageNum());
        List<JSONObject> objectList = redisService
                .findByRange("hotGoods" + refreshHotCount, (versionRequest.getPageNum() - 1) * GOODS_SIZE, versionRequest.getPageNum() * GOODS_SIZE - 1);
        objectList.addAll(redisService
                .findByRange("hotBooks" + refreshHotCount, (versionRequest.getPageNum() - 1) * BOOKS_SIZE, versionRequest.getPageNum() * BOOKS_SIZE - 1));
        List<SortGoodsCustomResponse> goodsCustomResponseList = new ArrayList<>();
        for (JSONObject goodStr : objectList) {
            goodsCustomResponseList.add(JSONObject.toJavaObject(goodStr, SortGoodsCustomResponse.class));
        }
        marketPrice(goodsCustomResponseList.stream().filter(goodsInfo -> goodsInfo.getType() == 1).collect(Collectors.toList()));

        packageAtmosphereMessage(goodsCustomResponseList);
        page.setContent(goodsCustomResponseList);

        page.setNumber(versionRequest.getPageNum());
        return BaseResponse.success(page);
    }

    /**
     * @description 商品详情页双十一活动配置
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/productConfig")
    public BaseResponse<List<ProductConfigResponse>> productConfig(@RequestBody SkuIdsRequest skuIdsRequest) {
        List<ProductConfigResponse> productConfigResponseList = new ArrayList<>();
        if (skuIdsRequest == null || skuIdsRequest.getSkuIds() == null || skuIdsRequest.getSkuIds().size() == 0) {
            return BaseResponse.success(productConfigResponseList);
        }
        List<ProductConfigResponse> list = JSONArray.parseArray(refreshConfig.getRibbonConfig(), ProductConfigResponse.class);
        if (list == null || list.size() == 0) {
            return BaseResponse.success(productConfigResponseList);
        }
        Map<String, ProductConfigResponse> map = list.stream().collect(
                Collectors.toMap(ProductConfigResponse::getSkuId, a -> a, (k1, k2) -> k1));
        for (String skuId : skuIdsRequest.getSkuIds()) {
            ProductConfigResponse productConfigResponse = map.get(skuId);
            if (productConfigResponse != null && new Date().before(productConfigResponse.getEndTime())
                    && new Date().after(productConfigResponse.getStartTime())) {
                productConfigResponseList.add(productConfigResponse);
            }
        }
        return BaseResponse.success(productConfigResponseList);
    }


    /**
     * @description 获取是否在活动时间后
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/isActivity")
    public BaseResponse<Boolean> isActivity() {
        String dateStartStr = refreshConfig.getActivityStartTime();
        String dateEndStr = refreshConfig.getActivityEndTime();
        Date dateStr = DateUtil.parseDateTime(dateStartStr);
        Date dateEnd = DateUtil.parseDateTime(dateEndStr);
        return BaseResponse.success(new Date().after(dateStr) && new Date().before(dateEnd));
    }


    /**
     * @description 根据KEY取配置值
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/configByKey")
    public BaseResponse<Map<String, String>> configByKey(@RequestBody @Validated KeyRequest keyRequest) {
        List<String> allowKeyList = Arrays.asList(refreshConfig.getAllowKeys().split(","));
        Map<String, String> configMap = new HashMap<>();
        for (String key : keyRequest.getKeys()) {
            if (allowKeyList.contains(key)) {
                configMap.put(key, SpringUtil.getBean(key));
            }
        }
        return BaseResponse.success(configMap);
    }


    /**
     * @description 根据会场ID获取相应内容
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/shopActivityBranchConfig")
    public BaseResponse<ActivityBranchResponse> shopActivityBranchConfig(@RequestBody BranchVenueIdRequest branchVenueIdRequest) {
        ActivityBranchResponse activityBranchResponse = redis.getObj("activityBranch:" + branchVenueIdRequest.getBranchVenueId(), ActivityBranchResponse.class);
        List<IndexConfigChild2Response> branchVenueConfigs = JSONArray.parseArray(refreshConfig.getShopActivityBranchTopConfig(), IndexConfigChild2Response.class);
        activityBranchResponse.setBranchVenueConfigs(branchVenueConfigs.stream().filter(
                config -> config.getBranchVenueId().equals(branchVenueIdRequest.getBranchVenueId())
        ).collect(Collectors.toList()));
        activityBranchResponse.getBranchVenueContents().forEach(
                branchVenueContent -> {
                    marketPrice(branchVenueContent.getActivityBranchContentResponses().stream().filter(goodsInfo -> goodsInfo.getType() == 1).collect(Collectors.toList()));
                    packageAtmosphereMessage(branchVenueContent.getActivityBranchContentResponses());
                }
        );
        return BaseResponse.success(activityBranchResponse);
    }


    private void packageAtmosphereMessage(List<SortGoodsCustomResponse> goodsCustomResponseList) {
        List<ProductConfigResponse> list = JSONArray.parseArray(refreshConfig.getRibbonConfig(), ProductConfigResponse.class);
        Map<String, ProductConfigResponse> productConfigResponseMap = list.stream()
                .filter(productConfig -> new Date().after(productConfig.getStartTime()) && new Date().before(productConfig.getEndTime()))
                .collect(Collectors.toMap(ProductConfigResponse::getSkuId, Function.identity(), (k1, k2) -> k1));
        if (!productConfigResponseMap.isEmpty()) {
            goodsCustomResponseList.forEach(
                    goodsCustomResponse -> {
                        ProductConfigResponse productConfigResponse = productConfigResponseMap.get(goodsCustomResponse.getGoodsInfoId());
                        if (productConfigResponse != null) {
                            goodsCustomResponse.setAtmosphereFirstTitle(productConfigResponse.getTitle());
                            goodsCustomResponse.setAtmosphereSecondTitle(productConfigResponse.getContent());
                            goodsCustomResponse.setAtmospherePrice(productConfigResponse.getPrice());
                        }
                    }
            );
        }
    }

    /**
     * @description 热销榜
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/shopActivityBranchHot")
    public BaseResponse<MicroServicePage<SortGoodsCustomResponse>> shopActivityBranchHot(@RequestBody BranchVenueIdRequest branchVenueIdRequest) {
        MicroServicePage<SortGoodsCustomResponse> page = new MicroServicePage();
        if (branchVenueIdRequest.getPageNum() == 0) {
            branchVenueIdRequest.setPageNum(1);
        }
        Long refreshHotCount = getRefreshHotCount(branchVenueIdRequest.getPageNum());

        List<JSONObject> objectList = redisService
                .findByRange("activityBranch:hot:" + refreshHotCount + ":" + branchVenueIdRequest.getBranchVenueId(),
                        (branchVenueIdRequest.getPageNum() - 1) * GOODS_SIZE, branchVenueIdRequest.getPageNum() * GOODS_SIZE - 1);
        objectList.addAll(redisService
                .findByRange("hotBooks" + refreshHotCount, (branchVenueIdRequest.getPageNum() - 1) * BOOKS_SIZE, branchVenueIdRequest.getPageNum() * BOOKS_SIZE - 1));
        List<SortGoodsCustomResponse> goodsCustomResponseList = new ArrayList<>();
        for (JSONObject goodStr : objectList) {
            goodsCustomResponseList.add(JSONObject.toJavaObject(goodStr, SortGoodsCustomResponse.class));
        }
        marketPrice(goodsCustomResponseList.stream().filter(goodsInfo -> goodsInfo.getType() == 1).collect(Collectors.toList()));
        packageAtmosphereMessage(goodsCustomResponseList);
        page.setContent(goodsCustomResponseList);
        page.setNumber(branchVenueIdRequest.getPageNum());
        return BaseResponse.success(page);
    }

    private void marketPrice(List<SortGoodsCustomResponse> goodsInfoList) {
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            return;
        }
        //根据商品id列表 获取商品列表信息
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(goodsInfoList.size()); //这里主要是为啦防止书单里面的数量过分的多的情况，限制最多100个
        queryRequest.setGoodsInfoIds(goodsInfoList.stream().map(goodCustomer -> goodCustomer.getGoodsInfoId()).collect(Collectors.toList()));
        //获取会员和等级
        queryRequest.setQueryGoods(true);
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        queryRequest.setStoreState(StoreState.OPENING.toValue());
        queryRequest.setVendibility(Constants.yes);
        List<EsGoodsVO> esGoodsVOS = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext().getEsGoods().getContent();
        CustomerVO customer = bookListModelAndGoodsService.getCustomerVo();
        List<GoodsInfoVO> goodsInfoVOList = bookListModelAndGoodsService.packageGoodsInfoList(esGoodsVOS, customer);
        Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity(), (v1, v2) -> v1));
        goodsInfoList.forEach(
                goodsInfo -> {
                    GoodsInfoVO goodsInfoVO = goodsInfoVOMap.get(goodsInfo.getGoodsInfoId());
                    if (goodsInfoVO != null) {
                        BigDecimal showPrice = goodsInfoVO.getSalePrice() == null ? goodsInfoVO.getMarketPrice() == null ? new BigDecimal("100000") : goodsInfoVO.getMarketPrice() : goodsInfoVO.getSalePrice();
                        goodsInfo.setShowPrice(showPrice);
                    }
                }
        );
    }

    /**
     * 获取当前缓存批次
     *
     * @param pageNum
     * @return
     */
    private Long getRefreshHotCount(Integer pageNum) {
        Long refreshHotCount;
        String ip = HttpUtil.getIpAddr();
        if (pageNum > 1) {
            //翻页浏览，获取用户缓存浏览队列，若过期，取最新队列
            String refresObject = redis.getString("ip:" + ip);

            if (refresObject != null) {
                refreshHotCount = Long.valueOf(refresObject);
            } else {
                refreshHotCount = Long.valueOf(redis.getString("refreshHotCount"));
                if (!redis.hasKey("hotGoods" + refreshHotCount) && !redis.hasKey("hotBooks" + refreshHotCount)) {
                    refreshHotCount = refreshHotCount - 1;
                }
                redis.setString("ip:" + ip, refreshHotCount.toString(), 30 * 60);
            }
        } else {
            //首页浏览，获取缓存最新队列，缓存用户浏览队列
            refreshHotCount = Long.valueOf(redis.getString("refreshHotCount"));
            if (!redis.hasKey("hotGoods" + refreshHotCount) && !redis.hasKey("hotBooks" + refreshHotCount)) {
                refreshHotCount = refreshHotCount - 1;
            }
            redis.setString("ip:" + ip, refreshHotCount.toString(), 30 * 60);
        }
        return refreshHotCount;
    }
}
