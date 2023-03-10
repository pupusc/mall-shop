package com.wanmi.sbc.goods.collect.respository;

import com.wanmi.sbc.goods.collect.DitaUtil;
import com.wanmi.sbc.goods.jpa.JpaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
//图书商品
public class GoodRepository {

    @Autowired
    JpaManager jpaManager;

    //非书商品
    public List getGoodsList(){

        String sql = " select * from ( " +
                " select a.goods_id,a.goods_no as spu_no,concat (b.cate_path, b.cate_id) as cate_path  from goods a left join goods_cate b on a.cate_id = b.cate_id " +
                " where a.del_flag = 0 and b.del_flag = 0 and a.added_flag in (1,2) " +
                " )c where cate_path not like '%1190%' ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        return list;

    }

    //10. 大促标签
    public List getTagList(String spu_id) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,10 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                " where a.del_flag = 0 and a.is_static = 1 and b.goods_id = ? order by order_num ASC ";
        Object[] obj = new Object[]{spu_id};

        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    public List getStaticList(String spu_id) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,90 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                " where a.del_flag = 0 and a.is_static = 2 and b.goods_id = ? order by order_num ASC ";
        Object[] obj = new Object[]{spu_id};

        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    //图文详情
    public List getGoodsDetail(String spu_no) {
        String sql = " select goods_detail from goods where goods_no = ? ";
        Object[] obj = new Object[]{spu_no};

        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    //订购须知
    public Map getOrderDetail(String spuId) {
        Map map = null;

        String sql = " select order_show_type, order_detail from goods where goods_id = ? " +
                " and del_flag = 0 ";
        Object[] obj = new Object[]{spuId};
        List list = jpaManager.queryForList(sql,obj);
        if(list !=null && list.size() >0){
            map = (Map)list.get(0);
        }
        return map;
    }

    //根据spu获取sku
    public Map getSkuBySpuId(String spu_id) {
        Map skuMap = null;

        String sql = " select goods_info_id,goods_info_img,goods_info_name,market_price from goods_info where goods_id = ? and stock > 0 " +
                " and del_flag = 0 order by market_price asc limit 1 ";
        Object[] obj = new Object[]{spu_id};
        List list = jpaManager.queryForList(sql,obj);
        if(list !=null && list.size() >0){
            skuMap = (Map)list.get(0);
        }else{
            String sql1 = " select goods_info_id,goods_info_img,goods_info_name,market_price from goods_info where goods_id = ?" +
                    " and del_flag = 0 order by market_price asc limit 1 ";
            List list1 = jpaManager.queryForList(sql1,obj);
            skuMap = (Map)list1.get(0);
        }
        return skuMap;
    }

}

