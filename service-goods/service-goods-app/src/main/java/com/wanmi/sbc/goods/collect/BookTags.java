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
//图书商品
public class BookTags {

    @Autowired
    JpaManager jpaManager;

    @Autowired
    RedisService redisService;

    public void doGoods(){

        List list = getGoodsList();

        //7分41秒
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            doBook(map);
        }

    }

    public void doBook(Map goodMap){

        String spu_no = String.valueOf(goodMap.get("spu"));
        String isbn = String.valueOf(goodMap.get("isbn"));

        //spu_no = "P735546359";
        //isbn   = "ISBN_C_T003";

        Map bookMap = getBookMap(isbn);

        if(bookMap == null || bookMap.size() == 0){
            return;
        }

        String book_id = String.valueOf(bookMap.get("id"));
        String trade_id = String.valueOf(bookMap.get("trade_id"));

        List allList = new ArrayList();

        //10. 大促标签
        List tagList1 = getTagList(book_id);
        if(tagList1!=null && tagList1.size() > 0){
            allList.addAll(tagList1);
        }

        //20. 榜单标签
        if(StringUtil.isNotBlank(spu_no)) {
            List topList = getTopList(spu_no);
            if(topList!=null && topList.size() > 0){
                allList.addAll(topList);
            }
        }

        //30. 书本身有奖项，显示第一个奖项名称
        List awardList = getAwardList(book_id);
        if(awardList!=null && awardList.size() > 0){
            allList.addAll(awardList);
        }

        //40. 图书作者有获奖，显示『奖项名称+获得者（作者）』
        List authorList = getAutherList(book_id);
        if(authorList!=null && authorList.size() > 0){
            allList.addAll(authorList);
        }

        //50. 当有指定的打标媒体、名家、专业机构推荐时，显示『媒体名称/名家名称/专业机构名称推荐』
        //List mediaList = getMediaListList(book_id);
        //if(mediaList!=null && mediaList.size() > 0){
        //    allList.addAll(mediaList);
        //}

        //60. 有图书库-推荐信息，显示『X位名家，X家媒体，X家专业机构推荐』
        List mediaList = getMediaList(book_id);
        if(mediaList!=null && mediaList.size() > 0){
            allList.addAll(mediaList);
        }

        //70. 书中提到的人物，有数据则显示：人物名称
        List nameList = getNameList(book_id);
        if(nameList!=null && nameList.size() > 0){
            allList.addAll(nameList);
        }

        //80. 图书本身最小年龄段、最大年龄段有数据，显示数字，X~Y岁，当任意一项没有对应显示为空
        List ageList = getAgeList(goodMap);
        if(ageList!=null && ageList.size() > 0){
            allList.addAll(ageList);
        }

        //90. 适读对象：当数对像有数据，则全量显示(先取静态标签)
        List staticList = getStaticList(book_id);
        if(staticList!=null && staticList.size() > 0){
            allList.addAll(staticList);
        }

        //100. 行业类类目：（本次新加字段），显示图书所在行业类目，按类目树结构显示，一级名称>二级名称>三级名称
        if(DitaUtil.isNotBlank(trade_id)){
            List tradeList = getTradeList(trade_id);
            if(tradeList!=null && tradeList.size() > 0){
                allList.addAll(tradeList);
            }
        }


        //110. 图书被包含在某丛书，显示「丛书」名称
        List clumpList = getClumpList(book_id);
        if(clumpList!=null && clumpList.size() > 0){
            allList.addAll(clumpList);
        }

        //120. 标签：一级分类=？？下显示3个图书库中关联优先级最高的标签

        Map map = new LinkedHashMap();
        map.put("isBook","yes");
        map.put("tags",allList);

        setRedis(spu_no,map);
    }

    private List getTradeList(String trade_id) {

        String sql = " select id,name,name as show_name,100 as order_type from meta_trade where id = ? ";

        Object[] obj = new Object[]{trade_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }


    private List getClumpList(String book_clump_id) {

        String sql = " select a.id,a.name,a.name as show_name,110 as order_type from meta_book_clump a " +
                     " left join meta_book b on a.id = b.book_clump_id " +
                     " where b.id = ? ";

        Object[] obj = new Object[]{book_clump_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    private List getAgeList(Map goodMap) {

        List ageList = new ArrayList();

        String fit_age_min = String.valueOf(goodMap.get("fit_age_min"));        //'最小阅读年龄'
        String fit_age_max = String.valueOf(goodMap.get("fit_age_max"));        //'最大阅读年龄'

        if(DitaUtil.isNotBlank(fit_age_min) && DitaUtil.isNotBlank(fit_age_max)){
            Map map = new HashMap();
            String name = fit_age_min + "~" + fit_age_max + "岁";
            map.put("name",name);
            map.put("show_name",name);
            map.put("order_type",80);
        }

        return ageList;

    }

    private List getAwardList(String book_id) {

        String sql = " select id,name,name as show_name,30 as order_type from meta_award where id in (select biz_id from meta_book_rcmmd where book_id = ? and biz_type = 1) limit 0,1 ";

        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    private List getAutherList(String book_id) {

        String sql = " select a.id,d.name,concat (d.name,'(', a.name,')')  as show_name,40 as order_type from meta_figure a left join meta_book_figure b on a.id = b.figure_id " +
                "left join meta_figure_award c on c.figure_id = a.id " +
                "left join meta_award d on d.id = c.award_id " +
                "where a.del_flag = 0 and b.del_flag = 0 and b.figure_type = 1 and b.book_id = ? and d.name is not null " +
                "order by b.id asc limit 0,1 ";

        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    private List getMediaList(String book_id) {

        String sql = " select a.id,a.name,concat(a.name,'推荐') as show_name,60 as order_type from meta_figure a " +
                     "   left join meta_book_rcmmd b on a.id = b.biz_id " +
                     "   where b.book_id = ? and b.biz_type in (3,4,5) and b.del_flag = 0 " +
                     "   order by b.id asc limit 0,1 ";

        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    private List getNameList(String book_id) {

        String sql = " select id,name,name as show_name,70 as order_type from meta_figure where id in  " +
                     " (select biz_id from meta_book_rcmmd where book_id = ? and biz_type = 8) ";

        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过isbn查找book_id
    private Map getBookMap(String isbn) {

        Map bookMap = null;

        String sql = " select * from meta_book where isbn = ? ";

        Object[] obj = new Object[]{isbn};
        List list = jpaManager.queryForList(sql,obj);
        if(list != null && list.size() >0 ){
            bookMap = (Map)list.get(0);
        }

        return bookMap;
    }

    //通过isbn查找spu
    private List findSpuBy(String isbn) {
        String spu_no = null;

        String sql = " select a.goods_no,b.prop_value as isbn from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id " +
                     " where b.prop_id = 5 " +
                     "   and b.prop_value = ? ";
        Object[] obj = new Object[]{isbn};
        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    //通过spu查找isbn
    private String findIsbnBy(String spu) {
        String isbn = null;

        String sql = " select a.goods_no,b.prop_value as isbn from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id " +
                     " where b.prop_id = 5 " +
                     "   and a.goods_no = ? ";
        Object[] obj = new Object[]{spu};
        List list = jpaManager.queryForList(sql,obj);

        if(list != null && list.size() >0){
            Map map = (Map)list.get(0);
            isbn = String.valueOf(map.get("isbn"));
        }
        return isbn;
    }

    //10. 大促标签
    private List getTagList(String book_id) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,10 as order_type from meta_label a left join meta_book_label b on a.id = b.label_id " +
                     " where b.book_id = ? and a.del_flag = 0 and a.is_static = 1 order by seq asc ";
        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    private List getStaticList(String book_id) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,90 as order_type from meta_label a left join meta_book_label b on a.id = b.label_id " +
                " where b.book_id = ? and a.del_flag = 0 and a.is_static = 2 order by seq asc ";
        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //20. 榜单标签
    //select b.id,concat (a.name,'第', b.order_num,'名') as name,a.name as top_name,b.sku_no,c.goods_info_name,b.spu_no,b.order_num from t_book_list_model a left join t_book_list_goods_publish b on a.id = b.book_list_id
    //left join goods_info c on b.sku_no = c.goods_info_no
    //where a.del_flag = 0 and b.del_flag = 0 and c.del_flag = 0
    //and b.spu_no='P735546359'
    //order by b.order_num desc limit 0,1
    public List getTopList(String spu_no) {

        String sql = " select b.id,concat (a.name,'第', b.order_num,'名') as show_name,a.name as name,b.sku_no,c.goods_info_name,b.spu_no,b.order_num,20 as order_type from t_book_list_model a left join t_book_list_goods_publish b on a.id = b.book_list_id " +
                " left join goods_info c on b.sku_no = c.goods_info_no " +
                " where a.del_flag = 0 and b.del_flag = 0 and c.del_flag = 0 " +
                " and b.spu_no=? order by b.order_num desc limit 0,1 ";
        Object[] obj = new Object[]{spu_no};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public void setRedis(String spu_no,Map map){

        //String json = JSONArray.parseArray(JSON.toJSONString(list)).toJSONString();
        String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();

        redisService.setString(RedisTagsConstant.ELASTIC_TAGS_GOODS_KEY_SPU+":"+spu_no, json );
    }


    private List getBookList(){

        String sql = "select * from meta_book where del_flag = 0";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        return list;

    }

    //所有商品
    private List getGoodsList_v2(){

        String sql = " select a.goods_no as spu,b.prop_value as isbn from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id where b.prop_id = 5 ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        return list;

    }

    //所有商品,spu和isbn关联并且不为空
    private List getGoodsList(){

        String sql = " select a.goods_no as spu,b.prop_value as isbn,c.id from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id " +
                     " left join meta_book c on b.prop_value = c.isbn " +
                     " where b.prop_id = 5 and c.id is not null and a.del_flag=0 and c.del_flag=0 and b.del_flag=0 ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);

        return list;

    }

}

