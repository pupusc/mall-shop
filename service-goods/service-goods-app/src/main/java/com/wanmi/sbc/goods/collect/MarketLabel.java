package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.goods.collect.respository.BookRepository;
import com.wanmi.sbc.goods.collect.respository.MarketRepository;
import com.wanmi.sbc.goods.redis.RedisService;
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
    MarketRepository marketJpa;

    @Autowired
    RedisService redisService;

    @Autowired
    BookRepository bookJpa;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private BookDetailTab bookDetailTab;

    public void doMarket() {

        List list = marketJpa.getSkuList();

        //xx分xx秒
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            doData(map);
        }

    }

    public void doData(Map skuMap) {

        List allList = new ArrayList();

        String spu_id = String.valueOf(skuMap.get("spu_id"));
        String sku_id = String.valueOf(skuMap.get("sku_id"));

    /*    String spu_id = "2c9a00ca86299cda01862a0163e60000";
        String sku_id= "2c9a009b86a5b1850186a6ae64c80004";
        String spu_no = "P735546359";
        String isbn   = "ISBN_C_T003";*/

        //10.返积分、20.积分兑换 30.满减 40.满折 50.49包邮 60 榜单 70 大促标签 80其它标签
        //10.返积分
        //List pointList = marketJpa.getPointList(sku_id);
        List pointList = cacheService.getPointList(sku_id);
        if (pointList != null && pointList.size() > 0) {
            allList.addAll(pointList);
        }

        //20.积分兑换
        //List exList = marketJpa.getExchangeList(sku_id);
        List exList = cacheService.getExchangeList(sku_id);
        if (exList != null && exList.size() > 0) {
            allList.addAll(exList);
        }

        //30. 满减
        //List markingList1 = marketJpa.getMarking1List(sku_id);
        List markingList1 = cacheService.getMarking1List(sku_id);
        if (markingList1 != null && markingList1.size() > 0) {
            allList.addAll(markingList1);
        }

        //40. 满折
       // List markingList2 = marketJpa.getMarking2List(sku_id);
        List markingList2 = cacheService.getMarking2List(sku_id);
        if (markingList2 != null && markingList2.size() > 0) {
            allList.addAll(markingList2);
        }

        //50. 满49元包邮
        //List list49 = marketJpa.get49List(spu_id);
        List list49 = cacheService.get49List(spu_id);
        if (list49 != null && list49.size() > 0) {
            allList.addAll(list49);
        }


        //60.榜单
        //List topList = marketJpa.getTopList(sku_id);
         List topList = cacheService.getTopList(sku_id);
        if (topList != null && topList.size() > 0) {
            allList.addAll(topList);
        }


        //70. 大促标签
        //List tagList1 = marketJpa.getTagList1(spu_id);
        List tagList1 = cacheService.getTagList1(spu_id);
        if (tagList1 != null && tagList1.size() > 0) {
            allList.addAll(tagList1);
        }

        //80. 其它标签
        //List tagList2 = marketJpa.getTagList2(spu_id);
        List tagList2 = cacheService.getTagList2(spu_id);
        if (tagList2 != null && tagList2.size() > 0) {
            allList.addAll(tagList2);
        }

        Map map = new LinkedHashMap();
        map.put("name", "营销标签");
        //销量
        //String saleNum = bookDetailTab.getSaleNum_bySpuID(spu_id);
        //map.put("salenum", saleNum);

        //定价
        //String fix_price = bookDetailTab.getFixPrice(spu_id);
        //map.put("fix_price", fix_price);

        //是否显示积分全额抵扣（参加积分活动和加入黑名单中的商品不显示）
        //map.put("isShowIntegral", bookDetailTab.isShowIntegral(spu_id, sku_id));

        ///**通过sku_id得到分组评论**/
        //List commentList=bookDetailTab.comment(spu_id);
        //map.put("commentList",commentList);

        //int commentNum = bookDetailTab.getCommentNum(commentList);
        //map.put("commentNum",commentNum);

        map.put("labels", allList);

        setRedis_Label(spu_id, sku_id, map);

    }

    public void setRedis_Label(String spu_id, String sku_id, Map map) {

        //String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();
        String json = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);

        String old_json = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_GOODS_MARKING_SKU_ID + ":" + sku_id);
        if (!json.equals(old_json)) {
            redisService.setString(RedisTagsConstant.ELASTIC_SAVE_GOODS_MARKING_SKU_ID + ":" + sku_id, json);
            //String updateTime = DitaUtil.getCurrentAllDate();
            //bookJpa.updateGoodTime(updateTime,spu_id);
        }

    }

    public String getRedis_Tags(String sku_id) {
        String value = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_GOODS_MARKING_SKU_ID + ":" + sku_id);
        if (DitaUtil.isBlank(value)) {
            value = "{}";
        }
        return value;
    }
}

