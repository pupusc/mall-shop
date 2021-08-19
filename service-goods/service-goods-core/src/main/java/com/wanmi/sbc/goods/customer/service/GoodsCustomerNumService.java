package com.wanmi.sbc.goods.customer.service;

import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.api.request.customer.GoodsCustomerNumRequest;
import com.wanmi.sbc.goods.customer.model.root.GoodsCustomerNum;
import com.wanmi.sbc.goods.customer.repository.GoodsCustomerNumRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品的客户数量服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class GoodsCustomerNumService {

    @Autowired
    private GoodsCustomerNumRepository goodsCustomerNumRepository;

    /**
     * 更新商品的客户数量
     * @param request
     */
    @Transactional
    public void save(GoodsCustomerNumRequest request) {
        List<GoodsCustomerNum> goodsCustomerNumList = goodsCustomerNumRepository.findByGoodsInfoId(request.getCustomerId(), request.getGoodsInfoId());
        if(CollectionUtils.isNotEmpty(goodsCustomerNumList)){
            GoodsCustomerNum goodsCustomerNum = goodsCustomerNumList.get(0);
            //是否叠加购买数
            if(Constants.yes.equals(request.getAddFlag())){
                goodsCustomerNum.setGoodsNum(goodsCustomerNum.getGoodsNum()+request.getGoodsNum());
            }else {
                goodsCustomerNum.setGoodsNum(request.getGoodsNum());
            }
            goodsCustomerNumRepository.save(goodsCustomerNum);
            return;
        }
        GoodsCustomerNum num = new GoodsCustomerNum();
        num.setGoodsInfoId(request.getGoodsInfoId());
        num.setCustomerId(request.getCustomerId());
        num.setGoodsNum(request.getGoodsNum());
        goodsCustomerNumRepository.save(num);
    }

}
