package com.wanmi.sbc.goodsPool.service.impl;

import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
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
                    List<GoodsDto> goods = new ArrayList<>();
                    getGoods(columnContentDTO, goods);
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
    public void getGoods(ColumnContentDTO columnContentDTO, List<GoodsDto> goods) {
        GoodsDto goodsDto = new GoodsDto();
        goodsDto.setSpuId(columnContentDTO.getSpuId());
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
