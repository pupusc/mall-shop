package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.service.MetaFigureService;
import com.wanmi.sbc.goods.bean.enums.FigureType;
import com.wanmi.sbc.goods.collect.respository.BookRepository;
import com.wanmi.sbc.goods.collect.respository.GoodRepository;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    GoodTags goodTags;

    @Autowired
    private MarketLabel marketLabel;

    @Autowired
    private BookCacheService bookCacheService;

    @Autowired
    private BookDetailTab bookDetailTab;

    //图书tab
    void doBook(Map goodMap) {

        Map redisMap = new LinkedHashMap();


        String spu_no = String.valueOf(goodMap.get("spu"));
        String isbn = String.valueOf(goodMap.get("isbn"));
        String spu_id = String.valueOf(goodMap.get("spu_id"));

       /* String  spu_id = "2c9a00ca86299cda01862a0163e60000";
        String sku_id = "2c9a009b86a5b1850186a6ae64c80004";
        String spu_no = "P735546359";
        String isbn   = "ISBN_C_T003";*/


        // Map bookMap = bookJpa.getBookMap(isbn);
        Map bookMap = bookCacheService.getBookMap_cache(isbn);   //2.读缓存


        if (bookMap == null || bookMap.size() == 0) {
            return;
        }

        String book_id = String.valueOf(bookMap.get("id"));  //7838

        //推荐内容~关键词
        doSearch(redisMap, book_id);

        //讲稿中提到的其他书籍
        doOtherBooks(redisMap, book_id);

        List allList = new ArrayList();
        doTab1(allList, book_id);
        doTab2(allList, book_id, spu_no);
        doTab3(allList);
        doTab4(allList, spu_id);

        //销量
        String saleNum = getSaleNum_bySpuID(spu_id);
        redisMap.put("salenum", saleNum);
        //定价
        String fix_price = getFixPrice(spu_id);
        // redisMap.put("fix_price", fix_price);

        //根据spu 找到sku
        String sku_id = String.valueOf(goodJpa.getSkuBySpuId(spu_id).get("goods_info_id"));
        //是否显示积分全额抵扣（参加积分活动和加入黑名单中的商品不显示）
        redisMap.put("isShowIntegral",bookDetailTab.isShowIntegral(spu_id,sku_id));

        redisMap.put("bookDetail", allList);

        setRedis_Books(spu_id, redisMap);

    }


    //redis优化用
    private void doTab1(List allList, String book_id) {
        Map map = new HashMap<>();
        //推荐人列表
        List metaBookRcmmdFigureList = bookJpa.RcommdFigureByBookId(book_id);
        if (null == metaBookRcmmdFigureList || metaBookRcmmdFigureList.size() == 0) {
            return;
        }

        //对于每一个推荐人，找到其推荐列表
        List<Map> result = (List<Map>) metaBookRcmmdFigureList.stream().map(bs -> {
            Map maptemp = (Map) bs;
            maptemp.remove("del_flag");
            maptemp.remove("update_time");
            maptemp.remove("create_time");
            maptemp.remove("is_selected");
            maptemp.remove("biz_time");
            maptemp.put("sku_id", maptemp.get("goods_info_id"));
            maptemp.remove("goods_info_id");
            if (null == maptemp.get("biz_type") || null == maptemp.get("biz_id")) {
                return null;
            }
            if (BookRcmmdTypeEnum.WENMIAO.getCode().equals(Integer.parseInt(maptemp.get("biz_type").toString()))) {
                if (null == maptemp.get("descr")) {
                    return null;//文喵必有推荐语
                } else {//文喵不用给商品列表
                    return maptemp;
                }
            }
            //如果是奖项，仅透出奖项名
            if (BookRcmmdTypeEnum.AWARD.getCode().equals(Integer.parseInt(maptemp.get("biz_type").toString()))) {
                String awardName = bookJpa.queryAwardById(Integer.parseInt(maptemp.get("biz_id").toString())).get(0).get("name").toString();
                maptemp.put("name", awardName);
                return maptemp;
            }
            //如果是选书人，仅透出选书人信息
            if (BookRcmmdTypeEnum.XUANSHUREN.getCode().equals(Integer.parseInt(maptemp.get("biz_type").toString()))) {
                return maptemp;
            }
            if (null == maptemp.get("biz_type") && null == maptemp.get("descr")) {
                return null;//没有推荐人和推荐语的跳过
            }
            List<String> isbnList = bookJpa.RcommdBookByFigureId(Integer.parseInt(maptemp.get("biz_id").toString()), book_id);
            if (null != isbnList && isbnList.size() != 0) {
                //说明这个推荐人有其他可推荐的,构建推荐商品的详细信息

                List<Map> goodsInfoMap = new ArrayList<>();

                isbnList.stream().forEach(isbn -> {
                    Map goodMap = bookJpa.findSpuByV3(isbn);
                    if (goodMap != null && goodMap.size() > 0) {
                        Map mapTemp = new HashMap<>();
                        String goods_name = String.valueOf(goodMap.get("goods_name"));
                        String spu_id = String.valueOf(goodMap.get("spu_id"));
                        String sku_id = null;
                        Map skuBySpuId = goodJpa.getSkuBySpuId(spu_id);
                        if (skuBySpuId != null && skuBySpuId.size() > 0) {
                            sku_id = String.valueOf(skuBySpuId.get("goods_info_id"));
                            mapTemp.put("sku_id", sku_id);
                        }
                        mapTemp.put("goods_id", spu_id);
                        mapTemp.put("goods_info_name", goods_name);
                        mapTemp.put("sku_id", sku_id);
                        goodsInfoMap.add(mapTemp);
                    }
                });

                if (null == goodsInfoMap || goodsInfoMap.size() == 0) {
                    return maptemp;
                }
                //构建返回类型
                List<Map> recomentBookVoMap = goodsInfoMap.stream().map(goodsInfoMapTemp -> {
                    if (null == goodsInfoMapTemp.get("goods_id") || null == goodsInfoMapTemp.get("goods_info_name") || null == goodsInfoMapTemp.get("sku_id")) {
                        return null;
                    }
                    Map recomentBookVo = new HashMap<>();
                    recomentBookVo.put("goodsId", goodsInfoMapTemp.get("goods_id").toString());
                    recomentBookVo.put("goodsInfoName", goodsInfoMapTemp.get("goods_info_name").toString());
                    String sku_id = goodsInfoMapTemp.get("sku_id").toString();
                    recomentBookVo.put("sku_id", sku_id);
                    recomentBookVo.put("labelMap", null);

                    String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + sku_id);
                    if (null != old_json) {
                        Map labelMap = JSONObject.parseObject(old_json, Map.class);
                        recomentBookVo.put("labelMap", labelMap);
                        recomentBookVo.put("sku_id", goodsInfoMapTemp.get("sku_id").toString());
                    }
                    return recomentBookVo;
                }).filter(g -> null != g).collect(Collectors.toList());

                if (null != recomentBookVoMap && recomentBookVoMap.size() != 0) {
                    maptemp.put("recomentBookBoList", recomentBookVoMap);
                }
                return maptemp;
            }
            return null;
        }).collect(Collectors.toList());
        List<Map> collect = result.stream().filter(r -> null != r).collect(Collectors.toList());
        map.put("medioRecomd", collect);
        allList.add(map);

    }

    private void doTab2(List allList, String bookId, String spuNo) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> contentMap = new HashMap<>();
        //作家
        Map FirstWriter = getFigure(bookId, FigureType.WRITER.toValue());
        contentMap.put("firstWriter", FirstWriter);

        //翻译家
        Map FirstTranslator = getFigure(bookId, FigureType.TRANSLATOR.toValue());
        contentMap.put("firstTranslator", FirstTranslator);

        //简介目录原文摘要
        List content = bookJpa.getContent(bookId);
        //Map contents = content.size() != 0 ? (Map) content.get(0) : null;
        contentMap.put("content", content);

        //图文详情
        List goodsDetail = goodJpa.getGoodsDetail(spuNo);
        Map goodsDetailMap = goodsDetail.size() != 0 ? (Map) goodsDetail.get(0) : null;
        contentMap.put("goodsDetail", goodsDetailMap.get("goods_detail"));

        //书中提到人物显示
        List characters = bookJpa.getCharacters(bookId);
        Map charactersMap = characters.size() != 0 ? (Map) characters.get(0) : null;
        contentMap.put("character", charactersMap);

        //丛书
        Map libraryMap = getLibrary(bookId);
        contentMap.put("library", libraryMap);

        //出版方
        Map producerMap = getProducer(bookId);
        contentMap.put("producer", producerMap);

        //推荐内容~关键词~图书
        Map mapRecommend = getKeyRecommend(bookId);
        contentMap.put("recommend", mapRecommend);

        map.put("tab2", contentMap);
        allList.add(map);
    }

    //1.作家 2.翻译家
    private Map getFigure(String bookId, String figureType) {
        //1.作家 2.翻译家
        List firstWriter = bookJpa.getFirstWriter(bookId, figureType);
        Map map = new HashMap();
        for (int i = 0; i < firstWriter.size(); i++) {
            //作家
            map = (Map) firstWriter.get(i);
            String writerId = String.valueOf(map.get("id"));
            //获得的奖项
            List writerAwards = bookJpa.getWriterAwards(writerId);
            map.put("Awards", writerAwards);
            //查询作家其它的书
            List writerBooks = bookJpa.getWriterBooks(bookId, writerId);
            List ret = new ArrayList();
            for (int j = 0; j < writerBooks.size(); j++) {
                Map writerBookMap = (Map) writerBooks.get(j);
                String isbn = String.valueOf(writerBookMap.get("isbn"));
                Map goodMap = bookJpa.findSpuByV3(isbn);
                if (goodMap != null && goodMap.size() > 0) {
                    String spu_no = String.valueOf(goodMap.get("spu_no"));
                    String goods_name = String.valueOf(goodMap.get("goods_name"));
                    String spu_id = String.valueOf(goodMap.get("spu_id"));
                    Map skuBySpuId = goodJpa.getSkuBySpuId(spu_id);
                    writerBookMap.put("sku_id", null);
                    writerBookMap.put("labelMap", null);
                    String sku_id = "";
                    if (skuBySpuId != null && skuBySpuId.size() > 0) {
                        sku_id = String.valueOf(skuBySpuId.get("goods_info_id"));
                        writerBookMap.put("sku_id", sku_id);
                    }
                    writerBookMap.put("spu_no", spu_no);
                    writerBookMap.put("goods_name", goods_name);
                    writerBookMap.put("spu_id", spu_id);
                    if (null != spu_id) {
                        String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + sku_id);
                        if (null != old_json) {
                            Map labelMap = JSONObject.parseObject(old_json, Map.class);
                            writerBookMap.put("labelMap", labelMap);
                        }
                    }
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
        for (int i = 0; i < libraryName.size(); i++) {
            //丛书名称
            map = (Map) libraryName.get(0);
            String libraryId = String.valueOf(map.get("id"));
            //获得的奖项
            List libraryNum = bookJpa.getLibraryNum(libraryId);
            map.put("libraryNum", libraryNum);
            //查询作家其它的书
            List library = bookJpa.getLibrary(bookId, libraryId);
            List ret = new ArrayList();
            for (int j = 0; j < library.size(); j++) {
                Map libraryMap = (Map) library.get(j);
                String isbn = String.valueOf(libraryMap.get("isbn"));
                Map goodMap = bookJpa.findSpuByV3(isbn);
                if (goodMap != null && goodMap.size() > 0) {
                    String spu_no = String.valueOf(goodMap.get("spu_no"));
                    String goods_name = String.valueOf(goodMap.get("goods_name"));
                    String spu_id = String.valueOf(goodMap.get("spu_id"));
                    libraryMap.put("sku_id", null);
                    Map skuBySpuId = goodJpa.getSkuBySpuId(spu_id);
                    String sku_id = "";
                    if (skuBySpuId != null && skuBySpuId.size() > 0) {
                        sku_id = String.valueOf(skuBySpuId.get("goods_info_id"));
                        libraryMap.put("sku_id", sku_id);
                    }
                    libraryMap.put("spu_no", spu_no);
                    libraryMap.put("goods_name", goods_name);
                    libraryMap.put("spu_id", spu_id);
                    libraryMap.put("labelMap", null);

                    if (null != spu_id) {
                        String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + sku_id);
                        if (null != old_json) {
                            Map labelMap = JSONObject.parseObject(old_json, Map.class);
                            libraryMap.put("labelMap", labelMap);
                        }
                    }
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
        for (int i = 0; i < producerName.size(); i++) {
            //丛书名称
            map = (Map) producerName.get(0);
            String producerId = String.valueOf(map.get("id"));
            //获得的奖项
            List producerNum = bookJpa.getProducerNum(producerId);
            map.put("producerNum", producerNum);
            //查询作家其它的书
            List producer = bookJpa.getProducer(bookId, producerId);
            List ret = new ArrayList();
            for (int j = 0; j < producer.size(); j++) {
                Map producerMap = (Map) producer.get(j);
                String isbn = String.valueOf(producerMap.get("isbn"));
                Map goodMap = bookJpa.findSpuByV3(isbn);
                if (goodMap != null && goodMap.size() > 0) {
                    String spu_no = String.valueOf(goodMap.get("spu_no"));
                    String goods_name = String.valueOf(goodMap.get("goods_name"));
                    String spu_id = String.valueOf(goodMap.get("spu_id"));
                    producerMap.put("sku_id", null);
                    Map skuBySpuId = goodJpa.getSkuBySpuId(spu_id);
                    producerMap.put("labelMap", null);
                    if (skuBySpuId != null && skuBySpuId.size() > 0) {
                        String sku_id = String.valueOf(skuBySpuId.get("goods_info_id"));
                        if (null != sku_id) {
                            String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + sku_id);
                            if (null != old_json) {
                                Map labelMap = JSONObject.parseObject(old_json, Map.class);
                                producerMap.put("labelMap", labelMap);
                            }
                        }
                    }
                    producerMap.put("spu_no", spu_no);
                    producerMap.put("goods_name", goods_name);
                    producerMap.put("spu_id", spu_id);
                    ret.add(producerMap);
                }
            }
            map.put("Books", ret);
        }
        return map;
    }

    public Map getKeyRecommend(String bookId) {
        Map mapRecommend = goodJpa.getKeyRecommend(bookId);
        return mapRecommend;
    }

    private void doTab3(List allList) {

    }

    private void doTab4(List allList, String spuId) {
        Map<String, Object> map = new HashMap<>();
        Map orderDetail = goodJpa.getOrderDetail(spuId);
        map.put("tab4", orderDetail);
        allList.add(map);
    }

    //sku 查询销量
    private String getSaleNum_byskuID(String sku_id) {

        List<Map> saleNum = new ArrayList<>();

        saleNum = bookJpa.getSaleNum(sku_id);//根据sku 查询榜单销量
        String sale_num = "";
        if (!saleNum.isEmpty()) { //根据sku 查询榜单销量
            sale_num = String.valueOf(saleNum.get(0).get("sale_num"));

        } else {     //没查到就取商品表sku 销售量
            saleNum = bookJpa.getSkuSaleNum(sku_id);
            if (!saleNum.isEmpty()) {//根据sku 查询商品销量
                sale_num = String.valueOf(saleNum.get(0).get("sale_num"));

            }
        }

        return DitaUtil.isBlank(sale_num) ? null : sale_num; //排除“null”

    }

    public String getSaleNum_bySpuID(String spu_id) {

        String sale_num = null;

        //通过spu_查找sku_id
        // String sku_id = goodJpa.getSkuBySpuId(spu_id);
        String sku_id = String.valueOf(goodJpa.getSkuBySpuId(spu_id).get("goods_info_id"));

        return getSaleNum_byskuID(sku_id);

       /* if(Integer.parseInt(sale_num)<300){

            List<Map> comentPoint = bookJpa.getComentPointV2(spu_id);
            if(null==comentPoint ||comentPoint.size()==0 ){
                return null;
            }
            if(null ==comentPoint.get(0).get("prop_value")){
                return null;
            }
            String point = comentPoint.get(0).get("prop_value").toString();
            return point;
        }*/

    }


    //讲稿中提到的其他书籍
    private void doOtherBooks(Map redisMap, String book_id) {

        List list = bookJpa.getOther(book_id);
        List ret = getBookList(list);

        redisMap.put("otherBook", ret);
    }

    //通过书去查找书籍~商品
    public List getBookList(List bookList) {

        List ret = new ArrayList();

        for (int i = 0; i < bookList.size(); i++) {
            Map map = (Map) bookList.get(i);
            String isbn = String.valueOf(map.get("isbn"));
            Map goodMap = bookJpa.findSpuByV3(isbn);
            if (goodMap != null && goodMap.size() > 0) {
                String spu_no = String.valueOf(goodMap.get("spu_no"));
                String goods_name = String.valueOf(goodMap.get("goods_name"));
                String spu_id = String.valueOf(goodMap.get("spu_id"));
                Map skuBySpuId = goodJpa.getSkuBySpuId(spu_id);
                String sku_id = "";
                if (skuBySpuId != null && skuBySpuId.size() > 0) {
                    sku_id = String.valueOf(skuBySpuId.get("goods_info_id"));
                    map.put("sku_id", sku_id);
                }
                map.put("spu_id", spu_id);
                map.put("spu_no", spu_no);
                map.put("goods_name", goods_name);
                map.put("labelMap", null);
                if (null != spu_id) {
                    //修改
                    String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + sku_id);
                    if (null != old_json) {
                        Map labelMap = JSONObject.parseObject(old_json, Map.class);
                        map.put("labelMap", labelMap);
                    }
                }
                ret.add(map);
            }
        }
        return ret;
    }

    //推荐内容~关键词
    private void doSearch(Map redisMap, String book_id) {
        List list = bookJpa.book_search_name(book_id);
        //List list = bookCacheService.book_search_name(book_id);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String id = String.valueOf(map.get("id"));
            List ret = bookJpa.book_search_key(id);
            map.put("list", ret);
        }
        redisMap.put("search", list);
    }

    public void setRedis_Books(String spu_id, Map map) {

        //String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();
        String json = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);

        String old_json = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID + ":" + spu_id);
        if (!json.equals(old_json)) {
            redisService.setString(RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID + ":" + spu_id, json);
            String updateTime = DitaUtil.getCurrentAllDate();
            bookJpa.updateGoodTime(updateTime, spu_id);
        }

    }

    //取定价8
    public String getFixPrice(String spuId) {

        String getFixPrice = "";
        //根据spu 找到sku
        String sku_id = String.valueOf(goodJpa.getSkuBySpuId(spuId).get("goods_info_id"));
        getFixPrice = String.valueOf(goodJpa.getfixPricebySku(sku_id).get("fix_price"));
        if (DitaUtil.isBlank(getFixPrice)) {
            getFixPrice = String.valueOf(goodJpa.getfixPricebySpu(spuId).get("fix_price"));
        }
        return DitaUtil.isBlank(getFixPrice) ? null : getFixPrice; //排除“null”
    }

    //是否显示积分全额抵扣
    public boolean isShowIntegral(String spuId, String sku_id) {
        boolean flag = true;//默认都显示
        List blackList = goodJpa.getBlackBySpuId(spuId);//查询积分黑名单
        List makertList = goodJpa.getByMarketSkuId(sku_id);//查询参加积分兑换活动

        //参加积分兑换活动 或者 加入黑名单 都不显示 积分全额抵扣
        if (blackList.size() > 0 || makertList.size() > 0) {
            flag = false;
        }
        return flag;
    }
}

