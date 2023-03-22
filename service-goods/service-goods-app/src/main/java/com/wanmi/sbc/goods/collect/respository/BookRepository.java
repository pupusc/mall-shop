package com.wanmi.sbc.goods.collect.respository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.bookmeta.bo.MetaAwardBO;
import com.wanmi.sbc.goods.collect.CacheService;
import com.wanmi.sbc.goods.collect.DitaUtil;
import com.wanmi.sbc.goods.jpa.JpaManager;
import com.wanmi.sbc.goods.redis.RedisService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
//图书商品
public class BookRepository {

    @Autowired
    JpaManager jpaManager;

    @Autowired
    CacheService cacheService;

    //tab2 根据book_id得到关联推荐主副标题
    public List book_search_name(String book_id) {

        String sql = " select id,name,sub_name from meta_book_relation where book_id = ? and del_flag = 0 order by order_num asc limit 0,1 ";
        Object[] obj = new Object[]{book_id};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //根据book_id得到得到关键词
    public List book_search_key(String relation_id) {

        String sql = " select id,name from meta_book_relation_key where relation_id = ? and del_flag = 0 order by order_num asc ";
        Object[] obj = new Object[]{relation_id};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public List getTradeList(String trade_id) {

        String sql = " select id,name,name as show_name,100 as order_type from meta_trade where id = ? ";

        Object[] obj = new Object[]{trade_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public List getClumpList(String book_clump_id) {

        String sql = " select a.id,a.name,a.name as show_name,110 as order_type from meta_book_clump a " +
                     " left join meta_book b on a.id = b.book_clump_id " +
                     " where b.id = ? ";

        Object[] obj = new Object[]{book_clump_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public List getAgeList(Map goodMap) {

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

    public List getAwardList(String book_id) {

        String sql = " select id,name,name as show_name,30 as order_type from meta_award where id in (select biz_id from meta_book_rcmmd where book_id = ? and biz_type = 1) limit 0,1 ";

        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public List getAutherList(String book_id) {

        String sql = " select a.id,d.name,concat (d.name,'(', a.name,')')  as show_name,40 as order_type from meta_figure a left join meta_book_figure b on a.id = b.figure_id " +
                "left join meta_figure_award c on c.figure_id = a.id " +
                "left join meta_award d on d.id = c.award_id " +
                "where a.del_flag = 0 and b.del_flag = 0 and b.figure_type = 1 and b.book_id = ? and d.name is not null " +
                "order by b.id asc limit 0,1 ";

        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public List getMediaList(String book_id) {

        String sql = " select a.id,a.name,concat(a.name,'推荐') as show_name,60 as order_type from meta_figure a " +
                     "   left join meta_book_rcmmd b on a.id = b.biz_id " +
                     "   where b.book_id = ? and b.biz_type in (3,4,5) and b.del_flag = 0 " +
                     "   order by b.id asc limit 0,1 ";

        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public List getNameList(String book_id) {

        String sql = " select id,name,name as show_name,70 as order_type from meta_figure where id in  " +
                     " (select biz_id from meta_book_rcmmd where book_id = ? and biz_type = 8) ";

        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过isbn查找book_id
    public Map getBookMap(String isbn) {

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
    public List findSpuByV2(String isbn) {
        String spu_no = null;

        String sql = " select a.goods_no as spu,b.prop_value as isbn,a.goods_id as spu_id from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id " +
                     " where b.prop_id = 5 " +
                     "   and b.prop_value = ? ";
        Object[] obj = new Object[]{isbn};
        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    //通过isbn查找spu
    public Map findSpuByV3(String isbn) {
        Map goodsMap = null;

        String sql = " select a.goods_no as spu_no,b.prop_value as isbn,a.goods_name,a.goods_id as spu_id from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id " +
                " where b.prop_id = 5 " +
                "   and b.prop_value = ? limit 0,1 ";
        Object[] obj = new Object[]{isbn};
        List list = jpaManager.queryForList(sql,obj);
        if(list !=null && list.size() >0){
            goodsMap = (Map)list.get(0);
        }
        return goodsMap;
    }

    //通过spu查找isbn
    public String findIsbnBy(String spu) {
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
    public List getTagList(String book_id) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,10 as order_type from meta_label a left join meta_book_label b on a.id = b.label_id " +
                     " where b.book_id = ? and a.del_flag = 0 and a.is_static = 1 order by seq asc ";
        Object[] obj = new Object[]{book_id};
        List list = jpaManager.queryForList(sql,obj);
        return list;
    }

    public List getStaticList(String book_id) {

        String sql = " select a.id,a.name,show_img,show_status,is_static,a.name as show_name,90 as order_type from meta_label a left join meta_book_label b on a.id = b.label_id " +
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

        String sql = " select b.id,concat (a.name,'第', b.order_num,'名') as show_name,a.name as name,c.goods_info_name,b.order_num,20 as order_type from t_book_list_model a left join t_book_list_goods_publish b on a.id = b.book_list_id " +
                " left join goods_info c on b.sku_no = c.goods_info_no " +
                " where a.del_flag = 0 and b.del_flag = 0 and c.del_flag = 0 " +
                " and b.spu_no=? order by b.order_num desc limit 0,1 ";
        Object[] obj = new Object[]{spu_no};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }


    public List getBookList(){

        String sql = "select * from meta_book where del_flag = 0";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        return list;

    }

    //所有商品
    public List getGoodsList_v2(){

        String sql = " select a.goods_no as spu,b.prop_value as isbn from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id where b.prop_id = 5 ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        return list;

    }

    //所有商品,spu和isbn关联并且不为空
    public List getGoodsList(){

       /* String sql = " select a.goods_no as spu,b.prop_value as isbn,c.id,a.goods_id as spu_id from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id " +
                     " left join meta_book c on b.prop_value = c.isbn " +
                     " where b.prop_id = 5 and c.id is not null and a.del_flag=0 and b.del_flag=0 "; */

        String sql = "select a.goods_no as spu,b.prop_value as isbn,c.id,a.goods_id as spu_id from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id \n" +
                " left join meta_book c on b.prop_value = c.isbn " +
                " where b.prop_id = 5 and a.del_flag=0 and b.del_flag=0   ";

        //and c.id = 7836
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);

        return list;

    }

    //所有商品,spu和isbn关联并且不为空
    public Map getGoodsMap(String isbn){

        String sql = " select a.goods_no as spu,b.prop_value as isbn,c.id,a.goods_id as spu_id from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id " +
                " left join meta_book c on b.prop_value = c.isbn " +
                " where b.prop_id = 5 and c.id is not null and c.isbn = ? ";

        //and c.id = 7836
        Object[] obj = new Object[]{isbn};
        Map ret = jpaManager.queryForMap(sql,obj);

        return ret;

    }

    //通过spu_no更新
    public void updateGoodTimeByNo(String updateTime,String spu_no) {
        String sql = "update goods set update_time = ? where goods_no = ?";
        Object[] obj = new Object[]{updateTime,spu_no};
        jpaManager.update(sql,obj);
    }

    //通过spu_id更新
    public void updateGoodTime(String updateTime,String spu_id) {
        String sql = "update goods set update_time = ? where goods_id = ?";
        Object[] obj = new Object[]{updateTime,spu_id};
        jpaManager.update(sql,obj);
    }

    //讲稿中提到的其他书籍
    public List getOther(String book_id) {

        String sql = " select a.id as book_id, name as book_name,a.isbn from meta_book a left join meta_book_rcmmd b on a.id = b.biz_id " +
                     " where b.book_id = ? and b.biz_type = 7 limit 0,5 ";
        Object[] obj = new Object[]{book_id};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }


    //图书简介的第一位作家
    public List getFirstWriter(String bookId, String figureType) {

        String sql = " SELECT m.id,m.name,m.introduce,c.figure_type from meta_figure m, meta_book_figure c " +
                " where m.del_flag = 0 and m.id = c.figure_id and c.book_id = ? and c.del_flag = 0 and c.figure_type = ? limit 0,1 ";
        Object[] obj = new Object[]{bookId, figureType};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //获得的奖项
    public List getWriterAwards(String writerId) {

        String sql = " select a.id,a.name from meta_award a left join meta_figure_award b on a.id = b.award_id " +
                " where b.figure_id = ? ";
        Object[] obj = new Object[]{writerId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //查询作家其它的书
    public List getWriterBooks(String bookId, String writerId) {

        String sql = " select b.book_id,a.isbn from meta_book a left join meta_book_figure b on a.id = b.book_id " +
                " where b.figure_id = ? and book_id != ? limit 0,5 ";
        Object[] obj = new Object[]{writerId, bookId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //简介目录原文摘要
    public List getContent(String bookId) {

        String sql = " select content,type from meta_book_content where book_id = ? and type in (1,2,3) ";
        Object[] obj = new Object[]{bookId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //书中提到人物显示
    public List getCharacters(String bookId) {

        String sql = " SELECT id,type,name,introduce from meta_figure where del_flag = 0 " +
                " and id in ( select id from meta_figure where id in (select biz_id from meta_book_rcmmd where book_id = ? and biz_type = 8)) ";
        Object[] obj = new Object[]{bookId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过book_id得到丛书名称
    public List getLibraryName(String bookId) {

        String sql = " select a.id,a.name from meta_book_clump a left join meta_book b on a.id = b.book_clump_id " +
                " where b.id = ? ";
        Object[] obj = new Object[]{bookId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过book_id得到丛书名称得到总数
    public List getLibraryNum(String libraryId) {

        String sql = " select count(*) as num from meta_book where book_clump_id = ? ";

        Object[] obj = new Object[]{libraryId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过book_id得到丛书名称,且不包含这本书
    public List getLibrary(String bookId, String libraryId) {

        String sql = " select a.id,a.name,a.isbn from meta_book a left join meta_book_rcmmd b on a.book_clump_id = b.id " +
                " where a.book_clump_id = ? and a.id !=? limit 0,5 ";

        Object[] obj = new Object[]{libraryId, bookId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过book_id得到出品方名称
    public List getProducerName(String bookId) {

        String sql = " select a.id,a.name from meta_producer a left join meta_book b on a.id = b.producer_id " +
                " where b.id = ? ";
        Object[] obj = new Object[]{bookId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过book_id得到出品方名称得到总数
    public List getProducerNum(String producerId) {

        String sql = " select count(*) as num from meta_book where producer_id = ? ";

        Object[] obj = new Object[]{producerId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过book_id得到出品方名称,且不包含这本书
    public List getProducer(String bookId, String producerId) {

        String sql = " select a.id,a.name,a.isbn from meta_book a left join meta_producer b on a.producer_id = b.id " +
                " where a.producer_id = ? and a.id != ? limit 0,5 ";

        Object[] obj = new Object[]{producerId, bookId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }


    //推荐人列表
    public List RcommdFigureByBookId(String bookId) {

        String sql = " select a.*,b.name,b.job_title from meta_book_rcmmd as a left join meta_figure as b on a.biz_id = b.id where a.book_id=? and a.is_selected=1 and a.del_flag=0 ";

        Object[] obj = new Object[]{bookId};

        List list = jpaManager.queryForList(sql,obj);

        return list;
    }

    //通过id查询获奖名称
    public List<Map> queryAwardById(int biz_id) {
        String sql = "select id, name, image, create_time, update_time, del_flag, descr from meta_award where id = ? and del_flag = 0";
        Object[] obj = new Object[]{biz_id};

        List<Map> list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public List<String> RcommdBookByFigureId(int biz_id, String book_id) {
        String sql = " select b.isbn from meta_book_rcmmd as a left join meta_book as b on a.book_id = b.id where a.biz_id=? and a.book_id !=? and a.is_selected=1 and a.del_flag=0 and b.del_flag=0 limit 0,5";
        Object[] obj = new Object[]{biz_id,book_id};

        List<Map> list = jpaManager.queryForList(sql,obj);

        if(null!=list && list.size()!=0) {
            List<String> isbn = list.stream().map(map -> {
                return map.get("isbn").toString();
            }).collect(Collectors.toList());
            return isbn;
        }else {
            return null;
        }
    }

    public List<Map> goodsInfoByIsbns(List<String> isbnList) {
        if (null == isbnList || isbnList.size() == 0) {
            return null;
        }
        String sql = "select * from goods_info where del_flag='0' and isbn_no in (";
        Object[] obj = new Object[isbnList.size()];
        int size = isbnList.size();
        for (int i = 0; i < size; i++){
            if (i==size-1){
                sql=sql+"? )";
                obj[i] = isbnList.get(i);
            }else {
                sql = sql + "? ,";
                obj[i] = isbnList.get(i);
            }
       }
        List<Map> list = jpaManager.queryForList(sql,obj);
        return list;
    }

    public  List<Map> getSaleNum(String goods_id) {
        String sql = "select sku_id,sale_num,rank_text from t_book_list_goods_publish where sku_id = ? and del_flag = 0";
        Object[] obj = new Object[]{goods_id};

        List<Map> list = jpaManager.queryForList(sql,obj);

        return list;

    }
     //根据商品sku 查询销量
    public  List<Map> getSkuSaleNum(String goods_id) {
        String sql = " select sales_num from goods_info where goods_info_id = ? and del_flag = 0 ";
        Object[] obj = new Object[]{goods_id};

        List<Map> list = jpaManager.queryForList(sql,obj);

        return list;

    }

    public List<Map> getComentPointV2(String spu_id) {
        String sql = "select sku_id,sale_num,rank_text from t_book_list_goods_publish where sku_id = ? and del_flag = 0";
        Object[] obj = new Object[]{spu_id};

        List<Map> list = jpaManager.queryForList(sql,obj);

        return list;
    }

    public List<Map> getBookRecommend(String isbn_id) {
        String sql = " SELECT * FROM (SELECT mb.score, mb.id FROM meta_book mb WHERE mb.isbn = ? ) a LEFT JOIN (SELECT mbr.descr, mbr.book_id, CASE  " +
                " mbr.biz_type WHEN 5 THEN 0 WHEN 3 THEN 1 WHEN 2 THEN 2 WHEN 4 THEN 3 WHEN 10 THEN 4 ELSE 5 END sorting, mf.name, mf.job_title FROM  " +
                " meta_book_rcmmd mbr, meta_figure mf WHERE mbr.biz_id= mf.id and mbr.biz_type in (2,3,4,5,9,10) and mbr.is_selected = 1 ORDER BY sorting) b ON a.id = b.book_id ";
        Object[] obj = new Object[]{isbn_id};

        List<Map> list = jpaManager.queryForList(sql,obj);

        return list;
    }
}

