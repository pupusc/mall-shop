package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdFigureBO;
import com.wanmi.sbc.bookmeta.service.MetaFigureService;
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
//图书商品详细
public class BookDetailTab {

    @Autowired
    RedisService redisService;

    @Autowired
    BookRepository bookJpa;

    @Autowired
    GoodRepository goodJpa;

    @Autowired
    MetaFigureService metaFigureService;

    //图书tab
    void doBook(Map goodMap) {

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
        //doTab1(allList,book_id);
        doTab2(allList,book_id,spu_no);
        doTab3(allList);
        doTab4(allList);

        redisMap.put("salenum","销量");
        redisMap.put("bookDetail",allList);

        setRedis_Books(spu_id,redisMap);

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

    private void doTab1(List allList,String book_id) {
        Map map=new HashMap<>();
        List<MetaBookRcmmdFigureBO> metaBookRcmmdFigureBOS = metaFigureService.getMetaBookRcmmdFigureBOS(Integer.parseInt(book_id));
        map.put("medioRecomd",metaBookRcmmdFigureBOS);
        allList.add(map);
    }

    private void doTab2(List allList,String bookId,String spuNo) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> contentMap = new HashMap<>();
        //作家
        Map FirstWriter = getFigure(bookId, FigureType.WRITER.toValue());
        contentMap.put("firstWriter",FirstWriter);

        //翻译家
        Map FirstTranslator = getFigure(bookId, FigureType.TRANSLATOR.toValue());
        contentMap.put("firstTranslator",FirstTranslator);

        //简介目录原文摘要
        List content = bookJpa.getContent(bookId);
        Map contents = content.size() != 0 ? (Map)content.get(0) : null;
        contentMap.put("content", contents);

        //图文详情
        List goodsDetail = goodJpa.getGoodsDetail(spuNo);
        Map goodsDetailMap = goodsDetail.size() != 0 ? (Map)goodsDetail.get(0) : null;
        contentMap.put("goodsDetail", goodsDetailMap.get("goods_detail"));

        //书中提到人物显示
        List characters = bookJpa.getCharacters(bookId);
        Map charactersMap = characters.size() != 0 ? (Map)characters.get(0) : null;
        contentMap.put("character", charactersMap);

        //丛书
        Map libraryMap = getLibrary(bookId);
        contentMap.put("library", libraryMap);

        //出版方
        Map producerMap = getProducer(bookId);
        contentMap.put("producer", producerMap);

        map.put("tab2", contentMap);
        allList.add(map);
    }



    //1.作家 2.翻译家
    private Map getFigure(String bookId, String figureType) {
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

    //获取丛书信息
    private Map getLibrary(String bookId) {
        List libraryName = bookJpa.getLibraryName(bookId);
        Map map = new HashMap();
        for(int i=0;i<libraryName.size();i++){
            //丛书名称
            map = (Map)libraryName.get(0);
            String libraryId = String.valueOf(map.get("id"));
            //获得的奖项
            List libraryNum = bookJpa.getLibraryNum(libraryId);
            map.put("libraryNum", libraryNum);
            //查询作家其它的书
            List library = bookJpa.getLibrary(bookId, libraryId);
            List ret = new ArrayList();
            for(int j=0;j<library.size();j++){
                Map libraryMap = (Map)library.get(j);
                String isbn = String.valueOf(libraryMap.get("isbn"));
                Map goodMap = bookJpa.findSpuByV3(isbn);
                if(goodMap != null && goodMap.size() >0){
                    String spu_no = String.valueOf(goodMap.get("spu_no"));
                    String goods_name = String.valueOf(goodMap.get("goods_name"));
                    libraryMap.put("spu_no",spu_no);
                    libraryMap.put("goods_name",goods_name);
                    ret.add(libraryMap);
                }
            }
            map.put("Books", ret);
        }
        return map;
    }

    //获取出版方信息
    private Map getProducer(String bookId) {
        List producerName = bookJpa.getProducerName(bookId);
        Map map = new HashMap();
        for(int i=0;i<producerName.size();i++){
            //丛书名称
            map = (Map)producerName.get(0);
            String producerId = String.valueOf(map.get("id"));
            //获得的奖项
            List producerNum = bookJpa.getProducerNum(producerId);
            map.put("producerNum", producerNum);
            //查询作家其它的书
            List producer = bookJpa.getProducer(bookId, producerId);
            List ret = new ArrayList();
            for(int j=0;j<producer.size();j++){
                Map producerMap = (Map)producer.get(j);
                String isbn = String.valueOf(producerMap.get("isbn"));
                Map goodMap = bookJpa.findSpuByV3(isbn);
                if(goodMap != null && goodMap.size() >0){
                    String spu_no = String.valueOf(goodMap.get("spu_no"));
                    String goods_name = String.valueOf(goodMap.get("goods_name"));
                    producerMap.put("spu_no",spu_no);
                    producerMap.put("goods_name",goods_name);
                    ret.add(producerMap);
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

    public void setRedis_Books(String spu_id,Map map){

        //String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();
        String json = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);

        String old_json = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID + ":" + spu_id);
        if(!json.equals(old_json)){
            redisService.setString(RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID+":" + spu_id, json );
            String updateTime = DitaUtil.getCurrentAllDate();
            bookJpa.updateGoodTime(updateTime,spu_id);
        }

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

}

