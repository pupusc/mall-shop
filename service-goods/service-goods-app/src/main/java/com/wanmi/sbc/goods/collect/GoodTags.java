package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.goods.collect.respository.BookRepository;
import com.wanmi.sbc.goods.collect.respository.GoodRepository;
import com.wanmi.sbc.goods.jpa.JpaManager;
import com.wanmi.sbc.goods.redis.RedisService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service

//非书商品
public class GoodTags {

    @Autowired
    GoodRepository goodJpa;
    @Autowired
    BookRepository bookJpa;

    @Autowired
    RedisService redisService;

    @Autowired
    BookTags bookTags;

    public void doGoods(){

        List list = goodJpa.getGoodsList();

        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            doGoods(map);
        }

    }

    public void doGoods(Map goodMap){

        String spu_no = String.valueOf(goodMap.get("spu_no"));

        //spu_no = "P989359460";

        List allList = new ArrayList();

        //10. 大促标签
        List tagList = goodJpa.getTagList(spu_no);
        if(tagList !=null && tagList.size() > 0){
            allList.addAll(tagList);
        }

        //20. 榜单标签
        if(StringUtil.isNotBlank(spu_no)) {
            List topList = bookJpa.getTopList(spu_no);
            if(topList!=null && topList.size() > 0){
                allList.addAll(topList);
            }
        }

        //90. 取商品上关联的静态标签，按标签优先级依次呈现
        List staticList = goodJpa.getStaticList(spu_no);
        if(staticList!=null && staticList.size() > 0){
            allList.addAll(staticList);
        }

        Map map = new LinkedHashMap();
        map.put("isBook","no");
        map.put("tags",allList);

        setRedis_Tags(spu_no,map);
    }

    public void setRedis_Tags(String spu_no,Map map){

        //String json = JSONArray.parseArray(JSON.toJSONString(list)).toJSONString();
        String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();

        redisService.setString(RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_NO+":"+spu_no, json );

    }

    public String getRedis_Tags(String spu_no){
        String value = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_NO+":"+spu_no);
        if(DitaUtil.isBlank(value)){
            value = "{}";
        }
        return value;
    }

    public String getRedis_Book(String spu_id){
        String value = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID+":"+spu_id);
        if(DitaUtil.isBlank(value)){
            value = "{}";
        }
        return value;
    }

}

