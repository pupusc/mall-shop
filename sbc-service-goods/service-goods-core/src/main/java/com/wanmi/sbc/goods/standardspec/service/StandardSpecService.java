package com.wanmi.sbc.goods.standardspec.service;

import com.wanmi.sbc.goods.bean.vo.StandardSkuVO;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import com.wanmi.sbc.goods.standardspec.repository.StandardSkuSpecDetailRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;

/**
 * 商品规格服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
public class StandardSpecService {

    @Autowired
    private StandardSkuSpecDetailRelRepository standardSkuSpecDetailRelRepository;

    /**
     * 根据多个SkuID查询
     * @param goodsInfoIds 多SkuID
     * @return
     */
    private List<StandardSkuSpecDetailRel> findByGoodsInfoIds(List<String> goodsInfoIds){
        return standardSkuSpecDetailRelRepository.findByGoodsInfoIds(goodsInfoIds);
    }

    /**
     * 根据多个SkuID返回格式化的规格
     *
     * @param goodsInfoIds 多SkuID
     * @return <skuId,specText>
     */
    private Map<String, String> textByGoodsInfoIds(List<String> goodsInfoIds) {
        return this.findByGoodsInfoIds(goodsInfoIds).stream().collect(
                Collectors.groupingBy(StandardSkuSpecDetailRel::getGoodsInfoId,
                        mapping(StandardSkuSpecDetailRel::getDetailName, joining(" "))));
    }

    /**
     * 填充格式化的规格
     *
     * @param goodsInfoVOS 商品SKu列表
     */
    public void fillSpecDetail(List<StandardSkuVO> goodsInfoVOS) {
        List<String> goodsInfoIds = goodsInfoVOS.stream().map(StandardSkuVO::getGoodsInfoId).collect(Collectors.toList());
        Map<String, String> specDetailMap = this.textByGoodsInfoIds(goodsInfoIds);
        goodsInfoVOS.forEach(i -> i.setSpecText(specDetailMap.get(i.getGoodsInfoId())));
    }
}
