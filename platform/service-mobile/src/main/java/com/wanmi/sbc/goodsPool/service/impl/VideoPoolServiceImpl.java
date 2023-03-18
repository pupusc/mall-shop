package com.wanmi.sbc.goodsPool.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.bookmeta.bo.MetaLabelBO;
import com.wanmi.sbc.bookmeta.bo.SkuDetailBO;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.bean.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/3 16:58
 */
@Service
public class VideoPoolServiceImpl implements PoolService {

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private MetaLabelProvider metaLabelProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private RedisService redisService;

    @Override
    public void getGoodsPool(List<GoodsPoolDto> goodsPoolDtos, List<ColumnContentDTO> poolCollect, MixedComponentTabDto pool, String keyword) {
        List<GoodsDto> goods = new ArrayList<>();
        for (ColumnContentDTO columnContentDTO : poolCollect) {
            SkuDetailBO skuDetailBO = getSpuBySkuId(columnContentDTO.getSkuNo());
            EsKeyWordSpuNewQueryProviderReq es = new EsKeyWordSpuNewQueryProviderReq();
            List<String> spuIds = new ArrayList<>();
            spuIds.add(skuDetailBO.getSpuId());
            es.setSpuIds(spuIds);
            //es.setIsbn(columnContentDTO.getIsbn());
            //es.setKeyword(keyword);
            List<EsSpuNewResp> content = esSpuNewProvider.listKeyWorldEsSpu(es).getContext().getResult().getContent();
            if (content.size() != 0) {
                EsSpuNewResp esSpuNewResp = content.get(0);
                getGoods(columnContentDTO, goods, esSpuNewResp, skuDetailBO);
            }
        }
        if (goods.size() != 0) {
            GoodsPoolDto goodsPoolDto = getPool(pool, null, goods);
            goodsPoolDtos.add(goodsPoolDto);
        }
    }

    @Override
    public void getGoods(ColumnContentDTO columnContentDTO, List<GoodsDto> goods, EsSpuNewResp res ,SkuDetailBO skuDetailBO) {
        GoodsDto goodsDto = new GoodsDto();
        goodsDto.setSpuId(skuDetailBO.getSpuId());
        //SkuDetailBO skuDetailBO = metaLabelProvider.getGoodsInfoBySpuId(goodsDto.getSpuId());
        goodsDto.setSkuId(skuDetailBO.getSkuId());
        goodsDto.setGoodsName(skuDetailBO.getSkuName());
        goodsDto.setImage(skuDetailBO.getImg() != null ? skuDetailBO.getImg() : (res.getUnBackgroundPic() != null ? res.getUnBackgroundPic() : res.getPic()));
        String score = null;
        String isbn = columnContentDTO.getIsbn() != null ? columnContentDTO.getIsbn() : (res.getBook() != null ? res.getBook().getIsbn() : null);
        if (isbn != null) {
            goodsDto.setIsbn(isbn);
            List context = bookListModelProvider.getBookRecommend(isbn).getContext();
            if (context.size() != 0) {
                Map map = (Map) context.get(0);
                score = map.get("score") != null ? map.get("score").toString() : null;
            }
        }
        //当图书库评分为空取商城商品评分
        score = score != null ? score : skuDetailBO.getScore();
        goodsDto.setScore(score);
        goodsDto.setRetailPrice(skuDetailBO.getPrice());
        if (JSON.parseObject(goodsInfoQueryProvider.getRedis(res.getSpuId()).getContext()) != null) {
            List tags = (List) JSON.parseObject(goodsInfoQueryProvider.getRedis(goodsDto.getSpuId()).getContext()).get("tags");
            if (tags != null) {
                tags.forEach(s -> {
                    TagsDto tagsDto = new TagsDto();
                    Map tagMap = (Map) s;
                    if ("20".equals(String.valueOf(tagMap.get("order_type")))) {
                        goodsDto.setListMessage(String.valueOf(tagMap.get("show_name")));
                    }
                });
            }
        }
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
        if (goods.size() <= 5) {
            goods.add(goodsDto);
        } else {
            return;
        }
    }

    @Override
    public GoodsPoolDto getPool(MixedComponentTabDto pool, ColumnContentDTO columnContentDTO, List<GoodsDto> goods) {
        GoodsPoolDto goodsPoolDto = new GoodsPoolDto();
        goodsPoolDto.setName(pool.getName());
        goodsPoolDto.setType(pool.getBookType());
        goodsPoolDto.setRecommend(pool.getRecommend());
        goodsPoolDto.setSorting(pool.getSorting());
        goodsPoolDto.setImage(JSON.parseObject(pool.getAttributeInfo()).get("image").toString());
        goodsPoolDto.setVideo(JSON.parseObject(pool.getAttributeInfo()).get("url").toString());
        goodsPoolDto.setGoods(goods);
//        List<TagsDto> tabs = new ArrayList<>();
//        if (pool.getLabelId() != null && !"".equals(pool.getLabelId())) {
//            for (Integer s : pool.getLabelId()) {
//                MetaLabelBO metaLabelBO = metaLabelProvider.queryById(s).getContext();
//                TagsDto tagsDto = new TagsDto();
//                tagsDto.setName(metaLabelBO.getName());
//                tagsDto.setShowStatus(metaLabelBO.getStatus());
//                tagsDto.setShowImg(metaLabelBO.getShowImg());
//                tagsDto.setType(metaLabelBO.getType());
//                tabs.add(tagsDto);
//            }
//        }
        //获取标签
//        goodsPoolDto.setLabelId(tabs);
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
