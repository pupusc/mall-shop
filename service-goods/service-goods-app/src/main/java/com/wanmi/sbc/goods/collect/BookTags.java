package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.bookmeta.bo.MetaAwardBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdFigureBO;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaZoneBookMapper;
import com.wanmi.sbc.bookmeta.service.MetaFigureService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.TagsDto;
import com.wanmi.sbc.goods.bean.enums.FigureType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.collect.respository.BookRepository;
import com.wanmi.sbc.goods.collect.respository.GoodRepository;
import com.wanmi.sbc.goods.redis.RedisService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
//图书商品
public class BookTags {

    @Autowired
    RedisService redisService;

    @Autowired
    BookRepository bookJpa;

    @Autowired
    GoodRepository goodJpa;

    @Autowired
    MarketLabel marketLabel;

    @Autowired
    BookDetailTab bookDetailTab;

    @Autowired
    private MetaFigureService metaFigureService;
    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;


    public void doGoods(){

        List list = bookJpa.getGoodsList();

        //7分41秒
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            doData(map);
        }

    }

    private void doData(Map map) {

        doGoods(map);                 //卖点标签&&营销标签
        bookDetailTab.doBook(map);    //图书tab

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
        doTab1New(allList,book_id);
        doTab2(allList,book_id,spu_no);
        doTab3(allList);
        doTab4(allList);
        String saleNum = getSaleNum(spu_id);
        redisMap.put("salenum",saleNum);
        redisMap.put("bookDetail",allList);

        setRedis_Books(spu_id,redisMap);

    }

    private String getSaleNum(String spu_id) {
        String goods_id = bookJpa.getSkuBySpu(spu_id).get(0).get("goods_id").toString();
        String sale_num = bookJpa.getSaleNum(goods_id).get(0).get("sale_num").toString();
        if(Integer.parseInt(sale_num)<300){
            String point = bookJpa.getComentPoint(spu_id).get(0).get("prop_value").toString();
            return point;
        }
        return sale_num;
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

    //远程调用
    private void doTab1(List allList,String book_id) {
        Map map=new HashMap<>();
        List<MetaBookRcmmdFigureBO> metaBookRcmmdFigureBOS = metaFigureService.getMetaBookRcmmdFigureBOS(Integer.parseInt(book_id));
        map.put("medioRecomd",metaBookRcmmdFigureBOS);
        allList.add(map);
    }

    //redis优化用
    private void doTab1New(List allList,String book_id) {
        Map map=new HashMap<>();
        //推荐人列表
        List metaBookRcmmdFigureList = bookJpa.RcommdFigureByBookId(book_id);
        //对于每一个推荐人，找到其推荐列表
        List<Map> result =(List<Map>) metaBookRcmmdFigureList.stream().map(bs -> {
            Map maptemp=(Map) bs;
            maptemp.remove("del_flag");
            maptemp.remove("update_time");
            maptemp.remove("create_time");
            maptemp.remove("is_selected");
            maptemp.remove("biz_time");
            if(BookRcmmdTypeEnum.WENMIAO.getCode().equals(Integer.parseInt(maptemp.get("biz_type").toString()))){
                if(null==maptemp.get("descr")){
                    return null;//文喵必有推荐语
                }else{//文喵不用给商品列表
                    return maptemp;
                }
            }
            //如果是奖项，仅透出奖项名
            if(BookRcmmdTypeEnum.AWARD.getCode().equals(Integer.parseInt(maptemp.get("biz_type").toString()))){
                String awardName = bookJpa.queryAwardById(Integer.parseInt(maptemp.get("biz_id").toString())).get(0).get("name").toString();
                maptemp.put("name",awardName);
                return maptemp;
            }
            //如果是选书人，仅透出选书人信息
            if(BookRcmmdTypeEnum.XUANSHUREN.getCode().equals(Integer.parseInt(maptemp.get("biz_type").toString()))){
                return maptemp;
            }
            if(null==maptemp.get("biz_type") && null == maptemp.get("descr")){
                return null;//没有推荐人和推荐语的跳过
            }
            List<String> isbnList = bookJpa.RcommdBookByFigureId(Integer.parseInt(maptemp.get("biz_id").toString()), book_id);
            if (null !=isbnList && isbnList.size() != 0) {
                //说明这个推荐人有其他可推荐的,构建推荐商品的详细信息

               List<Map> goodsInfoMap = bookJpa.goodsInfoByIsbns(isbnList);

                if(null==goodsInfoMap || goodsInfoMap.size()==0){
                    return maptemp;
                }
                //构建返回类型
                List<Map> recomentBookVoMap = goodsInfoMap.stream().map(goodsInfoMapTemp -> {
                    Map recomentBookVo = new HashMap<>();
                    recomentBookVo.put("goodsId", goodsInfoMapTemp.get("goods_id").toString());
                    recomentBookVo.put("goodsInfoName", goodsInfoMapTemp.get("goods_info_name").toString());
                    recomentBookVo.put("goodsInfoNo", goodsInfoMapTemp.get("goods_info_no").toString());
                    TagsDto tagsDto = goodsInfoQueryProvider.getTabsBySpu(goodsInfoMapTemp.get("goods_id").toString()).getContext();
                    if(null!=tagsDto.getTags() &&tagsDto.getTags().size()!=0 ) {
                      //  BeanUtils.copyProperties(tagsDto, recomentBookVo.getTagsDto().getTags());
                        recomentBookVo.put("tagsDto",tagsDto);
                    }
                    return recomentBookVo;
                }).collect(Collectors.toList());

                maptemp.put("recomentBookBoList",recomentBookVoMap);
                return maptemp;
            }
            return null;
        }).collect(Collectors.toList());
        List<Map> collect = result.stream().filter(r -> null != r).collect(Collectors.toList());
        map.put("medioRecomd",collect);
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


    public void doGoods(Map goodMap){

        String spu_no = String.valueOf(goodMap.get("spu"));
        String isbn = String.valueOf(goodMap.get("isbn"));
        String spu_id = String.valueOf(goodMap.get("spu_id"));

        //spu_id = "2c9a00ca86299cda01862a0163e60000"
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
        List ageList = getAgeList(isbn);
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

        setRedis_Tags(spu_id,map);
    }

    private List getAgeList(String isbn) {

        String fit_age_min = null;
        String fit_age_max = null;

        Map bookMap = bookJpa.getBookMap(isbn);
        if(bookMap != null && bookMap.size() >0 ){
            fit_age_min = String.valueOf(bookMap.get("fit_age_min"));        //'最小阅读年龄'
            fit_age_max = String.valueOf(bookMap.get("fit_age_max"));        //'最大阅读年龄'
        }

        List ageList = new ArrayList();

        if(DitaUtil.isNotBlank(fit_age_min) && DitaUtil.isNotBlank(fit_age_max)){
            Map map = new HashMap();
            String name = fit_age_min + "~" + fit_age_max + "岁";
            map.put("name",name);
            map.put("show_name",name);
            map.put("order_type",80);
            ageList.add(map);
        }

        return ageList;

    }

    public void setRedis_Tags(String spu_id,Map map){

        //String json = JSONObject.parseObject(JSON.toJSONString(map)).toJSONString();
        String json = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);

        String old_json = redisService.getString(RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_ID + ":" + spu_id);
        if(!json.equals(old_json)){
            redisService.setString(RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_ID+":" + spu_id, json );
            String updateTime = DitaUtil.getCurrentAllDate();
            bookJpa.updateGoodTime(updateTime,spu_id);
        }

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

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("name","xxx");
        map.put("id",null);
        String json = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
        System.out.println(json);
    }

}

