package com.wanmi.sbc.goodsPool.service.impl;

import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.setting.bean.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/3 16:58
 */
@Service
public class AdvertisementPoolServiceImpl implements PoolService {

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;
    @Override
    public void getGoodsPool(List<GoodsPoolDto> goodsPoolDtos, List<ColumnContentDTO> poolCollect, MixedComponentTabDto pool, String keyword) {
        for (ColumnContentDTO columnContentDTO : poolCollect) {
            String spuId = columnContentDTO.getSpuId();
            if (! StringUtils.isEmpty(spuId)) {
                List<String> spuIds = new ArrayList<>();
                spuIds.add(spuId);
                EsKeyWordSpuNewQueryProviderReq es = new EsKeyWordSpuNewQueryProviderReq();
                es.setSpuIds(spuIds);
                es.setKeyword(keyword);
                List<EsSpuNewResp> content = esSpuNewProvider.listKeyWorldEsSpu(es).getContext().getResult().getContent();
                if (content.size() != 0) {
                    EsSpuNewResp esSpuNewResp = content.get(0);
                    List<GoodsDto> goods = new ArrayList<>();
                    getGoods(columnContentDTO, goods,esSpuNewResp);
                    GoodsPoolDto goodsPoolDto = getPool(pool, columnContentDTO, goods);
                    goodsPoolDtos.add(goodsPoolDto);
                }
            } else if (! StringUtils.isEmpty(columnContentDTO.getLinkUrl())) {
                GoodsPoolDto goodsPoolDto = getPool(pool, columnContentDTO, null);
                goodsPoolDtos.add(goodsPoolDto);
            }
        }
    }

    @Override
    public void getGoods(ColumnContentDTO columnContentDTO, List<GoodsDto> goods, EsSpuNewResp res) {
        GoodsDto goodsDto = new GoodsDto();
        goodsDto.setSpuId(columnContentDTO.getSpuId());
        //goodsDto.setSkuId();
        String score = null;
        String isbn = columnContentDTO.getIsbn() != null ? columnContentDTO.getIsbn() : res.getBook().getIsbn();
        if (isbn != null) {
            goodsDto.setIsbn(isbn);
            List context = bookListModelProvider.getBookRecommend(isbn).getContext();
            if (context.size() != 0) {
                Map map = (Map) context.get(0);
                score = map.get("score") != null ? map.get("score").toString() : null;
            }
        }
//        if (goodsInfoVO.getGoodsSalesNum() >= 1000000) {
//            score = goodsInfoVO.getGoodsSalesNum().toString().substring(0, 3) + "万+";
//        } else if (goodsInfoVO.getGoodsSalesNum() >= 100000) {
//            score = goodsInfoVO.getGoodsSalesNum().toString().substring(0, 2) + "万+";
//        } else if (goodsInfoVO.getGoodsSalesNum() >= 10000) {
//            score = goodsInfoVO.getGoodsSalesNum().toString().substring(0, 1) + "万+";
//        } else if (goodsInfoVO.getGoodsSalesNum() >= 1000) {
//            score = goodsInfoVO.getGoodsSalesNum().toString().substring(0, 1) + "千+";
//        } else if (goodsInfoVO.getGoodsSalesNum() >= 100) {
//            score = goodsInfoVO.getGoodsSalesNum().toString().substring(0, 1) + "百+";
//        } else {
//            //当图书库评分为空取商城商品评分
//            score = score != null ? score : null;
//        }
        goodsDto.setScore(score);
        goodsDto.setRetailPrice(res.getSalesPrice());
        //商品标签
        List<String> tags = new ArrayList<>();
        if (res.getLabels() != null) {
            res.getLabels().forEach(label -> tags.add(label.getLabelName()));
        }
        goodsDto.setTags(tags);
        goods.add(goodsDto);
    }

    @Override
    public GoodsPoolDto getPool(MixedComponentTabDto pool, ColumnContentDTO columnContentDTO, List<GoodsDto> goods) {
        GoodsPoolDto goodsPoolDto = new GoodsPoolDto();
        goodsPoolDto.setType(pool.getBookType());
        goodsPoolDto.setSorting(columnContentDTO.getSorting());
        goodsPoolDto.setImage(columnContentDTO.getImageUrl());
        goodsPoolDto.setUrl(goods == null ? columnContentDTO.getLinkUrl() : null);
        goodsPoolDto.setGoods(goods);
        return goodsPoolDto;
    }
}
