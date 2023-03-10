package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.jpa.JpaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookCacheService {

    @Autowired
    JpaManager jpaManager;

    public static Map bookMap = null;

    public static Map bookTagMap = null;
    public static Map bookTopMap = null;
    public static Map bookAwardMap = null;
    public static Map bookAutherMap = null;
    public static Map bookMediaMap = null;
    public static Map bookNameMap = null;
    public static Map bookStaticMap = null;
    public static Map bookTradeMap = null;
    public static Map bookClumpMap = null;


    //通过isbn查找book_id缓存
    public void getBookMap_init() {

        bookMap = new HashMap();

        String sql = " select id,isbn,trade_id from meta_book where del_flag=0 ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String isbn = String.valueOf(map.get("isbn"));
            if (DitaUtil.isNotBlank(isbn)) {
                Map oldMap = (Map) bookMap.get(isbn);
                if (oldMap == null || oldMap.size() == 0) {       //不存在就放入
                    bookMap.put(isbn, map);
                }
            }

        }
    }

    //通过isbn查找book_id缓存
    public Map getBookMap_cache(String isbn) {
        if (bookMap == null) {
            getBookMap_init();
        }

        return (Map) bookMap.get(isbn);
    }


    //10. 大促标签_缓存
    public void getTagList_init() {

        bookTagMap = new HashMap();

        String sql = " select a.id,a.name,show_img,show_status,is_static,b.book_id,10 as order_type from meta_label a left join meta_book_label b on a.id = b.label_id " +
                " where a.del_flag = 0 and a.is_static = 1 order by seq asc ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookTagMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookTagMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }

    }

    //10. 大促标签
    public List getTagList_cache(String book_id) {
        if (bookTagMap == null) {
            getTagList_init();
        }

        return (List) bookTagMap.get(book_id);
    }
    //榜单标签
    public void getTopList_init() {
        bookTopMap=new HashMap();
        String sql = " select b.id,concat (a.name,'第', b.order_num,'名') as show_name,a.name as name,b.sku_no,c.goods_info_name,b.spu_no,b.order_num,20 as order_type from t_book_list_model a left join t_book_list_goods_publish b on a.id = b.book_list_id " +
                " left join goods_info c on b.sku_no = c.goods_info_no " +
                " where a.del_flag = 0 and b.del_flag = 0 and c.del_flag = 0 " +
                " order by b.order_num desc  ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String spu_no = String.valueOf(map.get("spu_no"));

            if (DitaUtil.isNotBlank(spu_no)) {
                List tagList = (List) bookTopMap.get(spu_no);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookTopMap.put(spu_no, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }

    }

    //10. 榜单标签
    public List getTopList(String spu_no) {
        if (bookTopMap == null) {
            getTopList_init();
        }

        return (List) bookTopMap.get(spu_no);
    }


    public void getAwardList_init() {
        bookAwardMap=new HashMap();
        //String sql = " select id,name,name as show_name,30 as order_type from meta_award where id in (select biz_id from meta_book_rcmmd where  biz_type = 1) limit 0,1 ";
        String sql = " select b.book_id,a.id,a.name,a.name as show_name,30 as order_type from meta_award a LEFT JOIN meta_book_rcmmd b on b.biz_id=a.id where b.biz_type = 1";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookAwardMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookAwardMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }

    }

    //30. 书本身有奖项，显示第一个奖项名称
    public List getAwardList(String book_id) {
        if (bookAwardMap == null) {
            getAwardList_init();
        }

        return (List) bookAwardMap.get(book_id);
    }


    public List getAutherList_init() {
        bookAutherMap=new HashMap();
        String sql = " select a.id,b.book_id,d.name,concat (d.name,'(', a.name,')')  as show_name,40 as order_type from meta_figure a left join meta_book_figure b on a.id = b.figure_id " +
                "left join meta_figure_award c on c.figure_id = a.id " +
                "left join meta_award d on d.id = c.award_id " +
                "where a.del_flag = 0 and b.del_flag = 0 and b.figure_type = 1 and  d.name is not null " +
                "order by b.id asc ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookAutherMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookAutherMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }
        return list;
    }

    //40. 图书作者有获奖，显示『奖项名称+获得者（作者）』
    public List getAutherList(String book_id) {
        if (bookAutherMap == null) {
            getAutherList_init();
        }

        return (List) bookAutherMap.get(book_id);
    }


    public void getMediaList_init() {

        bookMediaMap=new HashMap();

        String sql = " select b.book_id, a.id,a.name,concat(a.name,'推荐') as show_name,60 as order_type from meta_figure a " +
                "   left join meta_book_rcmmd b on a.id = b.biz_id " +
                "   where  b.biz_type in (3,4,5) and b.del_flag = 0 " +
                "   order by b.id asc  ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookMediaMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookMediaMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }

    }

    //60. 有图书库-推荐信息，显示『X位名家，X家媒体，X家专业机构推荐』
    public List getMediaList(String book_id) {
        if (bookMediaMap == null) {
            getMediaList_init();
        }

        return (List) bookMediaMap.get(book_id);
    }

    public void getNameList_init() {

        bookNameMap =new HashMap();

        /*String sql = " select id,name,name as show_name,70 as order_type from meta_figure where id in  " +
                " (select biz_id from meta_book_rcmmd where  biz_type = 8) ";*/
        String sql = "select b.book_id,a.id,name,a.name as show_name,70 as order_type from meta_figure a LEFT JOIN meta_book_rcmmd b ON a.id=b.biz_id where  b.biz_type = 8";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookNameMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookNameMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }
    }

    public List getNameList(String book_id) {
        if (bookNameMap == null) {
            getNameList_init();
        }

        return (List) bookNameMap.get(book_id);
    }

    public void getStaticList_init() {
        bookStaticMap=new HashMap();
        String sql = " select b.book_id,a.id,a.name,show_img,show_status,is_static,a.name as show_name,90 as order_type from meta_label a left join meta_book_label b on a.id = b.label_id " +
                " where  a.del_flag = 0 and a.is_static = 2 order by seq asc ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookStaticMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookStaticMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }

    }
    //90. 适读对象：当数对像有数据，则全量显示(先取静态标签)
    public List getStaticList(String book_id) {
        if (bookStaticMap == null) {
            getStaticList_init();
        }

        return (List) bookStaticMap.get(book_id);
    }


    public void getTradeList_init() {
        bookTradeMap=new HashMap();
        String sql = " select id,name,name as show_name,100 as order_type from meta_trade  ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String id = String.valueOf(map.get("id"));

            if (DitaUtil.isNotBlank(id)) {
                List tagList = (List) bookTradeMap.get(id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookTradeMap.put(id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }
    }

    //100. 行业类类目：（本次新加字段），显示图书所在行业类目，按类目树结构显示，一级名称>二级名称>三级名称
    public List getTradeList(String trade_id) {
        if (bookTradeMap == null) {
            getTradeList_init();
        }

        return (List) bookTradeMap.get(trade_id);
    }

    public void getClumpList_init() {
        bookClumpMap=new HashMap();
        String sql = " select b.id as book_id, a.id,a.name,a.name as show_name,110 as order_type from meta_book_clump a " +
                " left join meta_book b on a.id = b.book_clump_id ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookClumpMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookClumpMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }
    }

    public List getClumpList(String book_id) {
        if (bookClumpMap == null) {
            getClumpList_init();
        }

        return (List) bookClumpMap.get(book_id);
    }

    public void clear() {
        bookMap = null;
        bookTagMap=null;
        bookTopMap = null;
        bookAwardMap=null;
        bookAutherMap=null;
        bookMediaMap=null;
        bookNameMap=null;
        bookStaticMap=null;
        bookTradeMap=null;
        bookClumpMap=null;
    }

}
