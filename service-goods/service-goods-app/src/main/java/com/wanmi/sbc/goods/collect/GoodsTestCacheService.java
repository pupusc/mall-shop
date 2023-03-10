package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.jpa.JpaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsTestCacheService {

    @Autowired
    JpaManager jpaManager;

    public static Map GoodsTagMap = null;

    public static Map GoodsTopMap = null;

    public static Map GoodsStaticMap = null;






    //大促标签
     public List getTagList(String spu_id) {
        if (GoodsTagMap == null) {
            getTagList_init();
        }

        return (List) GoodsTagMap.get(spu_id);
    }


    //10. 大促标签
    public void getTagList_init() {

        GoodsTagMap=new HashMap();

        String sql = " select b.goods_id,a.id,a.name,show_img,show_status,is_static,10 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                " where a.del_flag = 0 and a.is_static = 1  order by order_num ASC ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_id = String.valueOf(map.get("goods_id"));

            if (DitaUtil.isNotBlank(goods_id)) {
                List tagList = (List) GoodsTagMap.get(goods_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    GoodsTagMap.put(goods_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }

    }

    //10. 榜单标签
    public List getTopList(String spu_no) {
        if (GoodsTopMap == null) {
            getTopList_init();
        }

        return (List) GoodsTopMap.get(spu_no);
    }

    public void getTopList_init() {
        GoodsTopMap=new HashMap();
        String sql = " select b.id,concat (a.name,'第', b.order_num,'名') as show_name,a.name as name,b.sku_no,c.goods_info_name,b.spu_no,b.order_num,20 as order_type from t_book_list_model a left join t_book_list_goods_publish b on a.id = b.book_list_id " +
                " left join goods_info c on b.sku_no = c.goods_info_no " +
                " where a.del_flag = 0 and b.del_flag = 0 and c.del_flag = 0 " +
                " order by b.order_num desc ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String spu_no = String.valueOf(map.get("spu_no"));

            if (DitaUtil.isNotBlank(spu_no)) {
                List tagList = (List) GoodsTopMap.get(spu_no);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    GoodsTopMap.put(spu_no, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }


    //90. 取商品上关联的静态标签，按标签优先级依次呈现
    public List getStaticList(String spu_id) {
        if (GoodsStaticMap == null) {
            getStaticList_init();
        }

        return (List) GoodsStaticMap.get(spu_id);
    }


    public void getStaticList_init() {
        GoodsStaticMap=new HashMap();
        String sql = " select b.goods_id, a.id,a.name,show_img,show_status,is_static,90 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                " where a.del_flag = 0 and a.is_static = 2  order by order_num ASC ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_id = String.valueOf(map.get("goods_id"));

            if (DitaUtil.isNotBlank(goods_id)) {
                List tagList = (List) GoodsStaticMap.get(goods_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    GoodsStaticMap.put(goods_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    public void clear() {
        GoodsTagMap = null;
        GoodsTopMap = null;
        GoodsStaticMap=null;
       
    }

}
