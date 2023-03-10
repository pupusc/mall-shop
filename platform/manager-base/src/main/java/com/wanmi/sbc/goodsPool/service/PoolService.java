package com.wanmi.sbc.goodsPool.service;

import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.setting.bean.dto.ColumnContentDTO;
import com.wanmi.sbc.setting.bean.dto.GoodsDto;
import com.wanmi.sbc.setting.bean.dto.GoodsPoolDto;
import com.wanmi.sbc.setting.bean.dto.MixedComponentTabDto;

import java.util.List;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/3/3 16:29
 */
public interface PoolService {

    void getGoodsPool(List<GoodsPoolDto> goodsPoolDtos, List<ColumnContentDTO> poolCollect, MixedComponentTabDto pool, String keyword);

    void getGoods(ColumnContentDTO columnContentDTO, List<GoodsDto> goods, EsSpuNewResp esSpuNewResp);
    GoodsPoolDto getPool(MixedComponentTabDto pool, ColumnContentDTO columnContentDTO, List<GoodsDto> goods);
}
