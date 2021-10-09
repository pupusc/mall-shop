package com.wanmi.sbc.goods.hotgoods.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.dto.HotGoodsDto;
import com.wanmi.sbc.goods.hotgoods.model.root.HotGoods;
import com.wanmi.sbc.goods.hotgoods.repository.HotGoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品EXCEL处理服务
 * Created by dyt on 2017/8/17.
 */
@Service
public class HotGoodsService {


    @Autowired
    private HotGoodsRepository hotGoodsRepository;

    /**
     * 刷新排序
     */
    @Transactional
    public Integer updateSort() {
        Integer count = hotGoodsRepository.updateSort();
        return count;
    }
    /**
     * 刷新排序
     */
    public List<HotGoodsDto> selectAllBySort() {
        List<HotGoods> hotGoods = hotGoodsRepository.selectAllBySort();
        List<HotGoodsDto> hotGoodsDtos = KsBeanUtil.convertList(hotGoods, HotGoodsDto.class);
        return hotGoodsDtos;
    }
}