package com.wanmi.sbc.goodsPool.service.impl;

import com.alibaba.fastjson.JSON;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.bookmeta.bo.MetaLabelBO;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
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
public class VideoPoolServiceImpl implements PoolService {

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private MetaLabelProvider metaLabelProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    @Override
    public void getGoodsPool(List<GoodsPoolDto> goodsPoolDtos, List<ColumnContentDTO> poolCollect, MixedComponentTabDto pool, String keyword) {
        List<GoodsDto> goods = new ArrayList<>();
        for (ColumnContentDTO columnContentDTO : poolCollect) {
            String spuId = columnContentDTO.getSpuId();
            List<String> spuIds = new ArrayList<>();
            spuIds.add(spuId);
            EsKeyWordSpuNewQueryProviderReq es = new EsKeyWordSpuNewQueryProviderReq();
            es.setSpuIds(spuIds);
            es.setKeyword(keyword);
            List<EsSpuNewResp> content = esSpuNewProvider.listKeyWorldEsSpu(es).getContext().getResult().getContent();
            if (content.size() != 0) {
                getGoods(columnContentDTO, goods);
            }
        }
        if (goods.size() != 0) {
            GoodsPoolDto goodsPoolDto = getPool(pool, null, goods);
            goodsPoolDtos.add(goodsPoolDto);
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
        goodsPoolDto.setName(pool.getName());
        goodsPoolDto.setType(pool.getBookType());
        goodsPoolDto.setRecommend(pool.getRecommend());
        goodsPoolDto.setSorting(pool.getSorting());
        goodsPoolDto.setImage(JSON.parseObject(pool.getAttributeInfo()).get("image").toString());
        goodsPoolDto.setVideo(JSON.parseObject(pool.getAttributeInfo()).get("video").toString());
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
