package com.wanmi.sbc.goods.common;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import com.wanmi.sbc.setting.bean.enums.SettingRedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品积分处理服务
 * Created by dyt on 2017/8/17.
 */
@Slf4j
@Service
public class SystemPointsConfigService {


    @Autowired
    private SystemPointsConfigQueryProvider systemPointsConfigQueryProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 查询设置缓存
     */
    private SystemPointsConfigQueryResponse querySettingCache() {
        String key = SettingRedisKey.SYSTEM_POINTS_CONFIG.toValue();
        boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            return JSONObject.parseObject(redisService.getString(key), SystemPointsConfigQueryResponse.class);
        }
        SystemPointsConfigQueryResponse response =systemPointsConfigQueryProvider.querySystemPointsConfig().getContext();
        redisService.setString(key, JSONObject.toJSONString(response));
        return response;
    }

    /**
     * 积分开关开启 并且 积分使用方式是商品抵扣
     * @return
     */
    public boolean isGoodsPoint(){
        SystemPointsConfigQueryResponse response = querySettingCache();
        return EnableStatus.ENABLE.equals(response.getStatus()) && PointsUsageFlag.GOODS.equals(response.getPointsUsageFlag());
    }

    /**
     * 未开启商品抵扣时，清零buyPoint
     * @param spus
     */
    public void clearBuyPoinsForSpus(List<GoodsVO> spus){
        if(spus == null){
            return;
        }
        Long sum = spus.stream().filter(g -> g.getBuyPoint() != null && g.getBuyPoint() > 0)
                .mapToLong(GoodsVO::getBuyPoint).sum();
        if(sum>0 && (!isGoodsPoint())){
            spus.forEach(g -> g.setBuyPoint(0L));
        }
    }

    /**
     * 未开启商品抵扣时，清零buyPoint
     * @param skus
     */
    public void clearBuyPoinsForSkus(List<GoodsInfoVO> skus){
        if(skus == null){
            return;
        }
        Long sum = skus.stream().filter(g -> g.getBuyPoint() != null && g.getBuyPoint() > 0)
                .mapToLong(GoodsInfoVO::getBuyPoint).sum();
        if(sum>0 && (!isGoodsPoint())){
            skus.forEach(g -> g.setBuyPoint(0L));
        }
    }

    /**
     * 未开启商品抵扣时，清零buyPoint
     * @param sku
     */
    public void clearBuyPoinsForSku(GoodsInfoVO sku){
        if(sku!= null && sku.getBuyPoint() != null && sku.getBuyPoint()>0 && (!isGoodsPoint())){
            sku.setBuyPoint(0L);
        }
    }


}