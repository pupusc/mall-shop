package com.wanmi.sbc.system.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import com.wanmi.sbc.setting.bean.enums.SettingRedisKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品EXCEL处理服务
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
     * 未开启商品抵扣时，清零buyPoint
     * @param goodses
     */
    public void clearBuyPointsForEsSpu(List<EsGoodsVO> goodses){
        if(!isGoodsPoint()){
            goodses.forEach(g -> {
                g.setBuyPoint(0L);
                if(CollectionUtils.isNotEmpty(g.getGoodsInfos())) {
                    g.getGoodsInfos().forEach(k -> k.setBuyPoint(0L));
                }
            });
        }
    }

    /**
     * 未开启商品抵扣时，清零buyPoint
     * @param skus
     */
    public void clearBuyPoinsForEsSku(List<EsGoodsInfoVO> skus){
        Long sum = skus.stream().filter(g -> g.getGoodsInfo()!= null && g.getGoodsInfo().getBuyPoint() != null
                && g.getGoodsInfo().getBuyPoint() > 0).map(EsGoodsInfoVO::getGoodsInfo).mapToLong(GoodsInfoNestVO::getBuyPoint).sum();
        if(sum>0 && (!isGoodsPoint())){
            skus.forEach(g -> g.getGoodsInfo().setBuyPoint(0L));
        }
    }

    /**
     * 积分开关开启 并且 积分使用方式是商品抵扣
     * @return
     */
    public boolean isGoodsPoint(){
        SystemPointsConfigQueryResponse response = querySettingCache();
        return EnableStatus.ENABLE.equals(response.getStatus()) && PointsUsageFlag.GOODS.equals(response.getPointsUsageFlag());
    }
}