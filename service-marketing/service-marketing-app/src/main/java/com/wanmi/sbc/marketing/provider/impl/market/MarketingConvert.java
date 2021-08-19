package com.wanmi.sbc.marketing.provider.impl.market;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangLingKe
 * @Description: 对象转换工具类
 * @Date: 2018-11-21 14:43
 */
public class MarketingConvert {


    /**
     * 参数转化
     * @param map
     * @return
     */
    public static HashMap<String, List<MarketingForEndVO>> marketingRes2MarketingForEnd(Map<String,
            List<MarketingResponse>> map){

        HashMap<String, List<MarketingForEndVO>> stringListMap = new HashMap<>();

        map.entrySet().stream().forEach(goodsMap->{
            stringListMap.put(goodsMap.getKey(), KsBeanUtil.convert(goodsMap.getValue(),MarketingForEndVO.class));
        });

        return stringListMap;
    }

}
