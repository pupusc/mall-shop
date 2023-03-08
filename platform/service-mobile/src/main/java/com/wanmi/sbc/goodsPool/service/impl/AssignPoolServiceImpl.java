package com.wanmi.sbc.goodsPool.service.impl;

import com.alibaba.fastjson.JSON;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.bookmeta.bo.MetaLabelBO;
import com.wanmi.sbc.bookmeta.bo.SkuDetailBO;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.setting.bean.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/3 16:58
 */
@Service
public class AssignPoolServiceImpl implements PoolService {

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private MetaLabelProvider metaLabelProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    @Override
    public void getGoodsPool(List<GoodsPoolDto> goodsPoolDtos, List<ColumnContentDTO> poolCollect, MixedComponentTabDto pool, String keyword) {
        List<GoodsDto> goods = new ArrayList<>();
        for (ColumnContentDTO columnContentDTO : poolCollect) {
            EsKeyWordSpuNewQueryProviderReq es = new EsKeyWordSpuNewQueryProviderReq();
            es.setIsbn(columnContentDTO.getIsbn());
            es.setKeyword(keyword);
            List<EsSpuNewResp> content = esSpuNewProvider.listKeyWorldEsSpu(es).getContext().getResult().getContent();
            if (content.size() != 0) {
                EsSpuNewResp esSpuNewResp = content.get(0);
                getGoods(columnContentDTO, goods, esSpuNewResp);
            }
        }
        if (goods.size() != 0) {
            GoodsPoolDto goodsPoolDto = getPool(pool, null, goods);
            goodsPoolDtos.add(goodsPoolDto);
        }
    }

    @Override
    public void getGoods(ColumnContentDTO columnContentDTO, List<GoodsDto> goods, EsSpuNewResp res) {
        GoodsDto goodsDto = new GoodsDto();
        goodsDto.setSpuId(res.getSpuId());
        SkuDetailBO skuDetailBO = metaLabelProvider.getGoodsInfoBySpuId(goodsDto.getSpuId());
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
        Integer saleNum = skuDetailBO.getSaleNum() != null ? Integer.valueOf(skuDetailBO.getSaleNum()) : 0;
        String[] digit = {"十+", "百+", "千+", "万+"};
        if (saleNum >= 300) {
            String num = String.valueOf(saleNum / 100);
            score = num.length() == 1 ? saleNum.toString() : (num.length() == 2 ? num.substring(0,1) + digit[num.length()]
                    : num.substring(0, num.length()-2) + digit[3]);
        } else {
            //当图书库评分为空取商城商品评分
            score = score != null ? score : skuDetailBO.getScore();
        }
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
        //商品标签
        List<String> tags = new ArrayList<>();
        if (res.getLabels() != null) {
            res.getLabels().forEach(label -> tags.add(label.getLabelName()));
        }
        goodsDto.setTags(tags);
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
        goodsPoolDto.setTitleImage(JSON.parseObject(pool.getAttributeInfo()).get("titleImage").toString());
        goodsPoolDto.setImage(JSON.parseObject(pool.getAttributeInfo()).get("image").toString());
        goodsPoolDto.setGoods(goods);
        List<TagsDto> tabs = new ArrayList<>();
        if (pool.getLabelId() != null && !"".equals(pool.getLabelId())) {
            for (Integer s : pool.getLabelId()) {
                MetaLabelBO metaLabelBO = metaLabelProvider.queryById(s).getContext();
                TagsDto tagsDto = new TagsDto();
                tagsDto.setName(metaLabelBO.getName());
                tagsDto.setShowStatus(metaLabelBO.getStatus());
                tagsDto.setShowImg(metaLabelBO.getShowImg());
                tagsDto.setType(metaLabelBO.getType());
                tabs.add(tagsDto);
            }
        }
        //获取标签
        goodsPoolDto.setLabelId(tabs);
        return goodsPoolDto;
    }

}
