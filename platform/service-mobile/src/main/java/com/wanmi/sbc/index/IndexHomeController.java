package com.wanmi.sbc.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.booklistmodel.response.SortGoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.configure.SpringUtil;
import com.wanmi.sbc.index.requst.KeyRequest;
import com.wanmi.sbc.index.requst.SkuIdsRequest;
import com.wanmi.sbc.index.requst.VersionRequest;
import com.wanmi.sbc.index.response.IndexConfigResponse;
import com.wanmi.sbc.index.response.ProductConfigResponse;
import com.wanmi.sbc.redis.RedisListService;
import com.xxl.job.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
        Long refreshHotCount;
        String ip = HttpUtil.getIpAddr();
        if (versionRequest.getPageNum() > 1) {
            //翻页浏览，获取用户缓存浏览队列，若过期，取最新队列
            Object refresObject = redisTemplate.opsForValue().get("ip:" + ip);
            if (refresObject != null) {
                refreshHotCount = Long.valueOf(refresObject.toString());
            } else {
                refreshHotCount = Long.valueOf(redisTemplate.opsForValue().get("refreshHotCount").toString());
                if (!redisTemplate.hasKey("hotGoods" + refreshHotCount) && !redisTemplate.hasKey("hotBooks" + refreshHotCount)) {
                    refreshHotCount = refreshHotCount - 1;
                }
                redisTemplate.opsForValue().set("ip:" + ip, refreshHotCount.toString(), 30, TimeUnit.MINUTES);
            }
        } else {
            //首页浏览，获取缓存最新队列，缓存用户浏览队列
            refreshHotCount = Long.valueOf(redisTemplate.opsForValue().get("refreshHotCount").toString());
            if (!redisTemplate.hasKey("hotGoods" + refreshHotCount) && !redisTemplate.hasKey("hotBooks" + refreshHotCount)) {
                refreshHotCount = refreshHotCount - 1;
            }
            redisTemplate.opsForValue().set("ip:" + ip, refreshHotCount.toString(), 30, TimeUnit.MINUTES);
        }

        List<String> objectList = redisService
                .findByRange("hotGoods" + refreshHotCount, (versionRequest.getPageNum() - 1) * GOODS_SIZE, versionRequest.getPageNum() * GOODS_SIZE - 1);
        objectList.addAll(redisService
                .findByRange("hotBooks" + refreshHotCount, (versionRequest.getPageNum() - 1) * BOOKS_SIZE, versionRequest.getPageNum() * BOOKS_SIZE - 1));

        List<SortGoodsCustomResponse> goodsCustomResponseList = new ArrayList<>();
        for (String goodStr:objectList) {
            goodStr = goodStr.replaceAll("\\\\", "");
            if (goodStr.startsWith("\"")) {
                goodStr = goodStr.substring(1);
            }
            if (goodStr.endsWith("\"")) {
                goodStr = goodStr.substring(0, goodStr.length() - 2);
            }
            goodsCustomResponseList.add(JSONObject.parseObject(goodStr, SortGoodsCustomResponse.class));
        }
        List<ProductConfigResponse> list = JSONArray.parseArray(refreshConfig.getRibbonConfig(), ProductConfigResponse.class);
        Map<String, ProductConfigResponse> productConfigResponseMap = list.stream()
                .filter(productConfig -> new Date().after(productConfig.getStartTime()) && new Date().before(productConfig.getEndTime())).collect(Collectors.toMap(ProductConfigResponse::getSkuId, Function.identity()));
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
        String dateStr = refreshConfig.getActivityStartTime();
        Date date = DateUtil.parseDateTime(dateStr);
        return BaseResponse.success(new Date().after(date));
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
        for (String key:keyRequest.getKeys()) {
            if (allowKeyList.contains(key)) {
                configMap.put(key, SpringUtil.getBean(key));
            }
        }
        return BaseResponse.success(configMap);
    }

}
