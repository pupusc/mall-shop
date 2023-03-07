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

    public static Map bookMap = null;

    public static Map tagMap = null;

    //通过isbn查找book_id缓存
    public void getBookMap_init() {

        bookMap = new HashMap();

        String sql = " select id,isbn,trade_id from meta_book where del_flag=0 ";

        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String isbn =  String.valueOf(map.get("isbn"));

            Map oldMap = (Map)bookMap.get(isbn);
            if(oldMap == null || oldMap.size() == 0){       //不存在就放入
                bookMap.put(isbn,map);
            }

        }

    }

    //通过isbn查找book_id
    public Map getBookMap_cache(String isbn) {
        if(bookMap == null){
            getBookMap_init();
        }

        return (Map)bookMap.get(isbn);
    }

    //10. 大促标签_缓存
    public void getTagList_init() {

        tagMap = new HashMap();

        String sql = " select a.id,a.name,show_img,show_status,is_static,b.book_id,10 as order_type from meta_label a left join meta_book_label b on a.id = b.label_id " +
                " where a.del_flag = 0 and a.is_static = 1 order by seq asc ";
        Object[] obj = new Object[]{};
        List list = jpaManager.queryForList(sql,obj);
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String book_id =  String.valueOf(map.get("book_id"));

            if(DitaUtil.isNotBlank(book_id)){
                List tagList = (List)tagMap.get(book_id);
                if(tagList == null || tagList.size() == 0){       //不存在就新建一个，放入
                    tagList = new ArrayList();
                    tagList.add(map);
                    tagMap.put(book_id,tagList);
                }else{
                    tagList.add(map);                             //存在放入
                }
            }

        }

    }

    //10. 大促标签
    public List getTagList_cache(String book_id) {
        if(tagMap == null){
            getTagList_init();
        }

        return (List)tagMap.get(book_id);
    }

    public void clear(){
        bookMap = null;
        tagMap = null;
    }

}
