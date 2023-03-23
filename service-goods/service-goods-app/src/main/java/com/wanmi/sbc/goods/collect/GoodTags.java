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

    @Autowired
    BookDetailTab bookDetailTab;

    @Autowired
    private GoodsTestCacheService goodsCacheService;

    public void doGoods(){

        List list = goodJpa.getGoodsList();

        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            doGoods(map);
        }

        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            bookDetailTab.doBook(map);    //非书tab
        }

    }

    public Map doGoods(String spu_id){

        Map goodMap = goodJpa.getGoodsMap(spu_id);

        doGoods(goodMap);

        return goodMap;
    }

    public void doGoods(Map goodMap){

        String spu_no = String.valueOf(goodMap.get("spu_no"));
        String spu_id = String.valueOf(goodMap.get("goods_id"));

    /*    String spu_id = "2c9a00ca86299cda01862a0163e60000";
        String spu_no = "P735546359";
        String sku_id = "2c9a009b86a5b1850186a6ae64c80004";
        String isbn   = "ISBN_C_T003";*/

        List allList = new ArrayList();

        //10. 大促标签
        //List tagList = goodJpa.getTagList(spu_id);
        List tagList = goodsCacheService.getTagList(spu_id);
        if(tagList !=null && tagList.size() > 0){
            allList.addAll(tagList);
        }

        //20. 榜单标签
        if(StringUtil.isNotBlank(spu_no)) {
            //List topList = bookJpa.getTopList(spu_no);
            List topList = goodsCacheService.getTopList(spu_no);
            if(topList!=null && topList.size() > 0){
                allList.addAll(topList);
            }
        }

        //90. 取商品上关联的静态标签，按标签优先级依次呈现
        //List staticList = goodJpa.getStaticList(spu_id);
        List staticList = goodsCacheService.getStaticList(spu_id);
        if(staticList!=null && staticList.size() > 0){
            allList.addAll(staticList);
        }

        Map map = new LinkedHashMap();
        map.put("isBook","no");
        map.put("tags",allList);

        setRedis_Tags(spu_id,map);
    }

    public void setRedis_Tags(String spu_id,Map map){

        //String json = JSONArray.parseArray(JSON.toJSONString(list)).toJSONString();
        String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();

        redisService.setString(RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_ID+":"+spu_id, json );

    }

    public String getRedis_Tags(String spu_id){
        String value = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_ID+":"+spu_id);
        if(DitaUtil.isBlank(value)){
            value = "{}";
        }
        return value;
    }

    public String getRedis_MarkingTags(String spu_id){
        String value = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_GOODS_MARKING_SKU_ID+":"+spu_id);
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

