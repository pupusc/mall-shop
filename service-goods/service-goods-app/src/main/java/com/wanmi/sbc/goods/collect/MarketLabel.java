package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.goods.collect.respository.BookRepository;
import com.wanmi.sbc.goods.collect.respository.GoodRepository;
import com.wanmi.sbc.goods.redis.RedisService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
//营销标签
public class MarketLabel {

    @Autowired
    GoodRepository goodJpa;

    public List doMarket(Map goodMap){
        List list = new ArrayList();
        String spu_id = String.valueOf(goodMap.get("spu_id"));

        if(DitaUtil.isBlank(spu_id)){
            return list;
        }



        return list;
    }

}

