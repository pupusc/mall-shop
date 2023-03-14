package com.wanmi.sbc.goods.collect.respository;

import com.wanmi.sbc.goods.jpa.JpaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        Map skuMap = new HashMap();

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
            if(list1 !=null && list1.size()>0) {
                skuMap = (Map) list1.get(0);
            }
        }
        return skuMap;
    }


    /**通过sku_id取定价**/
    public Map getfixPricebySku(String skuId) {
        Map map = new HashMap();

        String sql = " select sales_num,fix_price from goods_info where goods_info_id = ? and del_flag = 0";
        Object[] obj = new Object[]{skuId};
        List list = jpaManager.queryForList(sql,obj);
        if(list !=null && list.size() >0){
            map = (Map)list.get(0);
        }
        return map;
    }

    /**通过spu_id取定价**/
    public Map getfixPricebySpu(String spuId) {
        Map map = new HashMap();

        String sql = " select goods_id,prop_value as fix_price from goods_prop_detail_rel where goods_id = ? and prop_id = 4";
        Object[] obj = new Object[]{spuId};
        List list = jpaManager.queryForList(sql,obj);
        if(list !=null && list.size() >0){
            map = (Map)list.get(0);
        }
        return map;
    }


    /**通过book_id取关键词和推荐图书**/
    public Map getKeyRecommend(String bookId) {
        Map map = new HashMap();
        /* meta_book_relation_book:推荐图书    isbn,bookName 图书名称
           meta_book_relation 关联推荐 name ：主标题   sub_name 副标题  开始时间  结束时间
           meta_book_relation_key 搜索关键词  keyName：关键词  */
        String sql = " select a.book_id,a.id,a.name,a.sub_name,a.start_time,a.end_time,b.isbn,b.name as bookName,c.name as keyName from meta_book_relation a " +
                " LEFT JOIN meta_book_relation_book b ON b.relation_id=a.id " +
                " LEFT JOIN meta_book_relation_key c ON c.relation_id=a.id " +
                " WHERE book_id=? and a.del_flag = 0 AND b.del_flag=0 AND c.del_flag=0 order by a.order_num,b.order_num,c.order_num asc limit 0,1";
        Object[] obj = new Object[]{bookId};
        List list = jpaManager.queryForList(sql,obj);
        if(list !=null && list.size() >0){
            map = (Map)list.get(0);
        }
        return map;
    }

}

