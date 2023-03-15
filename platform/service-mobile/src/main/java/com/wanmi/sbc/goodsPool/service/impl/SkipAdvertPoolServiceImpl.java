package com.wanmi.sbc.goodsPool.service.impl;

import com.alibaba.fastjson.JSON;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.bookmeta.bo.SkuDetailBO;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.setting.bean.dto.ColumnContentDTO;
import com.wanmi.sbc.setting.bean.dto.GoodsDto;
import com.wanmi.sbc.setting.bean.dto.GoodsPoolDto;
import com.wanmi.sbc.setting.bean.dto.MixedComponentTabDto;

import java.util.List;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/15 16:15
 */
public class SkipAdvertPoolServiceImpl implements PoolService {
    @Override
    public void getGoodsPool(List<GoodsPoolDto> goodsPoolDtos, List<ColumnContentDTO> poolCollect, MixedComponentTabDto pool, String keyword) {
        GoodsPoolDto goodsPoolDto = getPool(pool, null, null);
        goodsPoolDtos.add(goodsPoolDto);
    }

    @Override
    public void getGoods(ColumnContentDTO columnContentDTO, List<GoodsDto> goods, EsSpuNewResp esSpuNewResp, SkuDetailBO skuDetailBO) {

    }

    @Override
    public GoodsPoolDto getPool(MixedComponentTabDto pool, ColumnContentDTO columnContentDTO, List<GoodsDto> goods) {
        GoodsPoolDto goodsPoolDto = new GoodsPoolDto();
        goodsPoolDto.setType(pool.getBookType());
        goodsPoolDto.setSorting(pool.getSorting());
        goodsPoolDto.setImage(JSON.parseObject(pool.getAttributeInfo()).get("image").toString());
        goodsPoolDto.setUrl(JSON.parseObject(pool.getAttributeInfo()).get("url").toString());
        goodsPoolDto.setGoods(goods);
        return goodsPoolDto;
    }
}
