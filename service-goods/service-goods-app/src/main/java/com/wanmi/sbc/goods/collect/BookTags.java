package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.goods.bean.enums.FigureType;
import com.wanmi.sbc.goods.collect.respository.BookRepository;
import com.wanmi.sbc.goods.collect.respository.GoodRepository;
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
    RedisService redisService;

    @Autowired
    BookRepository bookJpa;

    public void doGoods(){

        List list = bookJpa.getGoodsList();

        //7分41秒
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            doData(map);
        }

    }

    private void doData(Map map) {
        //doGoods(map);           //卖点标签
        doBook(map);            //图书tab
    }

    //单条记录
    public void doGoods(String isbn){

        List goodList = bookJpa.findSpuByV2(isbn);
        if(goodList != null && goodList.size() > 0){
            Map goodMap = (Map)goodList.get(0);
            doData(goodMap);
        }

    }

    //图书tab
    private void doBook(Map goodMap) {

        Map redisMap = new LinkedHashMap();

        String spu_no = String.valueOf(goodMap.get("spu"));
        String isbn = String.valueOf(goodMap.get("isbn"));
        String spu_id = String.valueOf(goodMap.get("spu_id"));

        Map bookMap = bookJpa.getBookMap(isbn);

        if(bookMap == null || bookMap.size() == 0){
            return;
        }

        String book_id = String.valueOf(bookMap.get("id"));

        //推荐内容~关键词
        doSearch(redisMap,book_id);

        //讲稿中提到的其他书籍
        doOtherBooks(redisMap,book_id);

        List allList = new ArrayList();
        doTab1(allList);
        doTab2(allList,book_id);
        doTab3(allList);
        doTab4(allList);

        redisMap.put("salenum","销量");
        redisMap.put("bookDetail",allList);

        setRedis_Books(spu_id,redisMap);

    }

    //讲稿中提到的其他书籍
    private void doOtherBooks(Map redisMap, String book_id) {

        List ret = new ArrayList();

        List list = bookJpa.getOther(book_id);
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String isbn = String.valueOf(map.get("isbn"));
            Map goodMap = bookJpa.findSpuByV3(isbn);
            if(goodMap != null && goodMap.size() >0){
                String spu_no = String.valueOf(goodMap.get("spu_no"));
                String goods_name = String.valueOf(goodMap.get("goods_name"));
                map.put("spu_no",spu_no);
                map.put("goods_name",goods_name);
                ret.add(map);
            }
        }

        redisMap.put("otherBook",ret);
    }

    //推荐内容~关键词
    private void doSearch(Map redisMap, String book_id) {
        List list = bookJpa.book_search_name(book_id);
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String id = String.valueOf(map.get("id"));
            List ret = bookJpa.book_search_key(id);
            map.put("list",ret);
        }
        redisMap.put("search",list);
    }

    private void doTab1(List allList) {

    }

    private void doTab2(List allList,String bookId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> contentMap = new HashMap<>();
        //作家
        Map FirstWriter = getFigure(allList, bookId, FigureType.WRITER.toValue());
        contentMap.put("firstWriter",FirstWriter);

        //翻译家
        Map FirstTranslator = getFigure(allList, bookId, FigureType.TRANSLATOR.toValue());
        contentMap.put("firstTranslator",FirstTranslator);

        //书中提到人物显示
        List characters = bookJpa.getCharacters(bookId);


        map.put("tab2", contentMap);
        allList.add(map);
    }

    private Map getFigure(List allList, String bookId, String figureType) {
        //1.作家 2.翻译家
        List firstWriter = bookJpa.getFirstWriter(bookId, figureType);
        Map map = new HashMap();
        for(int i=0;i<firstWriter.size();i++){
            //作家
            map = (Map)firstWriter.get(0);
            String writerId = String.valueOf(map.get("id"));
            //获得的奖项
            List writerAwards = bookJpa.getWriterAwards(writerId);
            map.put("Awards", writerAwards);
            //查询作家其它的书
            List writerBooks = bookJpa.getWriterBooks(bookId, writerId);
            List ret = new ArrayList();
            for(int j=0;j<writerBooks.size();j++){
                Map writerBookMap = (Map)writerBooks.get(j);
                String isbn = String.valueOf(writerBookMap.get("isbn"));
                Map goodMap = bookJpa.findSpuByV3(isbn);
                if(goodMap != null && goodMap.size() >0){
                    String spu_no = String.valueOf(goodMap.get("spu_no"));
                    String goods_name = String.valueOf(goodMap.get("goods_name"));
                    writerBookMap.put("spu_no",spu_no);
                    writerBookMap.put("goods_name",goods_name);
                    ret.add(writerBookMap);
                }
            }
            map.put("Books", ret);
        }
        return map;
    }

    private void doTab3(List allList) {

    }

    private void doTab4(List allList) {

    }


    public void doGoods(Map goodMap){

        String spu_no = String.valueOf(goodMap.get("spu"));
        String isbn = String.valueOf(goodMap.get("isbn"));

        //spu_no = "P735546359";
        //isbn   = "ISBN_C_T003";

        Map bookMap = bookJpa.getBookMap(isbn);

        if(bookMap == null || bookMap.size() == 0){
            return;
        }

        String book_id = String.valueOf(bookMap.get("id"));
        String trade_id = String.valueOf(bookMap.get("trade_id"));

        List allList = new ArrayList();

        //10. 大促标签
        List tagList1 = bookJpa.getTagList(book_id);
        if(tagList1!=null && tagList1.size() > 0){
            allList.addAll(tagList1);
        }

        //20. 榜单标签
        if(StringUtil.isNotBlank(spu_no)) {
            List topList = bookJpa.getTopList(spu_no);
            if(topList!=null && topList.size() > 0){
                allList.addAll(topList);
            }
        }

        //30. 书本身有奖项，显示第一个奖项名称
        List awardList = bookJpa.getAwardList(book_id);
        if(awardList!=null && awardList.size() > 0){
            allList.addAll(awardList);
        }

        //40. 图书作者有获奖，显示『奖项名称+获得者（作者）』
        List authorList = bookJpa.getAutherList(book_id);
        if(authorList!=null && authorList.size() > 0){
            allList.addAll(authorList);
        }

        //50. 当有指定的打标媒体、名家、专业机构推荐时，显示『媒体名称/名家名称/专业机构名称推荐』
        //List mediaList = getMediaListList(book_id);
        //if(mediaList!=null && mediaList.size() > 0){
        //    allList.addAll(mediaList);
        //}

        //60. 有图书库-推荐信息，显示『X位名家，X家媒体，X家专业机构推荐』
        List mediaList = bookJpa.getMediaList(book_id);
        if(mediaList!=null && mediaList.size() > 0){
            allList.addAll(mediaList);
        }

        //70. 书中提到的人物，有数据则显示：人物名称
        List nameList = bookJpa.getNameList(book_id);
        if(nameList!=null && nameList.size() > 0){
            allList.addAll(nameList);
        }

        //80. 图书本身最小年龄段、最大年龄段有数据，显示数字，X~Y岁，当任意一项没有对应显示为空
        List ageList = getAgeList(goodMap);
        if(ageList!=null && ageList.size() > 0){
            allList.addAll(ageList);
        }

        //90. 适读对象：当数对像有数据，则全量显示(先取静态标签)
        List staticList = bookJpa.getStaticList(book_id);
        if(staticList!=null && staticList.size() > 0){
            allList.addAll(staticList);
        }

        //100. 行业类类目：（本次新加字段），显示图书所在行业类目，按类目树结构显示，一级名称>二级名称>三级名称
        if(DitaUtil.isNotBlank(trade_id)){
            List tradeList = bookJpa.getTradeList(trade_id);
            if(tradeList!=null && tradeList.size() > 0){
                allList.addAll(tradeList);
            }
        }


        //110. 图书被包含在某丛书，显示「丛书」名称
        List clumpList = bookJpa.getClumpList(book_id);
        if(clumpList!=null && clumpList.size() > 0){
            allList.addAll(clumpList);
        }

        //120. 标签：一级分类=？？下显示3个图书库中关联优先级最高的标签

        Map map = new LinkedHashMap();
        map.put("isBook","yes");
        map.put("tags",allList);

        setRedis_Tags(spu_no,map);
    }

    private List getAgeList(Map goodMap) {

        List ageList = new ArrayList();

        String fit_age_min = String.valueOf(goodMap.get("fit_age_min"));        //'最小阅读年龄'
        String fit_age_max = String.valueOf(goodMap.get("fit_age_max"));        //'最大阅读年龄'

        if(com.wanmi.sbc.goods.collect.DitaUtil.isNotBlank(fit_age_min) && com.wanmi.sbc.goods.collect.DitaUtil.isNotBlank(fit_age_max)){
            Map map = new HashMap();
            String name = fit_age_min + "~" + fit_age_max + "岁";
            map.put("name",name);
            map.put("show_name",name);
            map.put("order_type",80);
        }

        return ageList;

    }

    public void setRedis_Tags(String spu_no,Map map){

        //String json = JSONArray.parseArray(JSON.toJSONString(list)).toJSONString();
        String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();

        String old_json = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_NO + ":" + spu_no);
        if(!json.equals(old_json)){
            redisService.setString(RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_NO+":" + spu_no, json );
            String updateTime = DitaUtil.getCurrentAllDate();
            bookJpa.updateGoodTimeByNo(updateTime,spu_no);
        }

    }

    public void setRedis_Books(String spu_id,Map map){

        //String json = JSONArray.parseArray(JSON.toJSONString(list)).toJSONString();
        String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();

        String old_json = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID + ":" + spu_id);
        if(!json.equals(old_json)){
            redisService.setString(RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID+":" + spu_id, json );
            String updateTime = DitaUtil.getCurrentAllDate();
            bookJpa.updateGoodTime(updateTime,spu_id);
        }

    }


}

