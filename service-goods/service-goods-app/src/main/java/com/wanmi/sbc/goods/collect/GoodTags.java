package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    JpaManager jpaManager;

    @Autowired
    RedisService redisService;

    @Autowired
    BookTags bookTags;

    public void doGoods(){

        List list = getGoodsList();

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
        List tagList = getTagList(spu_no);
        if(tagList !=null && tagList.size() > 0){
            allList.addAll(tagList);
        }

        //20. 榜单标签
        if(StringUtil.isNotBlank(spu_no)) {
            List topList = bookTags.getTopList(spu_no);
            if(topList!=null && topList.size() > 0){
                allList.addAll(topList);
            }
        }

        //90. 取商品上关联的静态标签，按标签优先级依次呈现
        List staticList = getStaticList(spu_no);
        if(staticList!=null && staticList.size() > 0){
            allList.addAll(staticList);
        }

        Map map = new LinkedHashMap();
        map.put("isBook","no");
        map.put("tags",allList);

        setRedis(spu_no,map);
    }

    public void setRedis(String spu_no,Map map){

        //String json = JSONArray.parseArray(JSON.toJSONString(list)).toJSONString();
        String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();

        redisService.setString(RedisTagsConstant.ELASTIC_TAGS_GOODS_KEY_SPU+":"+spu_no, json );

    }

    public String getRedis(String spu_no){
        String value = redisService.getString(RedisTagsConstant.ELASTIC_TAGS_GOODS_KEY_SPU+":"+spu_no);
        return value;
    }

    //非书商品
    private List getGoodsList(){

        String sql = " select * from ( " +
                     " select a.goods_id,a.goods_no as spu,concat (b.cate_path, b.cate_id) as cate_path  from goods a left join goods_cate b on a.cate_id = b.cate_id " +
                     " where a.del_flag = 0 and b.del_flag = 0 and a.added_flag in (1,2) " +
                     " )c where cate_path not like '%1190%' ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        return list;

    }

    //10. 大促标签
    private List getTagList(String spu_no) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,10 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                     " where a.del_flag = 0 and a.is_static = 1 and b.goods_no = ? order by order_num ASC ";
        Object[] obj = new Object[]{spu_no};

        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    private List getStaticList(String spu_no) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,90 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                     " where a.del_flag = 0 and a.is_static = 2 and b.goods_no = ? order by order_num ASC ";
        Object[] obj = new Object[]{spu_no};

        List list = jpaManager.queryForList(sql,obj);
        return list;
    }



}

