package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.jpa.JpaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CacheService {

    @Autowired
    JpaManager jpaManager;


    //积分
    public static Map marketJpaPointMap = null;
    //其他标签
    public static Map marketTagMap = null;

    //49 标签
    public static Map marketTopMap = null;

    // 减满
    public static Map marketMarkingMap = null;

    // 减折
    public static Map marketMarking2Map = null;

    //49 满减
    public static Map market49Map = null;

    //49 满减
    public static Map marketTagList1Map = null;

    //bookJpa 榜单标签
    public static Map bookJpaTopMap = null;

    public static Map marketExchangeMap = null;


    //10. 返积分_缓存
    public void getPointList_init() {

        marketJpaPointMap = new HashMap();
        String currentTime = DitaUtil.getCurrentAllDate();

        String sql = " select a.name as activity_name,a.begin_time,a.end_time,b.sku_id,b.num,concat ('返',b.num, '积分') as name,10 as order_type from `sbc-marketing`.t_normal_activity a left join `sbc-marketing`.t_activity_point_sku b on a.id = b.normal_activity_id " +
                "                  where a.del_flag = 0 and a.publish_state = 1 " +
                "                  and a.begin_time <= ? and ? <= a.end_time " +
                "                  order by a.id desc,b.num desc  ";
        Object[] obj = new Object[]{currentTime, currentTime};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String sku_id = String.valueOf(map.get("sku_id"));
            if (DitaUtil.isNotBlank(sku_id)) {
                List tagList = (List) marketJpaPointMap.get(sku_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    marketJpaPointMap.put(sku_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //10. 积分
    public List getPointList(String sku_id) {
        if (marketJpaPointMap == null) {
            getPointList_init();
        }

        return (List) marketJpaPointMap.get(sku_id);
    }


    //80. 其它标签_缓存
    public void getTagList2_init() {

        marketTagMap = new HashMap();

        String sql = " select b.goods_id,a.id,a.name,show_status,is_static,80 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                "         where a.del_flag = 0 and a.is_static = 2 order by seq asc  ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);

        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_id = String.valueOf(map.get("goods_id"));
            if (DitaUtil.isNotBlank(goods_id)) {
                List tagList = (List) marketTagMap.get(goods_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    marketTagMap.put(goods_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //10. 其他标签
    public List getTagList2(String spu_id) {
        if (marketTagMap == null) {
            getTagList2_init();
        }

        return (List) marketTagMap.get(spu_id);
    }


    //60. 榜单标签
    public List getTopList_init() {
        marketTopMap = new HashMap();

        String sql = "  select c.goods_info_id,b.id,concat (a.name,'第', b.order_num,'名') as show_name,a.name as name,c.goods_info_name,b.order_num,60 as order_type from t_book_list_model a left join t_book_list_goods_publish b on a.id = b.book_list_id " +
                "               left join goods_info c on b.sku_no = c.goods_info_no " +
                "               where a.del_flag = 0 and b.del_flag = 0 and c.del_flag = 0 " +
                "               order by b.order_num desc ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_info_id = String.valueOf(map.get("goods_info_id"));
            if (DitaUtil.isNotBlank(goods_info_id)) {
                List tagList = (List) marketTopMap.get(goods_info_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    marketTopMap.put(goods_info_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
        return list;
    }

    //60.  榜单标签
    public List getTopList(String spu_id) {
        if (marketTopMap == null) {
            getTopList_init();
        }

        return (List) marketTopMap.get(spu_id);
    }


    //30. 满减
    public List getMarking1List_init() {
        marketMarkingMap = new HashMap();
        String currentTime = DitaUtil.getCurrentAllDate();

        String sql = " select b.scope_id as sku_id,a.marketing_id,a.marketing_name,a.begin_time,a.end_time,'满减' as name,30 as order_type from `sbc-marketing`.marketing a left join `sbc-marketing`.marketing_scope b on a.marketing_id = b.marketing_id " +
                "    where a.del_flag = 0 and a.marketing_type = 0 and a.is_pause = 0 " +
                "    and a.begin_time <= ? and ? <= a.end_time " +
                "    order by a.marketing_id desc ";

        Object[] obj = new Object[]{currentTime, currentTime};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String sku_id = String.valueOf(map.get("sku_id"));
            if (DitaUtil.isNotBlank(sku_id)) {
                List tagList = (List) marketMarkingMap.get(sku_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    marketMarkingMap.put(sku_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
        return list;
    }

    //10.  减满
    public List getMarking1List(String sku_id) {
        if (marketMarkingMap == null) {
            getMarking1List_init();
        }

        return (List) marketMarkingMap.get(sku_id);
    }


    //40. 满折
    public void getMarking2List_init() {

        marketMarking2Map = new HashMap();
        String currentTime = DitaUtil.getCurrentAllDate();

        String sql = " select b.scope_id as sku_id ,a.marketing_id,a.marketing_name,a.begin_time,a.end_time,'满折' as name,40 as order_type from `sbc-marketing`.marketing a left join `sbc-marketing`.marketing_scope b on a.marketing_id = b.marketing_id " +
                "                      where a.del_flag = 0 and a.marketing_type = 1 and a.is_pause = 0 " +
                "                      and a.begin_time <= ? and ? <= a.end_time " +
                "                      order by a.marketing_id desc ";


        Object[] obj = new Object[]{currentTime, currentTime};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String sku_id = String.valueOf(map.get("sku_id"));
            if (DitaUtil.isNotBlank(sku_id)) {
                List tagList = (List) marketMarking2Map.get(sku_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    marketMarking2Map.put(sku_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //满折
    public List getMarking2List(String spu_id) {
        if (marketMarking2Map == null) {
            getMarking2List_init();
        }

        return (List) marketMarking2Map.get(spu_id);
    }

    //50. 满49元包邮
    public void get49List_init() {
        market49Map = new HashMap();

        String sql = " select a.goods_id,a.freight_temp_id,'满49元包邮' as name,50 as order_type from goods a left join freight_template_goods b on a.freight_temp_id = b.freight_temp_id " +
                " where  b.freight_temp_name = '满49元包邮模板'";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_id = String.valueOf(map.get("goods_id"));
            if (DitaUtil.isNotBlank(goods_id)) {
                List tagList = (List) market49Map.get(goods_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    market49Map.put(goods_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //满49包邮
    public List get49List(String spu_id) {
        if (market49Map == null) {
            get49List_init();
        }

        return (List) market49Map.get(spu_id);
    }

    //60. 大促标签
    public void getTagList1_init() {
        marketTagList1Map = new HashMap();
        String sql = "  select b.goods_id,a.id,a.name,show_img,show_status,is_static,70 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                "                      where  a.del_flag = 0 and a.is_static = 1 order by seq asc  ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_id = String.valueOf(map.get("goods_id"));
            if (DitaUtil.isNotBlank(goods_id)) {
                List tagList = (List) marketTagList1Map.get(goods_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    marketTagList1Map.put(goods_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //大促商标
    public List getTagList1(String spu_id) {
        if (marketTagList1Map == null) {
            getTagList1_init();
        }

        return (List) marketTagList1Map.get(spu_id);
    }

    //20. 积分兑换
    public List getExchangeList_init() {
        marketExchangeMap=new HashMap();
        String currentTime = DitaUtil.getCurrentAllDate();

        String sql = " select b.scope_id as sku_id, a.marketing_id,a.marketing_name,a.begin_time,a.end_time,c.point_need as num,concat (c.point_need, '积分兑换') as name,20 as order_type " +
                "  from `sbc-marketing`.marketing a left join `sbc-marketing`.marketing_scope b on a.marketing_id = b.marketing_id " +
                "  left join `sbc-marketing`.marketing_point_buy_level c on a.marketing_id = c.marketing_id " +
                "  where a.del_flag = 0 and a.marketing_type = 8 and a.is_pause = 0  " +
                "  and a.begin_time <= ? and ? <= a.end_time ";
        Object[] obj = new Object[]{currentTime,currentTime};

        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String sku_id = String.valueOf(map.get("sku_id"));
            if (DitaUtil.isNotBlank(sku_id)) {
                List tagList = (List) marketExchangeMap.get(sku_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    marketExchangeMap.put(sku_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
        return list;
    }

    public List getExchangeList(String sku_id) {
        if (marketExchangeMap == null) {
            getExchangeList_init();
        }

        return (List) marketExchangeMap.get(sku_id);
    }

    public void clear() {

        marketJpaPointMap = null;
        marketTagMap = null;
        marketTopMap = null;
        marketMarkingMap = null;
        marketMarking2Map = null;
        market49Map = null;
        marketTagList1Map = null;
        marketExchangeMap = null;
    }

}
