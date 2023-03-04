package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.goods.collect.respository.BookRepository;
import com.wanmi.sbc.goods.collect.respository.GoodRepository;
import com.wanmi.sbc.goods.collect.respository.MarketRepository;
import com.wanmi.sbc.goods.redis.RedisService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.jvm.hotspot.oops.Mark;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
//营销标签
public class MarketLabel {

    @Autowired
    MarketRepository marketJpa;

    @Autowired
    RedisService redisService;

    @Autowired
    BookRepository bookJpa;

    public void doMarket(){

        List list = marketJpa.getSkuList();

        //xx分xx秒
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            doData(map);
        }

    }

    private void doData(Map skuMap) {

        List allList = new ArrayList();

        String spu_id = String.valueOf(skuMap.get("spu_id"));
        String sku_id = String.valueOf(skuMap.get("sku_id"));

        //spu_id 2c9a00ca86299cda01862a0163e60000
        //sku_id 2c9a009b86a5b1850186a6ae64c80004

        //10.积分
        List pointList = marketJpa.getPointList(sku_id);
        if(pointList !=null && pointList.size() > 0){
            allList.addAll(pointList);
        }

        //20.榜单
        List topList = marketJpa.getTopList(sku_id);
        if(topList !=null && topList.size() > 0){
            allList.addAll(topList);
        }

        //30. 满减
        List markingList1 = marketJpa.getMarking1List(sku_id);
        if(markingList1 !=null && markingList1.size() > 0){
            allList.addAll(markingList1);
        }

        //40. 满减
        List markingList2 = marketJpa.getMarking2List(sku_id);
        if(markingList2 !=null && markingList2.size() > 0){
            allList.addAll(markingList2);
        }

        //50. 满49元包邮
        List list49 = marketJpa.get49List(spu_id);
        if(list49 !=null && list49.size() > 0){
            allList.addAll(list49);
        }

        //60. 大促标签
        List tagList1 = marketJpa.getTagList1(spu_id);
        if(tagList1 !=null && tagList1.size() > 0){
            allList.addAll(tagList1);
        }

        List tagList2 = marketJpa.getTagList2(spu_id);
        if(tagList2 !=null && tagList2.size() > 0){
            allList.addAll(tagList2);
        }

        Map map = new LinkedHashMap();
        map.put("name","营销标签");
        map.put("labels",allList);

        setRedis_Label(spu_id,sku_id,map);

    }

    public void setRedis_Label(String spu_id,String sku_id,Map map){

        //String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();
        String json = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);

        String old_json = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_GOODS_MARKING_SKU_ID + ":" + sku_id);
        if(!json.equals(old_json)){
            redisService.setString(RedisTagsConstant.ELASTIC_SAVE_GOODS_MARKING_SKU_ID+":" + sku_id, json );
            String updateTime = DitaUtil.getCurrentAllDate();
            bookJpa.updateGoodTime(updateTime,spu_id);
        }

    }
}

