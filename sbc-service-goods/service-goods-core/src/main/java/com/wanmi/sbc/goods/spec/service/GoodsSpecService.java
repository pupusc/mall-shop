package com.wanmi.sbc.goods.spec.service;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecExportVO;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecDetailRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecRepository;
import io.seata.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品规格接口服务
 * @author: daiyitian
 * @createDate: 2018/11/13 14:54
 * @version: 1.0
 */
@Service
public class GoodsSpecService {

    @Autowired
    private GoodsSpecRepository goodsSpecRepository;

    @Autowired
    private GoodsSpecDetailRepository goodsSpecDetailRepository;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;


    /**
     * 根据商品id批量查询规格列表
     * @param goodsIds 商品id
     * @return 规格列表
     */
    public List<GoodsSpec> findByGoodsIds(List<String> goodsIds){
       return goodsSpecRepository.findByGoodsIds(goodsIds);
    }

    /**
     * 获取规格和规格值
     * @param goodsIds
     * @return
     */
    public Map<String, List<GoodsInfoSpecExportVO>> findGoodsInfoSpecForExport(List<String> goodsIds) {
        Map<String, List<GoodsInfoSpecExportVO>> map = new HashMap<>();
        List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRelList = goodsInfoSpecDetailRelRepository.findByGoodsIds(goodsIds);
        if(CollectionUtils.isNotEmpty(goodsInfoSpecDetailRelList)) {
            Map<Long, String> specMap = goodsSpecRepository.findByGoodsIds(goodsIds).stream().collect(Collectors.toMap(GoodsSpec::getSpecId, GoodsSpec::getSpecName));
            Map<Long, String> detailMap = goodsSpecDetailRepository.findByGoodsIds(goodsIds).stream().collect(Collectors.toMap(GoodsSpecDetail::getSpecDetailId, GoodsSpecDetail::getDetailName));
            map = goodsInfoSpecDetailRelList.stream().map(goodsInfoSpecDetailRel -> {
                GoodsInfoSpecExportVO exportVO = new GoodsInfoSpecExportVO();
                String specName = specMap.get(goodsInfoSpecDetailRel.getSpecId());
                String detailName = detailMap.get(goodsInfoSpecDetailRel.getSpecDetailId());
                exportVO.setGoodsInfoId(goodsInfoSpecDetailRel.getGoodsInfoId());
                exportVO.setSpecName(specName);
                exportVO.setSpecDetailName(detailName);
                return exportVO;
            }).collect(Collectors.groupingBy(GoodsInfoSpecExportVO::getGoodsInfoId));
        }
        return map;
    }
}
