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
//营销标签
public class MarketRepository {

    @Autowired
    JpaManager jpaManager;

    //所有商品sku_id,b.stock > 0 也查出没有库存的商品
    public List getSkuList(){

        String sql = " select b.goods_id as spu_id,b.goods_info_id as sku_id,b.goods_info_no as sku_no from goods a left join goods_info b on a.goods_id = b.goods_id " +
                     "  where a.del_flag = 0 and b.del_flag = 0 and b.stock > 0 ";
        //             "  limit 0,1 ";
        // and b.goods_info_id = '2c9a009b86a5b1850186a6ae64c80004'
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);

        return list;

    }

    //10. 返积分
    public List getPointList(String sku_id) {

        String currentTime = DitaUtil.getCurrentAllDate();

        String sql = " select a.name as activity_name,a.begin_time,a.end_time,b.sku_id,b.spu_id,b.num,concat ('返',b.num, '积分') as name,10 as order_type from `sbc-marketing`.t_normal_activity a left join `sbc-marketing`.t_activity_point_sku b on a.id = b.normal_activity_id " +
                     " where a.del_flag = 0 and a.publish_state = 1 " +
                     " and a.begin_time <= ? and ? <= a.end_time and b.sku_id = ? " +
                     " order by a.id desc,b.num desc limit 0,1 ";
        Object[] obj = new Object[]{currentTime,currentTime,sku_id};
        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    //20. 榜单标签
    public List getTopList(String sku_id) {

        String sql = " select b.id,c.goods_info_id,concat (a.name,'第', b.order_num,'名') as show_name,a.name as name,b.sku_no,c.goods_info_name,b.spu_no,b.order_num,20 as order_type from t_book_list_model a left join t_book_list_goods_publish b on a.id = b.book_list_id " +
                     " left join goods_info c on b.sku_no = c.goods_info_no " +
                     " where a.del_flag = 0 and b.del_flag = 0 and c.del_flag = 0 " +
                     " and c.goods_info_id = ? order by b.order_num desc limit 0,1  ";
        Object[] obj = new Object[]{sku_id};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }


    //30. 满减
    public List getMarking1List(String sku_id) {

        String currentTime = DitaUtil.getCurrentAllDate();

        String sql = " select a.marketing_id,a.marketing_name,a.begin_time,a.end_time,b.scope_id as sku_id,'满减' as name,30 as order_type from `sbc-marketing`.marketing a left join `sbc-marketing`.marketing_scope b on a.marketing_id = b.marketing_id " +
                     " where a.del_flag = 0 and a.marketing_type = 0 and a.is_pause = 0 " +
                     " and a.begin_time <= ? and ? <= a.end_time " +
                     " and b.scope_id = ? " +
                     " order by a.marketing_id desc limit 0,1 ";

        Object[] obj = new Object[]{currentTime,currentTime,sku_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //40. 满折
    public List getMarking2List(String sku_id) {

        String currentTime = DitaUtil.getCurrentAllDate();

        String sql = " select a.marketing_id,a.marketing_name,a.begin_time,a.end_time,b.scope_id as sku_id,'满折' as name,40 as order_type from `sbc-marketing`.marketing a left join `sbc-marketing`.marketing_scope b on a.marketing_id = b.marketing_id " +
                     " where a.del_flag = 0 and a.marketing_type = 1 and a.is_pause = 0 " +
                     " and a.begin_time <= ? and ? <= a.end_time " +
                     " and b.scope_id = ? " +
                     " order by a.marketing_id desc limit 0,1 ";


        Object[] obj = new Object[]{currentTime,currentTime,sku_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //50. 满49元包邮
    public List get49List(String spu_id) {

        String currentTime = DitaUtil.getCurrentAllDate();

        String sql = " select a.freight_temp_id,'满49元包邮' as name,50 as order_type from goods a left join freight_template_goods b on a.freight_temp_id = b.freight_temp_id " +
                     " where a.goods_id = ? and b.freight_temp_name = '满49元包邮模板' ";


        Object[] obj = new Object[]{spu_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //60. 大促标签
    public List getTagList1(String spu_id) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,60 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                     " where b.goods_id = ? and a.del_flag = 0 and a.is_static = 1 order by seq asc  ";

        Object[] obj = new Object[]{spu_id};
        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    //70. 其它标签
    public List getTagList2(String book_id) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,70 as order_type from meta_label a left join meta_label_spu b on a.id = b.label_id " +
                " where b.goods_id = ? and a.del_flag = 0 and a.is_static = 2 order by seq asc  ";
        Object[] obj = new Object[]{book_id};

        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

}

