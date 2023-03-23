package com.wanmi.sbc.goodsPool.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewAggResp;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.bookmeta.bo.SkuDetailBO;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.enums.BookType;
import com.wanmi.sbc.util.DitaUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/3 16:58
 */
@Service
@Slf4j
public class GoodsPoolServiceImpl implements PoolService {

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MetaLabelProvider metaLabelProvider;

    @Autowired
    private RedisService redisService;
    @Override
    public void getGoodsPool(List<GoodsPoolDto> goodsPoolDtos, List<ColumnContentDTO> poolCollect, MixedComponentTabDto pool, String keyword) {
        for (ColumnContentDTO columnContentDTO : poolCollect) {
            //String spuId = columnContentDTO.getSpuId();
            String skuNo = columnContentDTO.getSkuNo();
            if (DitaUtil.isNotBlank(skuNo)) {
                skuNo = skuNo.trim().replaceAll("\n", "");
                columnContentDTO.setSkuNo(skuNo);
            }
            SkuDetailBO skuDetailBO = getSpuBySkuId(skuNo);
            String spuId = skuDetailBO.getSpuId();
            List<String> spuIds = new ArrayList<>();
            spuIds.add(spuId);
            EsKeyWordSpuNewQueryProviderReq es = new EsKeyWordSpuNewQueryProviderReq();
            es.setSpuIds(spuIds);
            //es.setKeyword(keyword);
            EsSpuNewAggResp<List<EsSpuNewResp>> esSpuNewAggResp = esSpuNewProvider.listKeyWorldEsSpu(es).getContext();
            List<EsSpuNewResp> content = esSpuNewAggResp != null ? (esSpuNewAggResp.getResult() != null ? esSpuNewAggResp.getResult().getContent() : null) : null;
            if (content != null && content.size() != 0) {
                EsSpuNewResp esSpuNewResp = content.get(0);
                List<GoodsDto> goods = new ArrayList<>();
                getGoods(columnContentDTO, goods, esSpuNewResp, skuDetailBO);
                GoodsPoolDto goodsPoolDto = getPool(pool, columnContentDTO, goods);
                goodsPoolDtos.add(goodsPoolDto);
            } else {
                log.info("时间:{},方法:{},skuNo参数:{},skuDetailBo:{}",
                        DateUtil.format(new Date(),DateUtil.FMT_TIME_1),
                        "getGoodsPool",
                        Objects.isNull(columnContentDTO.getSkuNo())?"":JSON.toJSONString(columnContentDTO.getSkuNo()),
                        Objects.isNull(skuDetailBO)?"":JSON.toJSONString(skuDetailBO));
            }
        }
    }

    //初始化商品
    @Override
    public void getGoods(ColumnContentDTO columnContentDTO, List<GoodsDto> goods, EsSpuNewResp res ,SkuDetailBO skuDetailBO) {
        GoodsDto goodsDto = new GoodsDto();
        goodsDto.setSpuId(skuDetailBO.getSpuId());
        //SkuDetailBO skuDetailBO = metaLabelProvider.getGoodsInfoBySpuId(goodsDto.getSpuId());
        goodsDto.setSkuId(skuDetailBO.getSkuId());
        goodsDto.setGoodsName(skuDetailBO.getSkuName());
        goodsDto.setSpecMore(Integer.parseInt(skuDetailBO.getSpecNum()) >= 2 ? true : false);
        String isbn = columnContentDTO.getIsbn() != null ? columnContentDTO.getIsbn() : (res.getBook() != null ? res.getBook().getIsbn() : null);
        String score = null;
        if (isbn != null) {
            goodsDto.setIsbn(isbn);
            List context = bookListModelProvider.getBookRecommend(isbn).getContext();
            String name = null;
            if (context.size() != 0) {
                Map map = (Map) context.get(0);
                score = map.get("score") != null ? map.get("score").toString() : null;
                name = map.get("name") != null ? map.get("name").toString() : null ;
                goodsDto.setRecommend(map.get("descr") != null ? map.get("descr").toString() : null);
                goodsDto.setRecommendName(name);
                goodsDto.setReferrerTitle(map.get("job_title") != null ? map.get("job_title").toString() : null);
            }
            goodsDto.setReferrer(name == null ? "文喵" : name);
        }
        goodsDto.setImage(skuDetailBO.getImg() != null ? skuDetailBO.getImg() : (res.getUnBackgroundPic() != null ? res.getUnBackgroundPic() : res.getPic()));
        //当图书库评分为空取商城商品评分
        score = score != null ? score : skuDetailBO.getScore();
        goodsDto.setScore(score);
        goodsDto.setRetailPrice(skuDetailBO.getPrice());
        //营销标签
        List<String> tagList = getMarkingSku(skuDetailBO.getSkuId()).stream().map(s -> String.valueOf(s.get("name"))).collect(Collectors.toList());
        goodsDto.setTags(tagList);
        //买点标签
        String tagJson = goodsInfoQueryProvider.getRedis(res.getSpuId()).getContext();
        if (JSON.parseObject(tagJson) != null) {
            List<TagsDto> tagsDtos = new ArrayList<>();
            List tags = (List) JSON.parseObject(tagJson).get("tags");
            if (tags != null) {
                tags.forEach(s -> {
                    TagsDto tagsDto = new TagsDto();
                    Map map = (Map) s;
                    tagsDto.setName(String.valueOf(map.get("show_name")));
                    tagsDto.setType((Integer) map.get("order_type"));
                    tagsDto.setId((Integer) map.get("id"));
                    if (!StringUtils.isEmpty(tagsDto.getName()) && !"null".equals(tagsDto.getName())) {
                        if(!StringUtils.isEmpty(tagsDto.getType()) && "20".equals(tagsDto.getType())) {
                            tagsDtos.add(0, tagsDto);
                        } else {
                            tagsDtos.add(tagsDto);
                        }
                    }
                });
            }
            //获取标签
            goodsDto.setLabelId(tagsDtos);
        }
        goods.add(goodsDto);
    }

    //初始化商品池
    @Override
    public GoodsPoolDto getPool(MixedComponentTabDto pool, ColumnContentDTO columnContentDTO, List<GoodsDto> goods) {
        GoodsPoolDto goodsPoolDto = new GoodsPoolDto();
        if(goods.get(0).getIsbn() != null) {
            goodsPoolDto.setType(pool.getBookType());
        } else {
            goodsPoolDto.setType(BookType.NOT_BOOK.toValue());
        }
        goodsPoolDto.setSorting(columnContentDTO.getSorting());
        goodsPoolDto.setGoods(goods);
        return goodsPoolDto;
    }

    private SkuDetailBO getSpuBySkuId(String skuId) {
        SkuDetailBO skuDetailBO = metaLabelProvider.getGoodsInfoBySkuId(skuId);
        return skuDetailBO;
    }

    public List<Map> getMarkingSku(String sku_id) {

        Map labelMap = new HashMap();
        //读取公用标签
        String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + sku_id);
        List<Map> labelNewList = new ArrayList<Map>();
        if (null != old_json) {
            labelMap = JSONObject.parseObject(old_json, Map.class);
            if(labelMap.get("labels") != null) {
                List<Map> labelList = (List)labelMap.get("labels");
                labelNewList = labelList.stream().filter(s -> s.get("order_type") != null && Integer.valueOf(String.valueOf(s.get("order_type"))) <= 50)
                        .collect(Collectors.toList());
                labelMap.put("labels", labelNewList);
            }
        }
        return labelNewList;
    }

}
