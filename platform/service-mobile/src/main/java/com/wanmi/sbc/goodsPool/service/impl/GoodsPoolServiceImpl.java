package com.wanmi.sbc.goodsPool.service.impl;

import com.alibaba.fastjson.JSON;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.bookmeta.bo.SkuDetailBO;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.setting.bean.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/3 16:58
 */
@Service
public class GoodsPoolServiceImpl implements PoolService {

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MetaLabelProvider metaLabelProvider;
    @Override
    public void getGoodsPool(List<GoodsPoolDto> goodsPoolDtos, List<ColumnContentDTO> poolCollect, MixedComponentTabDto pool, String keyword) {
        for (ColumnContentDTO columnContentDTO : poolCollect) {
            //String spuId = columnContentDTO.getSpuId();
            SkuDetailBO skuDetailBO = getSpuBySkuId(columnContentDTO.getSkuNo());
            String spuId = skuDetailBO.getSpuId();
            List<String> spuIds = new ArrayList<>();
            spuIds.add(spuId);
            EsKeyWordSpuNewQueryProviderReq es = new EsKeyWordSpuNewQueryProviderReq();
            es.setSpuIds(spuIds);
            //es.setKeyword(keyword);
            List<EsSpuNewResp> content = esSpuNewProvider.listKeyWorldEsSpu(es).getContext().getResult().getContent();
            if (content.size() != 0) {
                EsSpuNewResp esSpuNewResp = content.get(0);
                List<GoodsDto> goods = new ArrayList<>();
                getGoods(columnContentDTO, goods, esSpuNewResp, skuDetailBO);
                GoodsPoolDto goodsPoolDto = getPool(pool, columnContentDTO, goods);
                goodsPoolDtos.add(goodsPoolDto);
            }
        }
    }

    //初始化商品
    @Override
    public void getGoods(ColumnContentDTO columnContentDTO, List<GoodsDto> goods, EsSpuNewResp res ,SkuDetailBO skuDetailBO) {
        GoodsDto goodsDto = new GoodsDto();
        goodsDto.setSpuId(columnContentDTO.getSpuId());
        //SkuDetailBO skuDetailBO = metaLabelProvider.getGoodsInfoBySpuId(goodsDto.getSpuId());
        goodsDto.setSkuId(skuDetailBO.getSkuId());
        goodsDto.setGoodsName(skuDetailBO.getSkuName());
        String isbn = columnContentDTO.getIsbn() != null ? columnContentDTO.getIsbn() : res.getBook().getIsbn();
        String score = null;
        if (isbn != null) {
            goodsDto.setIsbn(isbn);
            List context = bookListModelProvider.getBookRecommend(isbn).getContext();
            if (context.size() != 0) {
                Map map = (Map) context.get(0);
                score = map.get("score") != null ? map.get("score").toString() : null;
                String name = map.get("name") != null ? map.get("name").toString() : null ;
                goodsDto.setRecommend(map.get("descr") != null ? map.get("descr").toString() : null);
                goodsDto.setRecommendName(name);
                goodsDto.setReferrer(name == null ? "文喵" : name);
                goodsDto.setReferrerTitle(map.get("job_title") != null ? map.get("job_title").toString() : null);
            }
        }
        goodsDto.setImage(skuDetailBO.getImg() != null ? skuDetailBO.getImg() : (res.getUnBackgroundPic() != null ? res.getUnBackgroundPic() : res.getPic()));
        //当图书库评分为空取商城商品评分
        score = score != null ? score : skuDetailBO.getScore();
        goodsDto.setScore(score);
        goodsDto.setRetailPrice(skuDetailBO.getPrice());
        //营销标签
        List<String> tagList = new ArrayList<>();
        if (res.getLabels() != null) {
            res.getLabels().forEach(label -> tagList.add(label.getLabelName()));
        }
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
        goodsPoolDto.setType(pool.getBookType());
        goodsPoolDto.setSorting(columnContentDTO.getSorting());
        goodsPoolDto.setGoods(goods);
        return goodsPoolDto;
    }

    private SkuDetailBO getSpuBySkuId(String skuId) {
        SkuDetailBO skuDetailBO = metaLabelProvider.getGoodsInfoBySkuId(skuId);
        return skuDetailBO;
    }

}
