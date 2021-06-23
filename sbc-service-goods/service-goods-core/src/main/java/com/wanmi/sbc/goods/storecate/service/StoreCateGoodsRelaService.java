package com.wanmi.sbc.goods.storecate.service;

import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.repository.StoreCateGoodsRelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/11/15 10:01
 * @version: 1.0
 */
@Service
public class StoreCateGoodsRelaService {

    @Autowired
    private StoreCateGoodsRelaRepository storeCateGoodsRelaRepository;

    /**
     * 根据商品ID查询
     * @param goodsIds 商品ID
     * @return
     */
    public List<StoreCateGoodsRela> selectByGoodsId(List<String> goodsIds){
        return storeCateGoodsRelaRepository.selectByGoodsId(goodsIds);
    }
}
