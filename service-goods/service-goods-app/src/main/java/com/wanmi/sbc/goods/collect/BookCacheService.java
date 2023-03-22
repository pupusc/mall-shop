package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.jpa.JpaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public static Map bookSearchNameMap = null;
    public static Map bookRcommdFigureMap = null;
    public static Map bookSearchKeyMap = null;
    public static Map bookOtherMap = null;
    public static Map bookpuByV3Map = null;
    public static Map bookSkuBySpuMap = null;
    public static Map bookAwardByIdMap = null;
    public static Map bookFirstWriterMap = null;
    public static Map bookWriterAwardsMap = null;
    public static Map bookWriterBooksMap = null;
    public static Map bookCommentSkuMap = null;
    public static Map bookContentMap = null;
    public static Map bookGoodsDetailMap = null;
    public static Map RcommdBookByFigureIdMap = null;
    public static Map KeyRecommendMap = null;
    public static Map getCharactersMap = null;
    public static Map getPublicBookIdMap = null;


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
        bookTopMap = new HashMap();
        String sql = " select b.id,concat (a.name,'第', b.order_num,'名') as show_name,a.name as name,b.sku_no,c.goods_info_name,b.spu_no,b.order_num,20 as order_type from t_book_list_model a left join t_book_list_goods_publish b on a.id = b.book_list_id " +
                " left join goods_info c on b.sku_no = c.goods_info_no " +
                " where a.del_flag = 0 and b.del_flag = 0 and c.del_flag = 0 " +
                " order by b.order_num desc  ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
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
        bookAwardMap = new HashMap();
        //String sql = " select id,name,name as show_name,30 as order_type from meta_award where id in (select biz_id from meta_book_rcmmd where  biz_type = 1) limit 0,1 ";
        String sql = " select b.book_id,a.id,a.name,a.name as show_name,30 as order_type from meta_award a LEFT JOIN meta_book_rcmmd b on b.biz_id=a.id where b.biz_type = 1";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
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
        bookAutherMap = new HashMap();
        String sql = " select a.id,b.book_id,d.name,concat (d.name,'(', a.name,')')  as show_name,40 as order_type from meta_figure a left join meta_book_figure b on a.id = b.figure_id " +
                "left join meta_figure_award c on c.figure_id = a.id " +
                "left join meta_award d on d.id = c.award_id " +
                "where a.del_flag = 0 and b.del_flag = 0 and b.figure_type = 1 and  d.name is not null " +
                "order by b.id asc ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
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

        bookMediaMap = new HashMap();

        String sql = " select b.book_id, a.id,a.name,concat(a.name,'推荐') as show_name,60 as order_type from meta_figure a " +
                "   left join meta_book_rcmmd b on a.id = b.biz_id " +
                "   where  b.biz_type in (3,4,5) and b.del_flag = 0 " +
                "   order by b.id asc  ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
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

        bookNameMap = new HashMap();

        /*String sql = " select id,name,name as show_name,70 as order_type from meta_figure where id in  " +
                " (select biz_id from meta_book_rcmmd where  biz_type = 8) ";*/
        String sql = "select b.book_id,a.id,name,a.name as show_name,70 as order_type from meta_figure a LEFT JOIN meta_book_rcmmd b ON a.id=b.biz_id where  b.biz_type = 8";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
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
        bookStaticMap = new HashMap();
        String sql = " select b.book_id,a.id,a.name,show_img,show_status,is_static,a.name as show_name,90 as order_type from meta_label a left join meta_book_label b on a.id = b.label_id " +
                " where  a.del_flag = 0 and a.is_static = 2 order by seq asc ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
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
        bookTradeMap = new HashMap();
        String sql = " select id,name,name as show_name,100 as order_type from meta_trade  ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
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
        bookClumpMap = new HashMap();
        String sql = " select b.id as book_id, a.id,a.name,a.name as show_name,110 as order_type from meta_book_clump a " +
                " left join meta_book b on a.id = b.book_clump_id ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
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

    //tab2 根据book_id得到关联推荐主副标题
    public void book_search_name_init() {

        bookSearchNameMap = new HashMap();
        //String sql = " select id,name,sub_name from meta_book_relation where book_id = ? and del_flag = 0 order by order_num asc limit 0,1 ";
        String sql = " select book_id,id,name,sub_name from meta_book_relation where  del_flag = 0 order by order_num asc ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookSearchNameMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookSearchNameMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }
    }


    //推荐内容~关键词
    public List book_search_name(String book_id) {
        if (bookSearchNameMap == null) {
            book_search_name_init();
        }

        return (List) bookSearchNameMap.get(book_id);
    }

    //推荐人列表
    public void RcommdFigureByBookId_init() {
        bookRcommdFigureMap = new HashMap();
        String sql = " select a.*,b.name,b.job_title from meta_book_rcmmd as a left join meta_figure as b on a.biz_id = b.id where  a.is_selected=1 and a.del_flag=0 ";

        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookRcommdFigureMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookRcommdFigureMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }

    }

    //推荐内容~关键词
    public List RcommdFigureByBookId(String book_id) {
        if (bookRcommdFigureMap == null) {
            RcommdFigureByBookId_init();
        }

        return (List) bookRcommdFigureMap.get(book_id);
    }

    //根据book_id得到得到关键词
    public void book_search_key_init() {

        bookSearchKeyMap = new HashMap();
        String sql = " select id,name,relation_id from meta_book_relation_key where  del_flag = 0 order by order_num asc ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String relation_id = String.valueOf(map.get("relation_id"));

            if (DitaUtil.isNotBlank(relation_id)) {
                List tagList = (List) bookSearchKeyMap.get(relation_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookSearchKeyMap.put(relation_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }
    }

    //根据book_id得到得到关键词

    public List book_search_key(String book_id) {
        if (bookSearchKeyMap == null) {
            book_search_key_init();
        }

        return (List) bookSearchKeyMap.get(book_id);
    }


    //讲稿中提到的其他书籍
    public void getOther_init() {
        bookOtherMap = new HashMap();
        String sql = " select a.id as book_id,b.book_id as id, name as book_name,a.isbn from meta_book a left join meta_book_rcmmd b on a.id = b.biz_id " +
                " where  b.biz_type = 7 ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String id = String.valueOf(map.get("id"));

            if (DitaUtil.isNotBlank(id)) {
                List tagList = (List) bookOtherMap.get(id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookOtherMap.put(id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }
    }


    //讲稿中提到的其他书籍
    public List getOther(String book_id) {
        if (bookOtherMap == null) {
            getOther_init();
        }

        return (List) bookOtherMap.get(book_id);
    }

    public void findSpuByV3_init() {

        bookpuByV3Map = new HashMap();
        String sql = " select a.goods_no as spu_no,b.prop_value as isbn,a.goods_name,a.goods_id as spu_id from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id " +
                " where b.prop_id = 5 ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);

        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String isbn = String.valueOf(map.get("isbn"));

            if (DitaUtil.isNotBlank(isbn)) {
                List tagList = (List) bookpuByV3Map.get(isbn);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookpuByV3Map.put(isbn, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }

        }

    }

    //讲稿中提到的其他书籍
    public Map findSpuByV3(String isbn) {
        if (bookpuByV3Map == null) {
            findSpuByV3_init();
        }
        Map map = new HashMap();
        List list = (List) bookpuByV3Map.get(isbn);
        if (list != null && list.size() > 0) {
            map = (Map) list.get(0);
        }

        return map;
    }


    //根据spu获取sku
    public void getSkuBySpuId_init() {

        bookSkuBySpuMap = new HashMap();

        String sql = " select goods_id as spu_id ,goods_info_id,goods_info_img,goods_info_name,market_price from goods_info where  stock > 0 " +
                " and del_flag = 0 order by market_price asc ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                String spu_id = String.valueOf(map.get("spu_id"));
                if (DitaUtil.isNotBlank(spu_id)) {
                    List tagList = (List) bookSkuBySpuMap.get(spu_id);
                    if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                        tagList = new ArrayList();
                        tagList.add(map);
                        bookSkuBySpuMap.put(spu_id, tagList);
                    } else {
                        tagList.add(map);                             //存在放入
                    }
                }

            }

        } else {
            String sql1 = " select goods_id as spu_id,goods_info_id,goods_info_img,goods_info_name,market_price from goods_info where " +
                    "  del_flag = 0 order by market_price asc  ";
            List list1 = jpaManager.queryForList(sql1, obj);
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                String spu_id = String.valueOf(map.get("spu_id"));
                if (DitaUtil.isNotBlank(spu_id)) {
                    List tagList = (List) bookSkuBySpuMap.get(spu_id);
                    if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                        tagList = new ArrayList();
                        tagList.add(map);
                        bookSkuBySpuMap.put(spu_id, tagList);
                    } else {
                        tagList.add(map);                             //存在放入
                    }
                }

            }
        }
    }

    //根据spu获取sku
    public Map getSkuBySpuId(String spu_id) {

        Map map = new HashMap();
        if (bookSkuBySpuMap == null) {
            getSkuBySpuId_init();
        }
        List list = (List) bookSkuBySpuMap.get(spu_id);
        if (list != null && list.size() > 0) {
            map = (Map) list.get(0);
        }
        return map;
    }

    //通过id查询获奖名称
    public void queryAwardById_init() {

        bookAwardByIdMap = new HashMap();
        String sql = "select id, name, image, create_time, update_time, del_flag, descr from meta_award where  del_flag = 0";
        Object[] obj = new Object[]{};

        List<Map> list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String id = String.valueOf(map.get("id"));

            if (DitaUtil.isNotBlank(id)) {
                List tagList = (List) bookAwardByIdMap.get(id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookAwardByIdMap.put(id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //通过id查询获奖名称
    public List<Map> queryAwardById(String biz_id) {
        if (bookAwardByIdMap == null) {
            queryAwardById_init();
        }

        return (List<Map>) bookAwardByIdMap.get(biz_id);
    }


    //图书简介的第一位作家
    public void getFirstWriter_init() {
        bookFirstWriterMap = new HashMap();
        String sql = " SELECT m.id,m.name,m.introduce,c.figure_type,c.book_id from meta_figure m, meta_book_figure c " +
                " where m.del_flag = 0 and m.id = c.figure_id  and c.del_flag = 0  ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));
            String figure_type = String.valueOf(map.get("figure_type"));
            if (DitaUtil.isNotBlank(figure_type) && DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookFirstWriterMap.get(book_id + figure_type);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookFirstWriterMap.put(book_id + figure_type, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //图书简介的第一位作家
    public List getFirstWriter(String bookId, String figureType) {
        if (bookFirstWriterMap == null) {
            getFirstWriter_init();
        }

        return (List) bookFirstWriterMap.get(bookId + figureType);
    }


    //获得的奖项
    public void getWriterAwards_init() {
        bookWriterAwardsMap = new HashMap();
        String sql = " select a.id,a.name,b.figure_id from meta_award a left join meta_figure_award b on a.id = b.award_id ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String figure_id = String.valueOf(map.get("figure_id"));

            if (DitaUtil.isNotBlank(figure_id)) {
                List tagList = (List) bookWriterAwardsMap.get(figure_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookWriterAwardsMap.put(figure_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }

    }

    //图书简介的第一位作家
    public List getWriterAwards(String writerId) {
        if (bookWriterAwardsMap == null) {
            getWriterAwards_init();
        }

        return (List) bookWriterAwardsMap.get(writerId);
    }

    /**
     * 通过sku_id得到分组评论
     **/

    public void getCommentSkuId_init() {

        bookCommentSkuMap = new HashMap<>();
        String sql = "select b.goods_id,a.evaluate_content_key as comment_name,count(a.incr_id) as num from goods_evaluate_analyse a left join goods_evaluate b on a.evaluate_id = b.evaluate_id " +
                "  group by a.evaluate_content_key" +
                "  order by num desc  ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_id = String.valueOf(map.get("goods_id"));

            if (DitaUtil.isNotBlank(goods_id)) {
                List tagList = (List) bookCommentSkuMap.get(goods_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookCommentSkuMap.put(goods_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //通过sku_id得到分组评论
    public List getCommentSkuId(String spu_id) {
        if (bookCommentSkuMap == null) {
            getCommentSkuId_init();
        }

        return (List) bookCommentSkuMap.get(spu_id);
    }

    //简介目录原文摘要
    public List getContent_init() {
        bookContentMap = new HashMap();
        String sql = " select content,type,book_id from meta_book_content where  type in (1,2,3) ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) bookContentMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookContentMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
        return list;
    }

    public List getContent(String bookId) {
        if (bookContentMap == null) {
            getContent_init();
        }

        return (List) bookContentMap.get(bookId);
    }

    //图文详情
    public List getGoodsDetail_init() {

        bookGoodsDetailMap = new HashMap();

        String sql = " select goods_detail,goods_no from goods  ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_no = String.valueOf(map.get("goods_no"));

            if (DitaUtil.isNotBlank(goods_no)) {
                List tagList = (List) bookGoodsDetailMap.get(goods_no);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    bookGoodsDetailMap.put(goods_no, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
        return list;
    }

    public List getGoodsDetail(String spu_no) {
        if (bookGoodsDetailMap == null) {
            getGoodsDetail_init();
        }

        return (List) bookGoodsDetailMap.get(spu_no);
    }

    //说明这个推荐人有其他可推荐的
    public void RcommdBookByFigureId_init() {

        RcommdBookByFigureIdMap = new HashMap();
        //String sql = " select b.isbn from meta_book_rcmmd as a left join meta_book as b on a.book_id = b.id where a.biz_id=? and a.book_id !=? and a.is_selected=1 and a.del_flag=0 and b.del_flag=0 limit 0,5";
        String sql = "  select b.isbn,a.book_id,a.biz_id from meta_book_rcmmd as a left join meta_book as b on a.book_id = b.id where a.is_selected=1 and a.del_flag=0 and b.del_flag=0 ";
        Object[] obj = new Object[]{};
        List<Map> list = jpaManager.queryForList(sql, obj);

        //根据biz_id 查询出每个biz_id 对应的book_id 和isbn
        if (list != null && list.size() > 0) {
            for (Map map : list) {
                List<Map> ortherBookIdlist = new ArrayList<>();
                String bizId = String.valueOf(map.get("biz_id"));
                String bookId = String.valueOf(map.get("book_id"));

                if (DitaUtil.isNotBlank(bizId) && DitaUtil.isNotBlank(bookId)) {
                    //循环找到同biz_id下不包含book_id 的其他id
                    for (Map ortherMap : list) {
                        if (String.valueOf(ortherMap.get("biz_id")).equals(bizId) && String.valueOf(ortherMap.get("book_id")).equals(bookId)) {
                            ortherBookIdlist.add(ortherMap); //找到就放入获取isbn
                        }
                    }
                    //map 中获取value 值
                    List listBookId = (List) RcommdBookByFigureIdMap.get(bizId + bookId);
                    if (listBookId == null || listBookId.size() == 0) {       //不存在就新建一个，放入
                        RcommdBookByFigureIdMap.put(bizId + bookId, ortherBookIdlist);
                    }

                }

            }
        }

    }

    //tab1 推荐人有其他可推荐的
    public List<String> RcommdBookByFigureId(String biz_id, String book_id) {
        if (RcommdBookByFigureIdMap == null) {
            RcommdBookByFigureId_init();
        }
        List<Map> list = (List) RcommdBookByFigureIdMap.get(biz_id + book_id);
        if (null != list && list.size() != 0) {
            List<String> isbn = list.stream().map(map -> {
                return map.get("isbn").toString();
            }).collect(Collectors.toList());
            return isbn;
        } else {
            return null;
        }

    }


    /**
     * 通过book_id取关键词和推荐图书
     **/
    public void getKeyRecommend_init() {
        KeyRecommendMap = new HashMap();
        /* meta_book_relation_book:推荐图书    isbn,bookName 图书名称
           meta_book_relation 关联推荐 name ：主标题   sub_name 副标题  开始时间  结束时间
           meta_book_relation_key 搜索关键词  keyName：关键词  */
        String sql = " select a.book_id,a.id,a.name,a.sub_name,a.start_time,a.end_time,b.isbn,b.name as bookName,c.name as keyName from meta_book_relation a " +
                " LEFT JOIN meta_book_relation_book b ON b.relation_id=a.id " +
                " LEFT JOIN meta_book_relation_key c ON c.relation_id=a.id " +
                " WHERE  a.del_flag = 0 AND b.del_flag=0 AND c.del_flag=0 order by a.order_num,b.order_num,c.order_num asc ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) KeyRecommendMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    KeyRecommendMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }
    }

    //tab1 推荐人有其他可推荐的
    public Map getKeyRecommend(String bookId) {
        Map map = new HashMap();

        if (KeyRecommendMap == null) {
            getKeyRecommend_init();
        }
        List list = (List) KeyRecommendMap.get(bookId);
        if (list != null && list.size() > 0) {
            map = (Map) list.get(0);
        }

        return map;


    }


    //书中提到人物显示
    public void getCharacters_init() {


        getCharactersMap = new HashMap();
      /*  String sql = " SELECT id,type,name,introduce from meta_figure where del_flag = 0 " +
                " and id in ( select id from meta_figure where id in (select biz_id from meta_book_rcmmd where book_id = ? and biz_type = 8)) ";*/

        String sql = " SELECT b.book_id,  a.id,a.type,a.name ,a.introduce  from meta_figure a  left JOIN  meta_book_rcmmd  b on b.biz_id =a.id     where a.del_flag = 0 and b.biz_type=8  and b.del_flag=0  ";
        Object[] obj = new Object[]{};

        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String book_id = String.valueOf(map.get("book_id"));

            if (DitaUtil.isNotBlank(book_id)) {
                List tagList = (List) getCharactersMap.get(book_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    getCharactersMap.put(book_id, tagList);
                } else {
                    tagList.add(map);                             //存在放入
                }
            }
        }

    }

    public List getCharacters(String bookId) {

        if (getCharactersMap == null) {
            getCharacters_init();
        }


        return (List) getCharactersMap.get(bookId);


    }


    //订购须知
    public void getOrderDetail_init() {

        getCharactersMap = new HashMap();
        String sql = " select goods_id,order_show_type, order_detail from goods where del_flag = 0 ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql, obj);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String goods_id = String.valueOf(map.get("goods_id"));

            if (DitaUtil.isNotBlank(goods_id)) {
                List tagList = (List) getCharactersMap.get(goods_id);
                if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    map.remove(goods_id);
                    tagList.add(map);
                    getCharactersMap.put(goods_id, tagList);
                } else {
                    map.remove(goods_id);
                    tagList.add(map);                             //存在放入
                }
            }
        }

    }


    //订购须知
    public Map getOrderDetail(String spuId) {


        Map map = new HashMap();
        if (getCharactersMap == null) {
            getOrderDetail_init();
        }

        List list = (List) getCharactersMap.get(spuId);
        if (list != null && list.size() > 0) {
            map = (Map) list.get(0);
        }

        return map;

    }
          /* 通过book_id查询
        名家>媒体>编辑>机构 取推荐人职称、推荐语 */
        public void getPublicBookId_init() {

            getPublicBookIdMap=new HashMap<>();

            String sql = "select a.id,a.name as writerName,a.job_title,b.descr,b.book_id ,b.is_selected ,case b.biz_type when 5 then 0 when 3 then 1  when 2 then 2  when 4 then 3   when 9 then 4   when 10  then 5  end biz_type,c.score,c.isbn from meta_figure a " +
                    " left join meta_book_rcmmd b on a.id = b.biz_id " +
                    " left join meta_book c ON c.id=b.book_id " +
                    " where  b.biz_type in(5,3,2,4,9,10)  and b.is_selected = 1  and a.del_flag=0 and b.del_flag=0 and c.del_flag=0  " +
                    " GROUP BY b.biz_type  ORDER BY biz_type asc LIMIT 0,1";
            Object[] obj = new Object[]{};
            List list = jpaManager.queryForList(sql, obj);

            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                String book_id = String.valueOf(map.get("book_id"));

                if (DitaUtil.isNotBlank(book_id)) {
                    List tagList = (List) getPublicBookIdMap.get(book_id);
                    if (tagList == null || tagList.size() == 0) {       //不存在就新建一个，放入
                        tagList = new ArrayList();
                        tagList.add(map);
                        getPublicBookIdMap.put(book_id, tagList);
                    } else {
                        tagList.add(map);                             //存在放入
                    }
                }
            }

    }

    /* 通过book_id查询
           名家>媒体>编辑>机构 取推荐人职称、推荐语 */
    public Map getPublicBookId(String bookId) {


        Map map = new HashMap();
        if (getPublicBookIdMap == null) {
            getPublicBookId_init();
        }

        List list = (List) getPublicBookIdMap.get(bookId);
        if (list != null && list.size() > 0) {
            map = (Map) list.get(0);
        }
        return map;
    }

    public void clear() {
        bookMap = null;
        bookTagMap = null;
        bookTopMap = null;
        bookAwardMap = null;
        bookAutherMap = null;
        bookMediaMap = null;
        bookNameMap = null;
        bookStaticMap = null;
        bookTradeMap = null;
        bookClumpMap = null;
        bookSearchNameMap = null;
        bookSearchKeyMap = null;
        bookOtherMap = null;
        bookpuByV3Map = null;
        bookSkuBySpuMap = null;
        bookAwardByIdMap = null;
        bookFirstWriterMap = null;
        bookWriterAwardsMap = null;
        bookWriterBooksMap = null;
        bookCommentSkuMap = null;
        bookContentMap = null;
        bookGoodsDetailMap = null;
        RcommdBookByFigureIdMap = null;
        KeyRecommendMap = null;
        getCharactersMap = null;
        getPublicBookIdMap=null;
    }

}
