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
    private CacheService cacheService;

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



    public void doGoods(Map goodMap){

        String spu_no = String.valueOf(goodMap.get("spu"));
        String isbn = String.valueOf(goodMap.get("isbn"));
        String spu_id = String.valueOf(goodMap.get("spu_id"));

        //spu_id = "2c9a00ca86299cda01862a0163e60000"
        //spu_no = "P735546359";
        //isbn   = "ISBN_C_T003";

        //Map bookMap = bookJpa.getBookMap(isbn);               //1.读数据库
        Map bookMap = cacheService.getBookMap_cache(isbn);      //2.读缓存

        if(bookMap == null || bookMap.size() == 0){
            return;
        }

        String book_id = String.valueOf(bookMap.get("id"));
        String trade_id = String.valueOf(bookMap.get("trade_id"));

        List allList = new ArrayList();

        //10. 大促标签
        /*List tagList1 = bookJpa.getTagList(book_id);
        if(tagList1!=null && tagList1.size() > 0){
            allList.addAll(tagList1);
        }*/

        //10. 大促标签_缓存
        List tagList1 = cacheService.getTagList_cache(book_id);
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

